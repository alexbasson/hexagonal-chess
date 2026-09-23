import pytest
from unittest.mock import MagicMock
from fastapi import FastAPI
from fastapi.testclient import TestClient
from organizing_policy.game_id import GameId
from organizing_policy.player import Player
from organizing_policy.game import Game
from organizing_api_adapter.games_router import create_games_router

GAME_ID = GameId(value="game-1")
GAME = Game(id=GAME_ID, white=Player(name="Alice"), black=Player(name="Bob"))


@pytest.fixture
def start_game():
    return MagicMock(return_value=GAME_ID)


@pytest.fixture
def get_game():
    mock = MagicMock(return_value=GAME)
    return mock


@pytest.fixture
def list_games():
    return MagicMock(return_value=[GAME])


@pytest.fixture
def client(start_game, get_game, list_games):
    app = FastAPI()
    app.include_router(create_games_router(start_game, get_game, list_games))
    return TestClient(app)


def test_post_games_returns_game_id(client):
    response = client.post("/games", json={"whiteName": "Alice", "blackName": "Bob"})

    assert response.status_code == 200
    assert response.json()["gameId"] == "game-1"


def test_post_games_calls_start_game(client, start_game):
    client.post("/games", json={"whiteName": "Alice", "blackName": "Bob"})

    start_game.assert_called_once_with("Alice", "Bob")


def test_get_game_returns_game(client):
    response = client.get(f"/games/{GAME_ID.value}")

    assert response.status_code == 200
    assert response.json()["id"] == "game-1"
    assert response.json()["white"]["name"] == "Alice"
    assert response.json()["black"]["name"] == "Bob"


def test_get_game_returns_404_when_not_found(client, get_game):
    get_game.side_effect = ValueError("game not found")

    response = client.get("/games/unknown")

    assert response.status_code == 404


def test_list_games_returns_all_games(client):
    response = client.get("/games")

    assert response.status_code == 200
    assert len(response.json()) == 1
    assert response.json()[0]["id"] == "game-1"
