from dataclasses import dataclass


@dataclass(frozen=True)
class GameId:
    value: str
