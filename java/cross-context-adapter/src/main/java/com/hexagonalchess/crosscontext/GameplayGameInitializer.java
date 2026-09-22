package com.hexagonalchess.crosscontext;

import com.hexagonalchess.gameplay.SetupBoard;
import com.hexagonalchess.organizing.GameId;
import com.hexagonalchess.organizing.GameInitializer;
import com.hexagonalchess.organizing.Player;

public class GameplayGameInitializer implements GameInitializer {

    private final SetupBoard setupBoard;

    public GameplayGameInitializer(SetupBoard setupBoard) {
        this.setupBoard = setupBoard;
    }

    @Override
    public GameId initializeGame(Player white, Player black) {
        var boardId = setupBoard.execute(white.name(), black.name());
        return new GameId(boardId.value());
    }
}
