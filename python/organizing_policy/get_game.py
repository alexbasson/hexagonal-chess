from organizing_policy.game_id import GameId
from organizing_policy.game_repository import GameRepository


def make_get_game(game_repository: GameRepository):
    def get_game(game_id: GameId):
        game = game_repository.find_by_id(game_id)
        if game is None:
            raise ValueError("game not found")
        return game

    return get_game
