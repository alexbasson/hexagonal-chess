package com.hexagonalchess.gameplay;

import java.util.List;

public interface MoveRepository {
    void save(Move move, BoardId boardId);
    List<Move> findByBoardId(BoardId boardId);
}
