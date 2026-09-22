from gameplay_policy.board import Board
from gameplay_policy.board_id import BoardId
from gameplay_policy.board_repository import BoardRepository


class InMemoryBoardRepository(BoardRepository):
    def __init__(self):
        self._store: dict[BoardId, Board] = {}

    def save(self, board: Board) -> None:
        self._store[board.id] = board

    def find_by_id(self, board_id: BoardId) -> Board | None:
        return self._store.get(board_id)
