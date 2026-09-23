package com.hexagonalchess.gameplay;

import java.util.List;

public record BoardDTO(
    String id,
    String whitePlayerName,
    String blackPlayerName,
    String activeColor,
    List<PieceOnSquare> pieces
) {

    public record SquareDTO(int file, int rank) {}
    public record PieceDTO(String type, String color) {}
    public record PieceOnSquare(SquareDTO square, PieceDTO piece) {}

    public static BoardDTO from(Board board) {
        var pieces = board.pieces().entrySet().stream()
            .map(e -> new PieceOnSquare(
                new SquareDTO(e.getKey().file(), e.getKey().rank()),
                new PieceDTO(
                    e.getValue().getClass().getSimpleName(),
                    e.getValue().color().name().toLowerCase()
                )
            ))
            .toList();

        return new BoardDTO(
            board.id().value(),
            board.whitePlayerName(),
            board.blackPlayerName(),
            board.activeColor().name().toLowerCase(),
            pieces
        );
    }
}
