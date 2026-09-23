from unittest.mock import MagicMock
from user_management_policy.friendship import Friendship
from user_management_policy.friendship_repository import FriendshipRepository
from user_management_policy.user_id import UserId
from user_management_policy.add_friend import make_add_friend


def test_add_friend_saves_friendship():
    friendship_repository = MagicMock(spec=FriendshipRepository)
    add_friend = make_add_friend(friendship_repository)
    owner_id = UserId("u1")
    friend_id = UserId("u2")

    add_friend(owner_id, friend_id)

    friendship_repository.save.assert_called_once_with(Friendship(owner_id=owner_id, friend_id=friend_id))
