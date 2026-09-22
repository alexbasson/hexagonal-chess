package com.hexagonalchess.gameplay;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryBoardRepository implements BoardRepository {

    private final Map<BoardId, Board> store = new HashMap<>();

    @Override
    public void save(Board board) {
        store.put(board.id(), board);
    }

    @Override
    public Optional<Board> findById(BoardId boardId) {
        return Optional.ofNullable(store.get(boardId));
    }
}
