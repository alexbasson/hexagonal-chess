from collections import defaultdict
from gameplay_policy.board_id import BoardId
from gameplay_policy.move import Move
from gameplay_policy.move_repository import MoveRepository


class InMemoryMoveRepository(MoveRepository):
    def __init__(self):
        self._store: dict[BoardId, list[Move]] = defaultdict(list)

    def save(self, move: Move, board_id: BoardId) -> None:
        self._store[board_id].append(move)

    def find_by_board_id(self, board_id: BoardId) -> list[Move]:
        return list(self._store[board_id])
