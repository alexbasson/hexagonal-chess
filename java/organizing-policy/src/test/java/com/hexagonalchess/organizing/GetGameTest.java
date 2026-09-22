package com.hexagonalchess.organizing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetGameTest {

    @Mock private GameRepository gameRepository;

    private GetGame getGame;

    @BeforeEach
    void setup() {
        getGame = new GetGame(gameRepository);
    }

    @Test
    void returns_game_when_found() {
        var gameId = new GameId("g1");
        var game = new Game(gameId, new Player("Alice"), new Player("Bob"));
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        assertThat(getGame.execute(gameId)).usingRecursiveComparison().isEqualTo(game);
    }

    @Test
    void throws_when_game_not_found() {
        var gameId = new GameId("unknown");
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> getGame.execute(gameId))
            .withMessageContaining("unknown");
    }
}
