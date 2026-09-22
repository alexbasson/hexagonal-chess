from abc import ABC, abstractmethod
from gameplay_policy.board_id import BoardId


class BoardRepository(ABC):
    @abstractmethod
    def save(self, board) -> None: ...

    @abstractmethod
    def find_by_id(self, board_id: BoardId): ...
