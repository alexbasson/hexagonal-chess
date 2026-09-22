from dataclasses import dataclass
from gameplay_policy.square import Square


@dataclass(frozen=True)
class Move:
    from_square: Square
    to_square: Square
