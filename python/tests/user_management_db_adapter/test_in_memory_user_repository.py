import pytest
from user_management_policy.user import User
from user_management_policy.user_id import UserId
from user_management_db_adapter.in_memory_user_repository import InMemoryUserRepository


@pytest.fixture
def repo():
    return InMemoryUserRepository()


USER_ID = UserId("u1")
USER = User(id=USER_ID, email="alice@example.com", display_name="Alice")


def test_find_by_id_returns_none_when_empty(repo):
    assert repo.find_by_id(USER_ID) is None


def test_save_and_find_by_id(repo):
    repo.save(USER)
    assert repo.find_by_id(USER_ID) == USER


def test_find_all_returns_all_saved_users(repo):
    user2 = User(id=UserId("u2"), email="bob@example.com", display_name="Bob")
    repo.save(USER)
    repo.save(user2)
    assert repo.find_all() == [USER, user2]


def test_delete_removes_user(repo):
    repo.save(USER)
    repo.delete(USER_ID)
    assert repo.find_by_id(USER_ID) is None


def test_save_overwrites_existing(repo):
    repo.save(USER)
    updated = User(id=USER_ID, email="new@example.com", display_name="Alice Updated")
    repo.save(updated)
    assert repo.find_by_id(USER_ID) == updated
