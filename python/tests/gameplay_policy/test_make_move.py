import pytest
from unittest.mock import MagicMock
from gameplay_policy.board_id import BoardId
from gameplay_policy.board import Board
from gameplay_policy.square import Square
from gameplay_policy.piece import King, Rook, Pawn
from gameplay_policy.move import Move
from gameplay_policy.board_repository import BoardRepository
from gameplay_policy.move_repository import MoveRepository
from gameplay_policy.make_move import make_make_move

BOARD_ID = BoardId(value="board-1")
WHITE_PAWN_SQ = Square(file=5, rank=2)
TARGET_SQ = Square(file=5, rank=3)
MOVE = Move(from_square=WHITE_PAWN_SQ, to_square=TARGET_SQ)


def make_board(pieces, active_color="white"):
    return Board(
        id=BOARD_ID,
        white_player_name="Alice",
        black_player_name="Bob",
        active_color=active_color,
        pieces=pieces,
    )


@pytest.fixture
def board_repository():
    return MagicMock(spec=BoardRepository)


@pytest.fixture
def move_repository():
    return MagicMock(spec=MoveRepository)


@pytest.fixture
def make_move(board_repository, move_repository):
    return make_make_move(board_repository, move_repository)


def test_raises_when_board_not_found(make_move, board_repository):
    board_repository.find_by_id.return_value = None

    with pytest.raises(ValueError, match="board not found"):
        make_move(BOARD_ID, MOVE)


def test_raises_when_no_piece_at_source(make_move, board_repository):
    board_repository.find_by_id.return_value = make_board({})

    with pytest.raises(ValueError, match="no piece"):
        make_move(BOARD_ID, MOVE)


def test_raises_when_moving_opponents_piece(make_move, board_repository):
    board = make_board({WHITE_PAWN_SQ: Pawn(color="black")})
    board_repository.find_by_id.return_value = board

    with pytest.raises(ValueError, match="not your piece"):
        make_move(BOARD_ID, MOVE)


def test_persists_move_and_returns_updated_board(make_move, board_repository, move_repository):
    board = make_board({WHITE_PAWN_SQ: Pawn(color="white")})
    board_repository.find_by_id.return_value = board

    result = make_move(BOARD_ID, MOVE)

    move_repository.save.assert_called_once_with(MOVE, BOARD_ID)
    board_repository.save.assert_called_once()
    assert result.pieces.get(TARGET_SQ) == Pawn(color="white")
    assert result.pieces.get(WHITE_PAWN_SQ) is None
    assert result.active_color == "black"


def test_pawn_advances_one_square_forward(make_move, board_repository):
    board = make_board({WHITE_PAWN_SQ: Pawn(color="white")})
    board_repository.find_by_id.return_value = board

    result = make_move(BOARD_ID, MOVE)

    assert result.pieces[TARGET_SQ] == Pawn(color="white")


def test_raises_when_move_leaves_king_in_check(make_move, board_repository):
    king_sq = Square(file=5, rank=1)
    enemy_rook_sq = Square(file=5, rank=8)
    pinned_sq = Square(file=5, rank=4)
    board = make_board({
        king_sq: King(color="white"),
        pinned_sq: Rook(color="white"),
        enemy_rook_sq: Rook(color="black"),
    })
    board_repository.find_by_id.return_value = board
    exposing_move = Move(from_square=pinned_sq, to_square=Square(file=6, rank=4))

    with pytest.raises(ValueError, match="leaves king in check"):
        make_move(BOARD_ID, exposing_move)
