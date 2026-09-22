package com.hexagonalchess.gameplay;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SetupBoard {

    private final BoardRepository boardRepository;

    public SetupBoard(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardId execute(String whiteName, String blackName) {
        var boardId = new BoardId(UUID.randomUUID().toString());
        boardRepository.save(new Board(boardId, whiteName, blackName, Color.WHITE, initialPieces()));
        return boardId;
    }

    private Map<Square, Piece> initialPieces() {
        var pieces = new HashMap<Square, Piece>();
        placeBackRank(pieces, Color.WHITE, 1);
        placePawns(pieces, Color.WHITE, 2);
        placePawns(pieces, Color.BLACK, 7);
        placeBackRank(pieces, Color.BLACK, 8);
        return Map.copyOf(pieces);
    }

    private void placeBackRank(Map<Square, Piece> pieces, Color color, int rank) {
        pieces.put(new Square(1, rank), new Piece.Rook(color));
        pieces.put(new Square(2, rank), new Piece.Knight(color));
        pieces.put(new Square(3, rank), new Piece.Bishop(color));
        pieces.put(new Square(4, rank), new Piece.Queen(color));
        pieces.put(new Square(5, rank), new Piece.King(color));
        pieces.put(new Square(6, rank), new Piece.Bishop(color));
        pieces.put(new Square(7, rank), new Piece.Knight(color));
        pieces.put(new Square(8, rank), new Piece.Rook(color));
    }

    private void placePawns(Map<Square, Piece> pieces, Color color, int rank) {
        for (int file = 1; file <= 8; file++) {
            pieces.put(new Square(file, rank), new Piece.Pawn(color));
        }
    }
}
