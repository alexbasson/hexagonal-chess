from user_management_policy.friendship import Friendship
from user_management_policy.friendship_repository import FriendshipRepository
from user_management_policy.user_id import UserId


class InMemoryFriendshipRepository(FriendshipRepository):
    def __init__(self):
        self._store: list[Friendship] = []

    def save(self, friendship: Friendship) -> None:
        self._store.append(friendship)

    def find_by_owner_id(self, owner_id: UserId) -> list[Friendship]:
        return [f for f in self._store if f.owner_id == owner_id]

    def delete_by_owner_and_friend(self, owner_id: UserId, friend_id: UserId) -> None:
        self._store = [
            f for f in self._store
            if not (f.owner_id == owner_id and f.friend_id == friend_id)
        ]
