from abc import ABC, abstractmethod
from gameplay_policy.board_id import BoardId
from gameplay_policy.move import Move


class MoveRepository(ABC):
    @abstractmethod
    def save(self, move: Move, board_id: BoardId) -> None: ...

    @abstractmethod
    def find_by_board_id(self, board_id: BoardId) -> list[Move]: ...
