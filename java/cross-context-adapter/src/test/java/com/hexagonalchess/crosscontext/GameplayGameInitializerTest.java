package com.hexagonalchess.crosscontext;

import com.hexagonalchess.gameplay.BoardId;
import com.hexagonalchess.gameplay.SetupBoard;
import com.hexagonalchess.organizing.GameId;
import com.hexagonalchess.organizing.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameplayGameInitializerTest {

    @Mock private SetupBoard setupBoard;

    private GameplayGameInitializer initializer;

    @BeforeEach
    void setup() {
        initializer = new GameplayGameInitializer(setupBoard);
    }

    @Test
    void initialize_game_delegates_to_setup_board_and_translates_ids() {
        when(setupBoard.execute("Alice", "Bob")).thenReturn(new BoardId("board-123"));

        var result = initializer.initializeGame(new Player("Alice"), new Player("Bob"));

        assertThat(result).isEqualTo(new GameId("board-123"));
    }
}
