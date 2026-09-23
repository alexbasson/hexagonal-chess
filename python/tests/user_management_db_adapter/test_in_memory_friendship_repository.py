import pytest
from user_management_policy.friendship import Friendship
from user_management_policy.user_id import UserId
from user_management_db_adapter.in_memory_friendship_repository import InMemoryFriendshipRepository


@pytest.fixture
def repo():
    return InMemoryFriendshipRepository()


OWNER = UserId("u1")
FRIEND = UserId("u2")
FRIENDSHIP = Friendship(owner_id=OWNER, friend_id=FRIEND)


def test_find_by_owner_id_returns_empty_when_none(repo):
    assert repo.find_by_owner_id(OWNER) == []


def test_save_and_find_by_owner_id(repo):
    repo.save(FRIENDSHIP)
    assert repo.find_by_owner_id(OWNER) == [FRIENDSHIP]


def test_find_by_owner_id_only_returns_own_friendships(repo):
    other_friendship = Friendship(owner_id=UserId("u3"), friend_id=FRIEND)
    repo.save(FRIENDSHIP)
    repo.save(other_friendship)
    assert repo.find_by_owner_id(OWNER) == [FRIENDSHIP]


def test_delete_by_owner_and_friend_removes_friendship(repo):
    repo.save(FRIENDSHIP)
    repo.delete_by_owner_and_friend(OWNER, FRIEND)
    assert repo.find_by_owner_id(OWNER) == []


def test_delete_by_owner_and_friend_does_nothing_when_not_found(repo):
    repo.delete_by_owner_and_friend(OWNER, FRIEND)
    assert repo.find_by_owner_id(OWNER) == []


def test_save_does_not_duplicate_existing_friendship(repo):
    repo.save(FRIENDSHIP)
    repo.save(FRIENDSHIP)
    assert repo.find_by_owner_id(OWNER) == [FRIENDSHIP]
