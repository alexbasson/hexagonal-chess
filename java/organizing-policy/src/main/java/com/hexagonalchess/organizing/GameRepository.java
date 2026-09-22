package com.hexagonalchess.organizing;

import java.util.List;
import java.util.Optional;

public interface GameRepository {
    void save(Game game);
    Optional<Game> findById(GameId gameId);
    List<Game> findAll();
}
