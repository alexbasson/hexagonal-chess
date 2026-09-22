from dataclasses import dataclass


@dataclass(frozen=True)
class Square:
    file: int
    rank: int
