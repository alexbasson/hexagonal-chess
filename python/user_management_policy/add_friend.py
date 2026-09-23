from user_management_policy.friendship import Friendship
from user_management_policy.friendship_repository import FriendshipRepository
from user_management_policy.user_id import UserId


def make_add_friend(friendship_repository: FriendshipRepository):
    def add_friend(owner_id: UserId, friend_id: UserId) -> None:
        friendship_repository.save(Friendship(owner_id=owner_id, friend_id=friend_id))
    return add_friend
