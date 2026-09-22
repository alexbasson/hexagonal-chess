package com.hexagonalchess.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MakeMoveTest {

    @Mock private BoardRepository boardRepository;
    @Mock private MoveRepository moveRepository;

    private MakeMove makeMove;

    @BeforeEach
    void setup() {
        makeMove = new MakeMove(boardRepository, moveRepository);
    }

    private static final BoardId BOARD_ID = new BoardId("game1");

    @Test
    void throws_when_board_not_found() {
        when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> makeMove.execute(BOARD_ID, new Move(new Square(5, 2), new Square(5, 4))))
            .withMessageContaining("game1");
    }

    @Test
    void throws_when_no_piece_at_source_square() {
        var board = new Board(BOARD_ID, "Alice", "Bob", Color.WHITE, Map.of());
        when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(board));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> makeMove.execute(BOARD_ID, new Move(new Square(5, 2), new Square(5, 4))))
            .withMessageContaining("No piece");
    }

    @Test
    void persists_move_and_updated_board() {
        Map<Square, Piece> pieces = Map.of(new Square(5, 2), new Piece.Pawn(Color.WHITE));
        var board = new Board(BOARD_ID, "Alice", "Bob", Color.WHITE, pieces);
        when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(board));
        var move = new Move(new Square(5, 2), new Square(5, 3));

        var result = makeMove.execute(BOARD_ID, move);

        verify(boardRepository).save(result);
        verify(moveRepository).save(move, BOARD_ID);
    }

    @Test
    void pawn_advances_one_square_forward() {
        Map<Square, Piece> pieces = Map.of(new Square(5, 2), new Piece.Pawn(Color.WHITE));
        var board = new Board(BOARD_ID, "Alice", "Bob", Color.WHITE, pieces);
        when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(board));

        var result = makeMove.execute(BOARD_ID, new Move(new Square(5, 2), new Square(5, 3)));

        assertThat(result.pieces().get(new Square(5, 3))).isEqualTo(new Piece.Pawn(Color.WHITE));
        assertThat(result.pieces()).doesNotContainKey(new Square(5, 2));
        assertThat(result.activeColor()).isEqualTo(Color.BLACK);
    }

    @Test
    void throws_when_move_leaves_own_king_in_check() {
        var pieces = new HashMap<Square, Piece>();
        pieces.put(new Square(5, 1), new Piece.King(Color.WHITE));
        pieces.put(new Square(5, 4), new Piece.Rook(Color.WHITE));
        pieces.put(new Square(5, 8), new Piece.Rook(Color.BLACK));
        var board = new Board(BOARD_ID, "Alice", "Bob", Color.WHITE, Map.copyOf(pieces));
        when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(board));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> makeMove.execute(BOARD_ID, new Move(new Square(5, 4), new Square(4, 4))))
            .withMessageContaining("check");
    }

    @Test
    void throws_when_moving_opponents_piece() {
        Map<Square, Piece> pieces = Map.of(new Square(5, 7), new Piece.Pawn(Color.BLACK));
        var board = new Board(BOARD_ID, "Alice", "Bob", Color.WHITE, pieces);
        when(boardRepository.findById(BOARD_ID)).thenReturn(Optional.of(board));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> makeMove.execute(BOARD_ID, new Move(new Square(5, 7), new Square(5, 6))))
            .withMessageContaining("not your piece");
    }
}
