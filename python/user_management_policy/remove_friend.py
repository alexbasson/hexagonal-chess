from user_management_policy.friendship_repository import FriendshipRepository
from user_management_policy.user_id import UserId


def make_remove_friend(friendship_repository: FriendshipRepository):
    def remove_friend(owner_id: UserId, friend_id: UserId) -> None:
        friendship_repository.delete_by_owner_and_friend(owner_id, friend_id)
    return remove_friend
