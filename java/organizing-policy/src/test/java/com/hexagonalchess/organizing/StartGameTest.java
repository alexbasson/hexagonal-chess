package com.hexagonalchess.organizing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartGameTest {

    @Mock private GameRepository gameRepository;
    @Mock private GameInitializer gameInitializer;

    private StartGame startGame;

    @BeforeEach
    void setup() {
        startGame = new StartGame(gameRepository, gameInitializer);
    }

    @Test
    void start_game_returns_a_game_id() {
        when(gameInitializer.initializeGame(new Player("Alice"), new Player("Bob")))
            .thenReturn(new GameId("game1"));
        var result = startGame.execute("Alice", "Bob");
        assertThat(result).isNotNull();
    }

    @Test
    void persists_game_with_player_names() {
        var gameId = new GameId("game1");
        when(gameInitializer.initializeGame(new Player("Alice"), new Player("Bob"))).thenReturn(gameId);

        startGame.execute("Alice", "Bob");

        var captor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.id()).isEqualTo(gameId);
        assertThat(saved.white()).isEqualTo(new Player("Alice"));
        assertThat(saved.black()).isEqualTo(new Player("Bob"));
    }
}
