from gameplay_policy.board_id import BoardId
from organizing_policy.game_id import GameId
from organizing_policy.game_initializer import GameInitializer
from organizing_policy.player import Player


class GameplayGameInitializer(GameInitializer):
    def __init__(self, setup_board):
        self._setup_board = setup_board

    def initialize_game(self, white: Player, black: Player) -> GameId:
        board_id = self._setup_board(white.name, black.name)
        return GameId(value=board_id.value)
