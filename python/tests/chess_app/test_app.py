from fastapi.testclient import TestClient
from chess_app.app import create_app


def test_full_game_flow():
    client = TestClient(create_app())

    start_response = client.post("/games", json={"white_name": "Alice", "black_name": "Bob"})
    assert start_response.status_code == 200
    game_id = start_response.json()["game_id"]

    board_response = client.get(f"/games/{game_id}/board")
    assert board_response.status_code == 200
    assert len(board_response.json()["pieces"]) == 32

    move_response = client.post(
        f"/games/{game_id}/moves",
        json={"from_square": "e2", "to_square": "e4"},
    )
    assert move_response.status_code == 200
    assert move_response.json()["active_color"] == "black"

    game_response = client.get(f"/games/{game_id}")
    assert game_response.status_code == 200
    assert game_response.json()["white"]["name"] == "Alice"

    list_response = client.get("/games")
    assert list_response.status_code == 200
    assert len(list_response.json()) == 1
