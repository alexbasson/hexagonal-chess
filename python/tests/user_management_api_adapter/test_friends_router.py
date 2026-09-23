import pytest
from unittest.mock import MagicMock
from fastapi import FastAPI
from fastapi.testclient import TestClient
from user_management_policy.friendship import Friendship
from user_management_policy.user_id import UserId
from user_management_api_adapter.friends_router import create_friends_router

OWNER_ID = UserId("u1")
FRIEND_ID = UserId("u2")
FRIENDSHIP = Friendship(owner_id=OWNER_ID, friend_id=FRIEND_ID)


@pytest.fixture
def add_friend():
    return MagicMock()


@pytest.fixture
def remove_friend():
    return MagicMock()


@pytest.fixture
def list_friends():
    return MagicMock(return_value=[FRIENDSHIP])


@pytest.fixture
def client(add_friend, remove_friend, list_friends):
    app = FastAPI()
    app.include_router(create_friends_router(add_friend, remove_friend, list_friends))
    return TestClient(app)


def test_post_friends_calls_add_friend(client, add_friend):
    response = client.post("/users/u1/friends", json={"friendId": "u2"})

    assert response.status_code == 200
    add_friend.assert_called_once_with(OWNER_ID, FRIEND_ID)


def test_delete_friend_calls_remove_friend(client, remove_friend):
    response = client.delete("/users/u1/friends/u2")

    assert response.status_code == 200
    remove_friend.assert_called_once_with(OWNER_ID, FRIEND_ID)


def test_get_friends_returns_friend_ids(client):
    response = client.get("/users/u1/friends")

    assert response.status_code == 200
    assert response.json() == [{"friendId": "u2"}]
