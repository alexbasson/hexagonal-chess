package com.hexagonalchess.gameplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryMoveRepository implements MoveRepository {

    private final Map<BoardId, List<Move>> store = new HashMap<>();

    @Override
    public void save(Move move, BoardId boardId) {
        store.computeIfAbsent(boardId, k -> new ArrayList<>()).add(move);
    }

    @Override
    public List<Move> findByBoardId(BoardId boardId) {
        return store.getOrDefault(boardId, List.of());
    }
}
