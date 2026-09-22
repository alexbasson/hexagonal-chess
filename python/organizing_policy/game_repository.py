from abc import ABC, abstractmethod
from organizing_policy.game_id import GameId


class GameRepository(ABC):
    @abstractmethod
    def save(self, game) -> None: ...

    @abstractmethod
    def find_by_id(self, game_id: GameId): ...

    @abstractmethod
    def find_all(self) -> list: ...
