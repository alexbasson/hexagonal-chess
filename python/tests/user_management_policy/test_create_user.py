from unittest.mock import MagicMock
from user_management_policy.user_repository import UserRepository
from user_management_policy.create_user import make_create_user


def test_create_user_saves_user_and_returns_id():
    user_repository = MagicMock(spec=UserRepository)
    create_user = make_create_user(user_repository)

    user_id = create_user("alice@example.com", "Alice")

    user_repository.save.assert_called_once()
    saved = user_repository.save.call_args[0][0]
    assert saved.id == user_id
    assert saved.email == "alice@example.com"
    assert saved.display_name == "Alice"
