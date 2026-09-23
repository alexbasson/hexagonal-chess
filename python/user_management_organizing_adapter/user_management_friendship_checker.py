from organizing_policy.friendship_checker import FriendshipChecker
from user_management_policy.friendship_repository import FriendshipRepository
from user_management_policy.user_id import UserId


class UserManagementFriendshipChecker(FriendshipChecker):
    def __init__(self, friendship_repository: FriendshipRepository):
        self._friendship_repository = friendship_repository

    def are_friends(self, owner_id: str, friend_id: str) -> bool:
        friendships = self._friendship_repository.find_by_owner_id(UserId(owner_id))
        return any(f.friend_id == UserId(friend_id) for f in friendships)
