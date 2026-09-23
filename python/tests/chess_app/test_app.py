from fastapi.testclient import TestClient
from chess_app.app import create_app


def test_full_game_flow():
    client = TestClient(create_app())

    start_response = client.post("/games", json={"whiteName": "Alice", "blackName": "Bob"})
    assert start_response.status_code == 200
    game_id = start_response.json()["gameId"]

    board_response = client.get(f"/games/{game_id}/board")
    assert board_response.status_code == 200
    assert len(board_response.json()["pieces"]) == 32

    move_response = client.post(
        f"/games/{game_id}/moves",
        json={"fromSquare": "e2", "toSquare": "e4"},
    )
    assert move_response.status_code == 200
    assert move_response.json()["activeColor"] == "black"

    game_response = client.get(f"/games/{game_id}")
    assert game_response.status_code == 200
    assert game_response.json()["white"]["name"] == "Alice"

    list_response = client.get("/games")
    assert list_response.status_code == 200
    assert len(list_response.json()) == 1
