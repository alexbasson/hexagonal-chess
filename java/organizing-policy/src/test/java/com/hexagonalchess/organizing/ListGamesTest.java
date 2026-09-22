package com.hexagonalchess.organizing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListGamesTest {

    @Mock private GameRepository gameRepository;

    private ListGames listGames;

    @BeforeEach
    void setup() {
        listGames = new ListGames(gameRepository);
    }

    @Test
    void returns_empty_list_when_no_games() {
        when(gameRepository.findAll()).thenReturn(List.of());
        assertThat(listGames.execute()).isEmpty();
    }

    @Test
    void returns_all_games() {
        var games = List.of(
            new Game(new GameId("g1"), new Player("Alice"), new Player("Bob")),
            new Game(new GameId("g2"), new Player("Carol"), new Player("Dave"))
        );
        when(gameRepository.findAll()).thenReturn(games);
        assertThat(listGames.execute())
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(games);
    }
}
