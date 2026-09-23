import pytest
from unittest.mock import MagicMock
from organizing_policy.game_invitation import GameInvitation
from organizing_policy.invitation_id import InvitationId
from organizing_policy.invitation_repository import InvitationRepository
from organizing_policy.invitation_status import InvitationStatus
from organizing_policy.decline_invitation import make_decline_invitation

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


def test_decline_invitation_marks_declined(invitation_repository):
    decline_invitation = make_decline_invitation(invitation_repository)

    decline_invitation(INVITATION_ID)

    saved = invitation_repository.save.call_args[0][0]
    assert saved.status == InvitationStatus.DECLINED


def test_decline_invitation_raises_when_not_pending(invitation_repository):
    invitation_repository.find_by_id.return_value = GameInvitation(
        id=INVITATION_ID,
        inviting_user_id="u1",
        invited_user_id="u2",
        status=InvitationStatus.ACCEPTED,
    )
    decline_invitation = make_decline_invitation(invitation_repository)

    with pytest.raises(ValueError):
        decline_invitation(INVITATION_ID)


def test_decline_invitation_raises_when_not_found(invitation_repository):
    invitation_repository.find_by_id.return_value = None
    decline_invitation = make_decline_invitation(invitation_repository)

    with pytest.raises(ValueError):
        decline_invitation(INVITATION_ID)
