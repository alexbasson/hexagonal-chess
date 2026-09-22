package com.hexagonalchess.organizing;

public class StartGame {

    private final GameRepository gameRepository;
    private final GameInitializer gameInitializer;

    public StartGame(GameRepository gameRepository, GameInitializer gameInitializer) {
        this.gameRepository = gameRepository;
        this.gameInitializer = gameInitializer;
    }

    public GameId execute(String whiteName, String blackName) {
        var white = new Player(whiteName);
        var black = new Player(blackName);
        var gameId = gameInitializer.initializeGame(white, black);
        gameRepository.save(new Game(gameId, white, black));
        return gameId;
    }
}
