package com.hexagonalchess.gameplay;

import java.util.HashMap;
import java.util.Map;

public class MakeMove {

    private final BoardRepository boardRepository;
    private final MoveRepository moveRepository;

    public MakeMove(BoardRepository boardRepository, MoveRepository moveRepository) {
        this.boardRepository = boardRepository;
        this.moveRepository = moveRepository;
    }

    public Board execute(BoardId boardId, Move move) {
        var board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("Board not found: " + boardId.value()));

        if (!board.pieces().containsKey(move.from())) {
            throw new IllegalArgumentException("No piece at " + move.from());
        }

        var piece = board.pieces().get(move.from());
        if (piece.color() != board.activeColor()) {
            throw new IllegalArgumentException("That is not your piece");
        }

        if (!isLegalMove(board, piece, move)) {
            throw new IllegalArgumentException("Illegal move: " + move);
        }

        var newPieces = applyMove(board.pieces(), move);

        if (isInCheck(newPieces, board.activeColor())) {
            throw new IllegalArgumentException("That move leaves your king in check");
        }

        var nextColor = board.activeColor() == Color.WHITE ? Color.BLACK : Color.WHITE;
        var newBoard = new Board(boardId, board.whitePlayerName(), board.blackPlayerName(), nextColor, newPieces);

        boardRepository.save(newBoard);
        moveRepository.save(move, boardId);

        return newBoard;
    }

    private boolean isLegalMove(Board board, Piece piece, Move move) {
        return switch (piece) {
            case Piece.Pawn p -> isLegalPawnMove(board, p.color(), move);
            case Piece.Knight k -> isLegalKnightMove(move);
            case Piece.Bishop b -> isLegalBishopMove(board, move);
            case Piece.Rook r -> isLegalRookMove(board, move);
            case Piece.Queen q -> isLegalQueenMove(board, move);
            case Piece.King k -> isLegalKingMove(move);
        };
    }

    private boolean isLegalPawnMove(Board board, Color color, Move move) {
        int direction = color == Color.WHITE ? 1 : -1;
        int fileDiff = move.to().file() - move.from().file();
        int rankDiff = move.to().rank() - move.from().rank();

        if (fileDiff == 0) {
            if (rankDiff == direction && !board.pieces().containsKey(move.to())) return true;
            int startingRank = color == Color.WHITE ? 2 : 7;
            if (rankDiff == 2 * direction && move.from().rank() == startingRank) {
                var intermediate = new Square(move.from().file(), move.from().rank() + direction);
                return !board.pieces().containsKey(intermediate) && !board.pieces().containsKey(move.to());
            }
        } else if (Math.abs(fileDiff) == 1 && rankDiff == direction) {
            var target = board.pieces().get(move.to());
            return target != null && target.color() != color;
        }
        return false;
    }

    private boolean isLegalKnightMove(Move move) {
        int fileDiff = Math.abs(move.to().file() - move.from().file());
        int rankDiff = Math.abs(move.to().rank() - move.from().rank());
        return (fileDiff == 1 && rankDiff == 2) || (fileDiff == 2 && rankDiff == 1);
    }

    private boolean isLegalBishopMove(Board board, Move move) {
        int fileDiff = Math.abs(move.to().file() - move.from().file());
        int rankDiff = Math.abs(move.to().rank() - move.from().rank());
        return fileDiff == rankDiff && fileDiff > 0 && isDiagonalClear(board, move);
    }

    private boolean isLegalRookMove(Board board, Move move) {
        if (move.from().file() != move.to().file() && move.from().rank() != move.to().rank()) return false;
        return isStraightClear(board, move);
    }

    private boolean isLegalQueenMove(Board board, Move move) {
        return isLegalBishopMove(board, move) || isLegalRookMove(board, move);
    }

    private boolean isLegalKingMove(Move move) {
        int fileDiff = Math.abs(move.to().file() - move.from().file());
        int rankDiff = Math.abs(move.to().rank() - move.from().rank());
        return fileDiff <= 1 && rankDiff <= 1 && (fileDiff + rankDiff > 0);
    }

    private boolean isDiagonalClear(Board board, Move move) {
        int fileStep = move.to().file() > move.from().file() ? 1 : -1;
        int rankStep = move.to().rank() > move.from().rank() ? 1 : -1;
        int file = move.from().file() + fileStep;
        int rank = move.from().rank() + rankStep;
        while (file != move.to().file()) {
            if (board.pieces().containsKey(new Square(file, rank))) return false;
            file += fileStep;
            rank += rankStep;
        }
        var target = board.pieces().get(move.to());
        return target == null || target.color() != board.pieces().get(move.from()).color();
    }

    private boolean isStraightClear(Board board, Move move) {
        int fileStep = Integer.signum(move.to().file() - move.from().file());
        int rankStep = Integer.signum(move.to().rank() - move.from().rank());
        int file = move.from().file() + fileStep;
        int rank = move.from().rank() + rankStep;
        while (file != move.to().file() || rank != move.to().rank()) {
            if (board.pieces().containsKey(new Square(file, rank))) return false;
            file += fileStep;
            rank += rankStep;
        }
        var target = board.pieces().get(move.to());
        return target == null || target.color() != board.pieces().get(move.from()).color();
    }

    private boolean isInCheck(Map<Square, Piece> pieces, Color kingColor) {
        var kingSquare = pieces.entrySet().stream()
            .filter(e -> e.getValue() instanceof Piece.King && e.getValue().color() == kingColor)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);
        if (kingSquare == null) return false;

        var opponentColor = kingColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        var tempBoard = new Board(new BoardId("temp"), "", "", opponentColor, pieces);
        return pieces.entrySet().stream()
            .filter(e -> e.getValue().color() == opponentColor)
            .anyMatch(e -> isLegalMove(tempBoard, e.getValue(), new Move(e.getKey(), kingSquare)));
    }

    private Map<Square, Piece> applyMove(Map<Square, Piece> pieces, Move move) {
        var result = new HashMap<>(pieces);
        result.put(move.to(), result.remove(move.from()));
        return Map.copyOf(result);
    }
}
