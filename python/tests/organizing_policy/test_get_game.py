import pytest
from unittest.mock import MagicMock
from organizing_policy.game_id import GameId
from organizing_policy.player import Player
from organizing_policy.game import Game
from organizing_policy.game_repository import GameRepository
from organizing_policy.get_game import make_get_game

GAME_ID = GameId(value="game-1")
GAME = Game(id=GAME_ID, white=Player(name="Alice"), black=Player(name="Bob"))


@pytest.fixture
def game_repository():
    return MagicMock(spec=GameRepository)


@pytest.fixture
def get_game(game_repository):
    return make_get_game(game_repository)


def test_get_game_returns_game(get_game, game_repository):
    game_repository.find_by_id.return_value = GAME

    result = get_game(GAME_ID)

    assert result == GAME


def test_get_game_raises_when_not_found(get_game, game_repository):
    game_repository.find_by_id.return_value = None

    with pytest.raises(ValueError, match="game not found"):
        get_game(GameId(value="unknown"))
