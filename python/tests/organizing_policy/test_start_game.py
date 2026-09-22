import pytest
from unittest.mock import MagicMock
from organizing_policy.game_id import GameId
from organizing_policy.player import Player
from organizing_policy.game_repository import GameRepository
from organizing_policy.game_initializer import GameInitializer
from organizing_policy.start_game import make_start_game

GAME_ID = GameId(value="game-1")


@pytest.fixture
def game_repository():
    return MagicMock(spec=GameRepository)


@pytest.fixture
def game_initializer():
    mock = MagicMock(spec=GameInitializer)
    mock.initialize_game.return_value = GAME_ID
    return mock


@pytest.fixture
def start_game(game_repository, game_initializer):
    return make_start_game(game_repository, game_initializer)


def test_start_game_returns_game_id(start_game):
    result = start_game("Alice", "Bob")

    assert result == GAME_ID


def test_start_game_saves_game_with_players(start_game, game_repository):
    from organizing_policy.game import Game

    start_game("Alice", "Bob")

    game_repository.save.assert_called_once_with(
        Game(id=GAME_ID, white=Player(name="Alice"), black=Player(name="Bob"))
    )


def test_start_game_calls_initializer_with_players(start_game, game_initializer):
    start_game("Alice", "Bob")

    game_initializer.initialize_game.assert_called_once_with(
        Player(name="Alice"), Player(name="Bob")
    )
