package com.hexagonalchess.organizing;

public class GetGame {

    private final GameRepository gameRepository;

    public GetGame(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game execute(GameId gameId) {
        return gameRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId.value()));
    }
}
