from unittest.mock import MagicMock
from user_management_policy.friendship import Friendship
from user_management_policy.friendship_repository import FriendshipRepository
from user_management_policy.user_id import UserId
from user_management_policy.list_friends import make_list_friends


def test_list_friends_returns_friendships_for_owner():
    friendship_repository = MagicMock(spec=FriendshipRepository)
    owner_id = UserId("u1")
    friendships = [Friendship(owner_id=owner_id, friend_id=UserId("u2"))]
    friendship_repository.find_by_owner_id.return_value = friendships
    list_friends = make_list_friends(friendship_repository)

    assert list_friends(owner_id) == friendships


def test_list_friends_returns_empty_when_no_friends():
    friendship_repository = MagicMock(spec=FriendshipRepository)
    friendship_repository.find_by_owner_id.return_value = []
    list_friends = make_list_friends(friendship_repository)

    assert list_friends(UserId("u1")) == []
