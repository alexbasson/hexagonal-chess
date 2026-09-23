from unittest.mock import MagicMock
from user_management_policy.user_id import UserId
from user_management_policy.user_repository import UserRepository
from user_management_policy.delete_user import make_delete_user


def test_delete_user_calls_repository_delete():
    user_repository = MagicMock(spec=UserRepository)
    delete_user = make_delete_user(user_repository)
    user_id = UserId(value="u1")

    delete_user(user_id)

    user_repository.delete.assert_called_once_with(user_id)
