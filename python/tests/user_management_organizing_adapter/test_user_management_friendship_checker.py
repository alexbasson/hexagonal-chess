from unittest.mock import MagicMock
from user_management_policy.friendship import Friendship
from user_management_policy.friendship_repository import FriendshipRepository
from user_management_policy.user_id import UserId
from user_management_organizing_adapter.user_management_friendship_checker import UserManagementFriendshipChecker


def test_are_friends_returns_true_when_friendship_exists():
    friendship_repository = MagicMock(spec=FriendshipRepository)
    friendship_repository.find_by_owner_id.return_value = [
        Friendship(owner_id=UserId("u1"), friend_id=UserId("u2"))
    ]
    checker = UserManagementFriendshipChecker(friendship_repository)

    assert checker.are_friends("u1", "u2") is True


def test_are_friends_returns_false_when_no_friendship():
    friendship_repository = MagicMock(spec=FriendshipRepository)
    friendship_repository.find_by_owner_id.return_value = []
    checker = UserManagementFriendshipChecker(friendship_repository)

    assert checker.are_friends("u1", "u2") is False
