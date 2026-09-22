from dataclasses import dataclass


@dataclass(frozen=True)
class BoardId:
    value: str
