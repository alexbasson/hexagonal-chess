package com.hexagonalchess.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SetupBoardTest {

    @Mock private BoardRepository boardRepository;

    private SetupBoard setupBoard;

    @BeforeEach
    void setup() {
        setupBoard = new SetupBoard(boardRepository);
    }

    @Test
    void returns_a_board_id() {
        var boardId = setupBoard.execute("Alice", "Bob");
        assertThat(boardId).isNotNull();
    }

    @Test
    void persists_the_board_with_the_returned_id() {
        var boardId = setupBoard.execute("Alice", "Bob");
        verify(boardRepository).save(new Board(boardId));
    }
}
