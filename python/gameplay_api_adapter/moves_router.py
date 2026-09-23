from fastapi import APIRouter
from pydantic import BaseModel, ConfigDict
from pydantic.alias_generators import to_camel
from gameplay_policy.board_id import BoardId
from gameplay_policy.square import Square
from gameplay_policy.move import Move


class MoveRequest(BaseModel):
    model_config = ConfigDict(alias_generator=to_camel, populate_by_name=True)

    from_square: str
    to_square: str

    def to_move(self) -> Move:
        return Move(
            from_square=_parse_square(self.from_square),
            to_square=_parse_square(self.to_square),
        )


def _parse_square(notation: str) -> Square:
    file = ord(notation[0]) - ord("a") + 1
    rank = int(notation[1])
    return Square(file=file, rank=rank)


def create_moves_router(make_move) -> APIRouter:
    router = APIRouter()

    @router.post("/games/{game_id}/moves")
    def post_move(game_id: str, body: MoveRequest):
        try:
            board = make_move(BoardId(value=game_id), body.to_move())
            return _board_response(board)
        except ValueError as e:
            from fastapi import HTTPException
            raise HTTPException(status_code=422, detail=str(e))

    return router


def _board_response(board):
    return {
        "id": board.id.value,
        "whitePlayerName": board.white_player_name,
        "blackPlayerName": board.black_player_name,
        "activeColor": board.active_color,
        "pieces": [
            {"square": {"file": sq.file, "rank": sq.rank},
             "piece": {"type": type(p).__name__, "color": p.color}}
            for sq, p in board.pieces.items()
        ],
    }
