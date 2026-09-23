from abc import ABC, abstractmethod
from user_management_policy.user_id import UserId


class FriendshipRepository(ABC):
    @abstractmethod
    def save(self, friendship) -> None: ...

    @abstractmethod
    def find_by_owner_id(self, owner_id: UserId) -> list: ...

    @abstractmethod
    def delete_by_owner_and_friend(self, owner_id: UserId, friend_id: UserId) -> None: ...
