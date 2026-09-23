import pytest
from unittest.mock import MagicMock
from fastapi import FastAPI
from fastapi.testclient import TestClient
from gameplay_policy.board_id import BoardId
from gameplay_policy.board import Board
from gameplay_policy.square import Square
from gameplay_policy.piece import King
from gameplay_policy.board_repository import BoardRepository
from gameplay_api_adapter.board_router import create_board_router

BOARD_ID = "game-1"
BOARD = Board(
    id=BoardId(value=BOARD_ID),
    white_player_name="Alice",
    black_player_name="Bob",
    active_color="white",
    pieces={Square(file=5, rank=1): King(color="white")},
)


@pytest.fixture
def board_repository():
    return MagicMock(spec=BoardRepository)


@pytest.fixture
def client(board_repository):
    app = FastAPI()
    app.include_router(create_board_router(board_repository))
    return TestClient(app)


def test_get_board_returns_200_with_board(client, board_repository):
    board_repository.find_by_id.return_value = BOARD

    response = client.get(f"/games/{BOARD_ID}/board")

    assert response.status_code == 200
    assert response.json()["id"] == BOARD_ID
    assert response.json()["whitePlayerName"] == "Alice"
    assert response.json()["blackPlayerName"] == "Bob"
    assert response.json()["activeColor"] == "white"
    assert len(response.json()["pieces"]) == 1


def test_get_board_returns_404_when_not_found(client, board_repository):
    board_repository.find_by_id.return_value = None

    response = client.get("/games/unknown/board")

    assert response.status_code == 404
