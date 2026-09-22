from unittest.mock import MagicMock
from gameplay_policy.board_id import BoardId
from gameplay_policy.board_repository import BoardRepository
from gameplay_policy.setup_board import make_setup_board


from gameplay_policy.square import Square
from gameplay_policy.piece import King, Queen, Pawn


def test_returns_a_board_id():
    board_repository = MagicMock(spec=BoardRepository)
    setup_board = make_setup_board(board_repository)

    result = setup_board("Alice", "Bob")

    assert isinstance(result, BoardId)


def test_persists_board_with_player_names():
    board_repository = MagicMock(spec=BoardRepository)
    setup_board = make_setup_board(board_repository)

    setup_board("Alice", "Bob")

    saved_board = board_repository.save.call_args[0][0]
    assert saved_board.white_player_name == "Alice"
    assert saved_board.black_player_name == "Bob"


def test_initial_position_places_all_32_pieces():
    board_repository = MagicMock(spec=BoardRepository)
    setup_board = make_setup_board(board_repository)

    setup_board("Alice", "Bob")

    saved_board = board_repository.save.call_args[0][0]
    assert len(saved_board.pieces) == 32
    assert saved_board.pieces[Square(file=5, rank=1)] == King(color="white")
    assert saved_board.pieces[Square(file=4, rank=1)] == Queen(color="white")
    assert saved_board.pieces[Square(file=5, rank=8)] == King(color="black")
    assert saved_board.pieces[Square(file=5, rank=2)] == Pawn(color="white")
    assert saved_board.pieces[Square(file=5, rank=7)] == Pawn(color="black")
