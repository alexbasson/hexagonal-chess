from dataclasses import dataclass
from organizing_policy.game_id import GameId
from organizing_policy.player import Player


@dataclass(frozen=True)
class Game:
    id: GameId
    white: Player
    black: Player
