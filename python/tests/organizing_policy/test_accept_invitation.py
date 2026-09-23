import pytest
from unittest.mock import MagicMock
from organizing_policy.game_invitation import GameInvitation
from organizing_policy.invitation_id import InvitationId
from organizing_policy.invitation_repository import InvitationRepository
from organizing_policy.invitation_status import InvitationStatus
from organizing_policy.user_name_provider import UserNameProvider
from organizing_policy.game_id import GameId
from organizing_policy.accept_invitation import make_accept_invitation

INVITATION_ID = InvitationId("inv-1")
PENDING_INVITATION = GameInvitation(
    id=INVITATION_ID,
    inviting_user_id="u1",
    invited_user_id="u2",
    status=InvitationStatus.PENDING,
)


@pytest.fixture
def invitation_repository():
    mock = MagicMock(spec=InvitationRepository)
    mock.find_by_id.return_value = PENDING_INVITATION
    return mock


@pytest.fixture
def user_name_provider():
    mock = MagicMock(spec=UserNameProvider)
    mock.get_display_name.side_effect = lambda uid: "Alice" if uid == "u1" else "Bob"
    return mock


@pytest.fixture
def start_game():
    return MagicMock(return_value=GameId("game-1"))


def test_accept_invitation_starts_game(invitation_repository, user_name_provider, start_game):
    accept_invitation = make_accept_invitation(invitation_repository, user_name_provider, start_game)

    accept_invitation(INVITATION_ID)

    start_game.assert_called_once_with("Alice", "Bob")


def test_accept_invitation_returns_game_id(invitation_repository, user_name_provider, start_game):
    accept_invitation = make_accept_invitation(invitation_repository, user_name_provider, start_game)

    game_id = accept_invitation(INVITATION_ID)

    assert game_id.value == "game-1"


def test_accept_invitation_marks_accepted(invitation_repository, user_name_provider, start_game):
    accept_invitation = make_accept_invitation(invitation_repository, user_name_provider, start_game)

    accept_invitation(INVITATION_ID)

    saved = invitation_repository.save.call_args[0][0]
    assert saved.status == InvitationStatus.ACCEPTED


def test_accept_invitation_raises_when_not_pending(invitation_repository, user_name_provider, start_game):
    invitation_repository.find_by_id.return_value = GameInvitation(
        id=INVITATION_ID,
        inviting_user_id="u1",
        invited_user_id="u2",
        status=InvitationStatus.DECLINED,
    )
    accept_invitation = make_accept_invitation(invitation_repository, user_name_provider, start_game)

    with pytest.raises(ValueError):
        accept_invitation(INVITATION_ID)


def test_accept_invitation_raises_when_not_found(invitation_repository, user_name_provider, start_game):
    invitation_repository.find_by_id.return_value = None
    accept_invitation = make_accept_invitation(invitation_repository, user_name_provider, start_game)

    with pytest.raises(ValueError):
        accept_invitation(INVITATION_ID)
