package com.hexagonalchess.gameplay;

public sealed interface Piece permits Piece.King, Piece.Queen, Piece.Rook, Piece.Bishop, Piece.Knight, Piece.Pawn {
    Color color();

    record King(Color color) implements Piece {}
    record Queen(Color color) implements Piece {}
    record Rook(Color color) implements Piece {}
    record Bishop(Color color) implements Piece {}
    record Knight(Color color) implements Piece {}
    record Pawn(Color color) implements Piece {}
}
