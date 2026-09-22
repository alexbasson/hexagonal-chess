from abc import ABC, abstractmethod
from organizing_policy.player import Player
from organizing_policy.game_id import GameId


class GameInitializer(ABC):
    @abstractmethod
    def initialize_game(self, white: Player, black: Player) -> GameId: ...
