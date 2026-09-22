from organizing_policy.game_id import GameId
from organizing_policy.player import Player
from organizing_policy.game import Game
from organizing_db_adapter.in_memory_game_repository import InMemoryGameRepository

GAME_ID = GameId(value="g1")
GAME = Game(id=GAME_ID, white=Player(name="Alice"), black=Player(name="Bob"))


def test_game_repo_returns_none_when_not_found():
    assert InMemoryGameRepository().find_by_id(GAME_ID) is None


def test_game_repo_returns_saved_game():
    repo = InMemoryGameRepository()
    repo.save(GAME)
    assert repo.find_by_id(GAME_ID) == GAME


def test_game_repo_find_all_returns_empty():
    assert InMemoryGameRepository().find_all() == []


def test_game_repo_find_all_returns_saved_games():
    repo = InMemoryGameRepository()
    repo.save(GAME)
    assert repo.find_all() == [GAME]
