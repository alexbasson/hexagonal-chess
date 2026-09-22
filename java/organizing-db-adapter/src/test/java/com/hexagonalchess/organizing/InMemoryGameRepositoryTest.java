package com.hexagonalchess.organizing;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryGameRepositoryTest {

    private final InMemoryGameRepository repository = new InMemoryGameRepository();

    @Test
    void findById_returns_empty_when_no_game_saved() {
        assertThat(repository.findById(new GameId("unknown"))).isEmpty();
    }

    @Test
    void findAll_returns_empty_list_initially() {
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void findById_returns_saved_game() {
        var game = new Game(new GameId("g1"), new Player("Alice"), new Player("Bob"));
        repository.save(game);
        assertThat(repository.findById(new GameId("g1")))
            .usingRecursiveComparison()
            .isEqualTo(java.util.Optional.of(game));
    }

    @Test
    void findAll_returns_all_saved_games() {
        var g1 = new Game(new GameId("g1"), new Player("Alice"), new Player("Bob"));
        var g2 = new Game(new GameId("g2"), new Player("Carol"), new Player("Dave"));
        repository.save(g1);
        repository.save(g2);
        assertThat(repository.findAll())
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(List.of(g1, g2));
    }
}
