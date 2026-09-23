import pytest
from unittest.mock import MagicMock
from fastapi import FastAPI
from fastapi.testclient import TestClient
from user_management_policy.user import User
from user_management_policy.user_id import UserId
from user_management_api_adapter.users_router import create_users_router

USER_ID = UserId("u1")
USER = User(id=USER_ID, email="alice@example.com", display_name="Alice")


@pytest.fixture
def create_user():
    return MagicMock(return_value=USER_ID)


@pytest.fixture
def get_user():
    return MagicMock(return_value=USER)


@pytest.fixture
def update_user():
    return MagicMock()


@pytest.fixture
def delete_user():
    return MagicMock()


@pytest.fixture
def list_users():
    return MagicMock(return_value=[USER])


@pytest.fixture
def client(create_user, get_user, update_user, delete_user, list_users):
    app = FastAPI()
    app.include_router(create_users_router(create_user, get_user, update_user, delete_user, list_users))
    return TestClient(app)


def test_post_admin_users_returns_user_id(client):
    response = client.post("/admin/users", json={"email": "alice@example.com", "displayName": "Alice"})

    assert response.status_code == 200
    assert response.json()["userId"] == "u1"


def test_post_admin_users_calls_create_user(client, create_user):
    client.post("/admin/users", json={"email": "alice@example.com", "displayName": "Alice"})

    create_user.assert_called_once_with("alice@example.com", "Alice")


def test_get_admin_user_returns_user(client):
    response = client.get("/admin/users/u1")

    assert response.status_code == 200
    assert response.json()["id"] == "u1"
    assert response.json()["email"] == "alice@example.com"
    assert response.json()["displayName"] == "Alice"


def test_get_admin_user_returns_404_when_not_found(client, get_user):
    get_user.side_effect = ValueError("not found")

    response = client.get("/admin/users/unknown")

    assert response.status_code == 404


def test_put_admin_user_calls_update_user(client, update_user):
    response = client.put("/admin/users/u1", json={"email": "new@example.com", "displayName": "Alice"})

    assert response.status_code == 200
    update_user.assert_called_once_with(USER_ID, "new@example.com", "Alice")


def test_put_admin_user_returns_404_when_not_found(client, update_user):
    update_user.side_effect = ValueError("not found")

    response = client.put("/admin/users/unknown", json={"email": "x@x.com", "displayName": "X"})

    assert response.status_code == 404


def test_delete_admin_user_calls_delete_user(client, delete_user):
    response = client.delete("/admin/users/u1")

    assert response.status_code == 200
    delete_user.assert_called_once_with(USER_ID)


def test_get_admin_users_returns_all(client):
    response = client.get("/admin/users")

    assert response.status_code == 200
    assert len(response.json()) == 1
    assert response.json()[0]["id"] == "u1"
