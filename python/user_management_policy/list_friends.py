from user_management_policy.friendship_repository import FriendshipRepository
from user_management_policy.user_id import UserId


def make_list_friends(friendship_repository: FriendshipRepository):
    def list_friends(owner_id: UserId) -> list:
        return friendship_repository.find_by_owner_id(owner_id)
    return list_friends
