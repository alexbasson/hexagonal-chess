from gameplay_policy.board_id import BoardId
from gameplay_policy.board import Board
from gameplay_policy.square import Square
from gameplay_policy.move import Move
from gameplay_db_adapter.in_memory_board_repository import InMemoryBoardRepository
from gameplay_db_adapter.in_memory_move_repository import InMemoryMoveRepository

BOARD_ID = BoardId(value="b1")
BOARD = Board(id=BOARD_ID, white_player_name="Alice", black_player_name="Bob",
              active_color="white", pieces={})
MOVE = Move(from_square=Square(file=5, rank=2), to_square=Square(file=5, rank=3))


def test_board_repo_returns_none_when_not_found():
    assert InMemoryBoardRepository().find_by_id(BOARD_ID) is None


def test_board_repo_returns_saved_board():
    repo = InMemoryBoardRepository()
    repo.save(BOARD)
    assert repo.find_by_id(BOARD_ID) == BOARD


def test_move_repo_returns_empty_list_when_not_found():
    assert InMemoryMoveRepository().find_by_board_id(BOARD_ID) == []


def test_move_repo_returns_saved_moves():
    repo = InMemoryMoveRepository()
    repo.save(MOVE, BOARD_ID)
    assert repo.find_by_board_id(BOARD_ID) == [MOVE]
