from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from organizing_policy.game_id import GameId


class StartGameRequest(BaseModel):
    white_name: str
    black_name: str


def create_games_router(start_game, get_game, list_games) -> APIRouter:
    router = APIRouter()

    @router.post("/games")
    def post_game(body: StartGameRequest):
        game_id = start_game(body.white_name, body.black_name)
        return {"game_id": game_id.value}

    @router.get("/games/{game_id}")
    def get_game_by_id(game_id: str):
        try:
            game = get_game(GameId(value=game_id))
            return _game_response(game)
        except ValueError:
            raise HTTPException(status_code=404)

    @router.get("/games")
    def get_all_games():
        return [_game_response(g) for g in list_games()]

    return router


def _game_response(game):
    return {
        "id": game.id.value,
        "white": {"name": game.white.name},
        "black": {"name": game.black.name},
    }
