from organizing_policy.game_repository import GameRepository


def make_list_games(game_repository: GameRepository):
    def list_games() -> list:
        return game_repository.find_all()

    return list_games
