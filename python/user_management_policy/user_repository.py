from abc import ABC, abstractmethod
from user_management_policy.user_id import UserId


class UserRepository(ABC):
    @abstractmethod
    def save(self, user) -> None: ...

    @abstractmethod
    def find_by_id(self, user_id: UserId): ...

    @abstractmethod
    def find_all(self) -> list: ...

    @abstractmethod
    def delete(self, user_id: UserId) -> None: ...
