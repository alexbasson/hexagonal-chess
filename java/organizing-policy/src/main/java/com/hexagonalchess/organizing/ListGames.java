package com.hexagonalchess.organizing;

import java.util.List;

public class ListGames {

    private final GameRepository gameRepository;

    public ListGames(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> execute() {
        return gameRepository.findAll();
    }
}
