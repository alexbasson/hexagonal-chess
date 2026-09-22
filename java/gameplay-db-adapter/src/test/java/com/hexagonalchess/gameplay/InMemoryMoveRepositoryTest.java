package com.hexagonalchess.gameplay;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryMoveRepositoryTest {

    private final InMemoryMoveRepository repository = new InMemoryMoveRepository();

    @Test
    void findByBoardId_returns_empty_list_initially() {
        assertThat(repository.findByBoardId(new BoardId("b1"))).isEmpty();
    }

    @Test
    void findByBoardId_returns_saved_moves_for_board() {
        var boardId = new BoardId("b1");
        var move = new Move(new Square(5, 2), new Square(5, 4));
        repository.save(move, boardId);
        assertThat(repository.findByBoardId(boardId))
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(java.util.List.of(move));
    }

    @Test
    void findByBoardId_only_returns_moves_for_requested_board() {
        var boardA = new BoardId("a");
        var boardB = new BoardId("b");
        repository.save(new Move(new Square(1, 2), new Square(1, 4)), boardA);
        repository.save(new Move(new Square(5, 7), new Square(5, 5)), boardB);
        assertThat(repository.findByBoardId(boardA)).hasSize(1);
        assertThat(repository.findByBoardId(boardB)).hasSize(1);
    }
}
