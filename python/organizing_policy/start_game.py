from organizing_policy.game import Game
from organizing_policy.game_id import GameId
from organizing_policy.player import Player
from organizing_policy.game_repository import GameRepository
from organizing_policy.game_initializer import GameInitializer


def make_start_game(game_repository: GameRepository, game_initializer: GameInitializer):
    def start_game(white_name: str, black_name: str) -> GameId:
        white = Player(name=white_name)
        black = Player(name=black_name)
        game_id = game_initializer.initialize_game(white, black)
        game_repository.save(Game(id=game_id, white=white, black=black))
        return game_id

    return start_game
