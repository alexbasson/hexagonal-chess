import pytest
from unittest.mock import MagicMock
from user_management_policy.user import User
from user_management_policy.user_id import UserId
from user_management_policy.user_repository import UserRepository
from user_management_policy.get_user import make_get_user

USER_ID = UserId(value="u1")
USER = User(id=USER_ID, email="alice@example.com", display_name="Alice")


@pytest.fixture
def user_repository():
    return MagicMock(spec=UserRepository)


def test_get_user_returns_the_user(user_repository):
    user_repository.find_by_id.return_value = USER
    get_user = make_get_user(user_repository)

    assert get_user(USER_ID) == USER


def test_get_user_raises_when_not_found(user_repository):
    user_repository.find_by_id.return_value = None
    get_user = make_get_user(user_repository)

    with pytest.raises(ValueError):
        get_user(USER_ID)
