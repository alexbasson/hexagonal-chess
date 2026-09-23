import pytest
from unittest.mock import MagicMock
from fastapi import FastAPI
from fastapi.testclient import TestClient
from organizing_policy.game_invitation import GameInvitation
from organizing_policy.game_id import GameId
from organizing_policy.invitation_id import InvitationId
from organizing_policy.invitation_status import InvitationStatus
from organizing_api_adapter.invitations_router import create_invitations_router

INVITATION_ID = InvitationId("inv-1")
INVITATION = GameInvitation(
    id=INVITATION_ID,
    inviting_user_id="u1",
    invited_user_id="u2",
    status=InvitationStatus.PENDING,
)


@pytest.fixture
def create_invitation():
    return MagicMock(return_value=INVITATION_ID)


@pytest.fixture
def accept_invitation():
    return MagicMock(return_value=GameId("game-1"))


@pytest.fixture
def decline_invitation():
    return MagicMock()


@pytest.fixture
def list_invitations():
    return MagicMock(return_value=[INVITATION])


@pytest.fixture
def client(create_invitation, accept_invitation, decline_invitation, list_invitations):
    app = FastAPI()
    app.include_router(create_invitations_router(create_invitation, accept_invitation, decline_invitation, list_invitations))
    return TestClient(app)


def test_post_invitations_returns_invitation_id(client):
    response = client.post("/invitations", json={"invitingUserId": "u1", "invitedUserId": "u2"})

    assert response.status_code == 200
    assert response.json()["invitationId"] == "inv-1"


def test_post_invitations_raises_422_when_not_friends(client, create_invitation):
    create_invitation.side_effect = ValueError("not friends")

    response = client.post("/invitations", json={"invitingUserId": "u1", "invitedUserId": "u2"})

    assert response.status_code == 422


def test_post_accept_returns_game_id(client):
    response = client.post("/invitations/inv-1/accept")

    assert response.status_code == 200
    assert response.json()["gameId"] == "game-1"


def test_post_accept_raises_422_when_not_pending(client, accept_invitation):
    accept_invitation.side_effect = ValueError("not pending")

    response = client.post("/invitations/inv-1/accept")

    assert response.status_code == 422


def test_post_decline_succeeds(client):
    response = client.post("/invitations/inv-1/decline")

    assert response.status_code == 200


def test_post_decline_raises_422_when_not_pending(client, decline_invitation):
    decline_invitation.side_effect = ValueError("not pending")

    response = client.post("/invitations/inv-1/decline")

    assert response.status_code == 422


def test_get_invitations_sent(client, list_invitations):
    response = client.get("/invitations?direction=sent")

    assert response.status_code == 200
    assert len(response.json()) == 1
    assert response.json()[0]["id"] == "inv-1"
    assert response.json()[0]["invitingUserId"] == "u1"
    assert response.json()[0]["invitedUserId"] == "u2"
    list_invitations.assert_called_once()
