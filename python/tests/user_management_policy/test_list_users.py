from unittest.mock import MagicMock
from user_management_policy.user import User
from user_management_policy.user_id import UserId
from user_management_policy.user_repository import UserRepository
from user_management_policy.list_users import make_list_users


def test_list_users_returns_all_users():
    user_repository = MagicMock(spec=UserRepository)
    users = [User(id=UserId("u1"), email="a@example.com", display_name="Alice")]
    user_repository.find_all.return_value = users
    list_users = make_list_users(user_repository)

    assert list_users() == users


def test_list_users_returns_empty_when_none():
    user_repository = MagicMock(spec=UserRepository)
    user_repository.find_all.return_value = []
    list_users = make_list_users(user_repository)

    assert list_users() == []
