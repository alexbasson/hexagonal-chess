from organizing_policy.game_id import GameId
from organizing_policy.game_repository import GameRepository


class InMemoryGameRepository(GameRepository):
    def __init__(self):
        self._store: dict = {}

    def save(self, game) -> None:
        self._store[game.id] = game

    def find_by_id(self, game_id: GameId):
        return self._store.get(game_id)

    def find_all(self) -> list:
        return list(self._store.values())
