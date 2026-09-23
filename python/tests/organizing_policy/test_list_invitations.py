from unittest.mock import MagicMock
from organizing_policy.game_invitation import GameInvitation
from organizing_policy.invitation_id import InvitationId
from organizing_policy.invitation_repository import InvitationRepository
from organizing_policy.invitation_status import InvitationStatus
from organizing_policy.list_invitations import make_list_invitations

SENT = GameInvitation(id=InvitationId("i1"), inviting_user_id="u1", invited_user_id="u2", status=InvitationStatus.PENDING)
RECEIVED = GameInvitation(id=InvitationId("i2"), inviting_user_id="u3", invited_user_id="u1", status=InvitationStatus.PENDING)
ACCEPTED = GameInvitation(id=InvitationId("i3"), inviting_user_id="u1", invited_user_id="u4", status=InvitationStatus.ACCEPTED)


def make_repo(sent=None, received=None):
    mock = MagicMock(spec=InvitationRepository)
    mock.find_by_inviting_user_id.return_value = sent or []
    mock.find_by_invited_user_id.return_value = received or []
    return mock


def test_list_invitations_sent(  ):
    repo = make_repo(sent=[SENT])
    list_invitations = make_list_invitations(repo)

    result = list_invitations("u1", direction="sent")

    assert result == [SENT]


def test_list_invitations_received():
    repo = make_repo(received=[RECEIVED])
    list_invitations = make_list_invitations(repo)

    result = list_invitations("u1", direction="received")

    assert result == [RECEIVED]


def test_list_invitations_filters_by_status():
    repo = make_repo(sent=[SENT, ACCEPTED])
    list_invitations = make_list_invitations(repo)

    result = list_invitations("u1", direction="sent", status=InvitationStatus.PENDING)

    assert result == [SENT]


def test_list_invitations_returns_all_statuses_when_no_filter():
    repo = make_repo(sent=[SENT, ACCEPTED])
    list_invitations = make_list_invitations(repo)

    result = list_invitations("u1", direction="sent")

    assert result == [SENT, ACCEPTED]
