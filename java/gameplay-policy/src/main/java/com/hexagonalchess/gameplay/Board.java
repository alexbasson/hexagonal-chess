package com.hexagonalchess.gameplay;

import java.util.Map;

public record Board(BoardId id, String whitePlayerName, String blackPlayerName, Color activeColor, Map<Square, Piece> pieces) {}
