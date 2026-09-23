import pytest
from unittest.mock import MagicMock
from user_management_policy.user import User
from user_management_policy.user_id import UserId
from user_management_policy.user_repository import UserRepository
from user_management_policy.update_user import make_update_user

USER_ID = UserId(value="u1")


def test_update_user_saves_updated_user():
    user_repository = MagicMock(spec=UserRepository)
    user_repository.find_by_id.return_value = User(id=USER_ID, email="old@example.com", display_name="Old")
    update_user = make_update_user(user_repository)

    update_user(USER_ID, "new@example.com", "New Name")

    user_repository.save.assert_called_once_with(
        User(id=USER_ID, email="new@example.com", display_name="New Name")
    )


def test_update_user_raises_when_not_found():
    user_repository = MagicMock(spec=UserRepository)
    user_repository.find_by_id.return_value = None
    update_user = make_update_user(user_repository)

    with pytest.raises(ValueError):
        update_user(USER_ID, "x@example.com", "X")
