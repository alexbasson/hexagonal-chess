package com.hexagonalchess.gameplay;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryBoardRepositoryTest {

    private final InMemoryBoardRepository repository = new InMemoryBoardRepository();

    @Test
    void findById_returns_empty_when_no_board_saved() {
        assertThat(repository.findById(new BoardId("unknown"))).isEmpty();
    }

    @Test
    void findById_returns_saved_board() {
        var board = new Board(new BoardId("b1"), "Alice", "Bob", Color.WHITE, Map.of());
        repository.save(board);
        assertThat(repository.findById(new BoardId("b1")))
            .usingRecursiveComparison()
            .isEqualTo(Optional.of(board));
    }
}
