package com.hexagonalchess.organizing;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryGameRepository implements GameRepository {

    private final Map<GameId, Game> store = new LinkedHashMap<>();

    @Override
    public void save(Game game) {
        store.put(game.id(), game);
    }

    @Override
    public Optional<Game> findById(GameId gameId) {
        return Optional.ofNullable(store.get(gameId));
    }

    @Override
    public List<Game> findAll() {
        return new ArrayList<>(store.values());
    }
}
