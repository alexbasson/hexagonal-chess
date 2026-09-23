from unittest.mock import MagicMock
from user_management_policy.friendship_repository import FriendshipRepository
from user_management_policy.user_id import UserId
from user_management_policy.remove_friend import make_remove_friend


def test_remove_friend_deletes_friendship():
    friendship_repository = MagicMock(spec=FriendshipRepository)
    remove_friend = make_remove_friend(friendship_repository)
    owner_id = UserId("u1")
    friend_id = UserId("u2")

    remove_friend(owner_id, friend_id)

    friendship_repository.delete_by_owner_and_friend.assert_called_once_with(owner_id, friend_id)
