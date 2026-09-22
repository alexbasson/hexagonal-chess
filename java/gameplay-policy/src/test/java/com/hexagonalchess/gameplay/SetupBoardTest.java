package com.hexagonalchess.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.ArgumentCaptor;

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
    void each_board_gets_a_unique_id() {
        var first = setupBoard.execute("Alice", "Bob");
        var second = setupBoard.execute("Alice", "Bob");
        assertThat(first).isNotEqualTo(second);
    }

    private Board savedBoard() {
        var captor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(captor.capture());
        return captor.getValue();
    }

    @Test
    void persists_board_with_player_names() {
        var boardId = setupBoard.execute("Alice", "Bob");
        var saved = savedBoard();
        assertThat(saved.id()).isEqualTo(boardId);
        assertThat(saved.whitePlayerName()).isEqualTo("Alice");
        assertThat(saved.blackPlayerName()).isEqualTo("Bob");
    }

    @Test
    void active_color_starts_as_white() {
        setupBoard.execute("Alice", "Bob");
        assertThat(savedBoard().activeColor()).isEqualTo(Color.WHITE);
    }

    @Test
    void initial_position_places_pieces_correctly() {
        setupBoard.execute("Alice", "Bob");
        var pieces = savedBoard().pieces();
        assertThat(pieces.get(new Square(5, 1))).isEqualTo(new Piece.King(Color.WHITE));
        assertThat(pieces.get(new Square(5, 8))).isEqualTo(new Piece.King(Color.BLACK));
        assertThat(pieces.get(new Square(5, 2))).isEqualTo(new Piece.Pawn(Color.WHITE));
        assertThat(pieces.get(new Square(5, 7))).isEqualTo(new Piece.Pawn(Color.BLACK));
    }
}
