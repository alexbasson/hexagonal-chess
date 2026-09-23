import pytest
from organizing_policy.game_invitation import GameInvitation
from organizing_policy.invitation_id import InvitationId
from organizing_policy.invitation_status import InvitationStatus
from organizing_db_adapter.in_memory_invitation_repository import InMemoryInvitationRepository


@pytest.fixture
def repo():
    return InMemoryInvitationRepository()


INV_ID = InvitationId("inv-1")
INVITATION = GameInvitation(id=INV_ID, inviting_user_id="u1", invited_user_id="u2", status=InvitationStatus.PENDING)


def test_find_by_id_returns_none_when_empty(repo):
    assert repo.find_by_id(INV_ID) is None


def test_save_and_find_by_id(repo):
    repo.save(INVITATION)
    assert repo.find_by_id(INV_ID) == INVITATION


def test_find_by_inviting_user_id(repo):
    repo.save(INVITATION)
    assert repo.find_by_inviting_user_id("u1") == [INVITATION]


def test_find_by_invited_user_id(repo):
    repo.save(INVITATION)
    assert repo.find_by_invited_user_id("u2") == [INVITATION]


def test_find_by_inviting_user_id_returns_empty_when_none(repo):
    assert repo.find_by_inviting_user_id("u1") == []


def test_save_overwrites_existing(repo):
    repo.save(INVITATION)
    accepted = GameInvitation(id=INV_ID, inviting_user_id="u1", invited_user_id="u2", status=InvitationStatus.ACCEPTED)
    repo.save(accepted)
    assert repo.find_by_id(INV_ID) == accepted
