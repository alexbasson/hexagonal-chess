import pytest
from unittest.mock import MagicMock
from organizing_policy.game_id import GameId
from organizing_policy.player import Player
from organizing_policy.game import Game
from organizing_policy.game_repository import GameRepository
from organizing_policy.list_games import make_list_games

GAME_1 = Game(id=GameId(value="g1"), white=Player(name="Alice"), black=Player(name="Bob"))
GAME_2 = Game(id=GameId(value="g2"), white=Player(name="Carol"), black=Player(name="Dave"))


@pytest.fixture
def game_repository():
    return MagicMock(spec=GameRepository)


@pytest.fixture
def list_games(game_repository):
    return make_list_games(game_repository)


def test_list_games_returns_empty_list(list_games, game_repository):
    game_repository.find_all.return_value = []

    assert list_games() == []


def test_list_games_returns_all_games(list_games, game_repository):
    game_repository.find_all.return_value = [GAME_1, GAME_2]

    assert list_games() == [GAME_1, GAME_2]
