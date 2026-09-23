import pytest
from unittest.mock import MagicMock
from organizing_policy.friendship_checker import FriendshipChecker
from organizing_policy.invitation_repository import InvitationRepository
from organizing_policy.invitation_status import InvitationStatus
from organizing_policy.create_invitation import make_create_invitation


@pytest.fixture
def friendship_checker():
    mock = MagicMock(spec=FriendshipChecker)
    mock.are_friends.return_value = True
    return mock


@pytest.fixture
def invitation_repository():
    return MagicMock(spec=InvitationRepository)


def test_create_invitation_saves_pending_invitation(friendship_checker, invitation_repository):
    create_invitation = make_create_invitation(friendship_checker, invitation_repository)

    create_invitation("u1", "u2")

    saved = invitation_repository.save.call_args[0][0]
    assert saved.inviting_user_id == "u1"
    assert saved.invited_user_id == "u2"
    assert saved.status == InvitationStatus.PENDING


def test_create_invitation_returns_invitation_id(friendship_checker, invitation_repository):
    create_invitation = make_create_invitation(friendship_checker, invitation_repository)

    invitation_id = create_invitation("u1", "u2")

    assert invitation_id is not None


def test_create_invitation_raises_when_not_friends(invitation_repository):
    friendship_checker = MagicMock(spec=FriendshipChecker)
    friendship_checker.are_friends.return_value = False
    create_invitation = make_create_invitation(friendship_checker, invitation_repository)

    with pytest.raises(ValueError):
        create_invitation("u1", "u2")
