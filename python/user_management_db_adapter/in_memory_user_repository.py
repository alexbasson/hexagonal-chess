from user_management_policy.user import User
from user_management_policy.user_id import UserId
from user_management_policy.user_repository import UserRepository


class InMemoryUserRepository(UserRepository):
    def __init__(self):
        self._store: dict[str, User] = {}

    def save(self, user: User) -> None:
        self._store[user.id.value] = user

    def find_by_id(self, user_id: UserId) -> User | None:
        return self._store.get(user_id.value)

    def find_all(self) -> list[User]:
        return list(self._store.values())

    def delete(self, user_id: UserId) -> None:
        self._store.pop(user_id.value, None)
