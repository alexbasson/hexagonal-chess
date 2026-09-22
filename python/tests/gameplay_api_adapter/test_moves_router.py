import pytest
from unittest.mock import MagicMock
from fastapi import FastAPI
from fastapi.testclient import TestClient
from gameplay_policy.board_id import BoardId
from gameplay_policy.board import Board
from gameplay_policy.make_move import make_make_move
from gameplay_policy.board_repository import BoardRepository
from gameplay_policy.move_repository import MoveRepository
from gameplay_api_adapter.moves_router import create_moves_router

BOARD_ID = "game-1"
UPDATED_BOARD = Board(
    id=BoardId(value=BOARD_ID),
    white_player_name="Alice",
    black_player_name="Bob",
    active_color="black",
    pieces={},
)


@pytest.fixture
def make_move():
    return MagicMock(return_value=UPDATED_BOARD)


@pytest.fixture
def client(make_move):
    app = FastAPI()
    app.include_router(create_moves_router(make_move))
    return TestClient(app)


def test_post_move_returns_200_with_updated_board(client):
    response = client.post(f"/games/{BOARD_ID}/moves",
                           json={"from_square": "e2", "to_square": "e3"})

    assert response.status_code == 200
    assert response.json()["active_color"] == "black"


def test_post_move_calls_make_move_with_correct_args(client, make_move):
    from gameplay_policy.square import Square
    from gameplay_policy.move import Move

    client.post(f"/games/{BOARD_ID}/moves",
                json={"from_square": "e2", "to_square": "e3"})

    make_move.assert_called_once_with(
        BoardId(value=BOARD_ID),
        Move(from_square=Square(file=5, rank=2), to_square=Square(file=5, rank=3)),
    )


def test_post_move_returns_422_when_illegal(client, make_move):
    make_move.side_effect = ValueError("illegal move")

    response = client.post(f"/games/{BOARD_ID}/moves",
                           json={"from_square": "e2", "to_square": "e5"})

    assert response.status_code == 422
