import pytest
from unittest.mock import MagicMock
from user_management_policy.user import User
from user_management_policy.user_id import UserId
from user_management_policy.user_repository import UserRepository
from user_management_organizing_adapter.user_management_user_name_provider import UserManagementUserNameProvider


@pytest.fixture
def user_repository():
    return MagicMock(spec=UserRepository)


def test_get_display_name_returns_display_name(user_repository):
    user_repository.find_by_id.return_value = User(id=UserId("u1"), email="a@b.com", display_name="Alice")
    provider = UserManagementUserNameProvider(user_repository)

    assert provider.get_display_name("u1") == "Alice"


def test_get_display_name_raises_when_user_not_found(user_repository):
    user_repository.find_by_id.return_value = None
    provider = UserManagementUserNameProvider(user_repository)

    with pytest.raises(ValueError):
        provider.get_display_name("unknown")
