from dataclasses import dataclass


@dataclass(frozen=True)
class King:
    color: str


@dataclass(frozen=True)
class Queen:
    color: str


@dataclass(frozen=True)
class Rook:
    color: str


@dataclass(frozen=True)
class Bishop:
    color: str


@dataclass(frozen=True)
class Knight:
    color: str


@dataclass(frozen=True)
class Pawn:
    color: str


Piece = King | Queen | Rook | Bishop | Knight | Pawn
