package com.hexagonalchess.gameplay;

import java.util.Optional;

public interface BoardRepository {
    void save(Board board);
    Optional<Board> findById(BoardId boardId);
}
