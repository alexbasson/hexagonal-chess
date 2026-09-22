package com.hexagonalchess.gameplay;

public record MoveRequest(String from, String to) {

    public Move toMove() {
        return new Move(parseSquare(from), parseSquare(to));
    }

    private static Square parseSquare(String notation) {
        int file = notation.charAt(0) - 'a' + 1;
        int rank = Character.getNumericValue(notation.charAt(1));
        return new Square(file, rank);
    }
}
