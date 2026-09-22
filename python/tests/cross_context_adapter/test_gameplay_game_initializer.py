import pytest
from unittest.mock import MagicMock
from organizing_policy.player import Player
from organizing_policy.game_id import GameId
from gameplay_policy.board_id import BoardId
from cross_context_adapter.gameplay_game_initializer import GameplayGameInitializer


@pytest.fixture
def setup_board():
    return MagicMock(return_value=BoardId(value="board-1"))


@pytest.fixture
def initializer(setup_board):
    return GameplayGameInitializer(setup_board)


def test_initialize_game_returns_game_id(initializer):
    result = initializer.initialize_game(Player(name="Alice"), Player(name="Bob"))

    assert result == GameId(value="board-1")


def test_initialize_game_calls_setup_board_with_player_names(initializer, setup_board):
    initializer.initialize_game(Player(name="Alice"), Player(name="Bob"))

    setup_board.assert_called_once_with("Alice", "Bob")
