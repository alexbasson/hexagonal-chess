from abc import ABC, abstractmethod


class FriendshipChecker(ABC):
    @abstractmethod
    def are_friends(self, owner_id: str, friend_id: str) -> bool: ...
