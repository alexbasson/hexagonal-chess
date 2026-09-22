from dataclasses import dataclass
from gameplay_policy.board_id import BoardId
from gameplay_policy.square import Square
from gameplay_policy.piece import Piece


@dataclass(frozen=True)
class Board:
    id: BoardId
    white_player_name: str
    black_player_name: str
    active_color: str
    pieces: dict[Square, Piece]
