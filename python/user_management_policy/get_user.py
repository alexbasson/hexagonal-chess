from user_management_policy.user import User
from user_management_policy.user_id import UserId
from user_management_policy.user_repository import UserRepository


def make_get_user(user_repository: UserRepository):
    def get_user(user_id: UserId) -> User:
        user = user_repository.find_by_id(user_id)
        if user is None:
            raise ValueError(f"User not found: {user_id.value}")
        return user
    return get_user
