from fastapi import APIRouter, HTTPException
from gameplay_policy.board_id import BoardId
from gameplay_policy.board_repository import BoardRepository


def create_board_router(board_repository: BoardRepository) -> APIRouter:
    router = APIRouter()

    @router.get("/games/{game_id}/board")
    def get_board(game_id: str):
        board = board_repository.find_by_id(BoardId(value=game_id))
        if board is None:
            raise HTTPException(status_code=404)
        return {
            "id": board.id.value,
            "white_player_name": board.white_player_name,
            "black_player_name": board.black_player_name,
            "active_color": board.active_color,
            "pieces": [
                {"square": {"file": sq.file, "rank": sq.rank},
                 "piece": {"type": type(p).__name__, "color": p.color}}
                for sq, p in board.pieces.items()
            ],
        }

    return router
