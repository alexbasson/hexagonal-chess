from user_management_policy.user import User
from user_management_policy.user_id import UserId
from user_management_policy.user_repository import UserRepository


def make_update_user(user_repository: UserRepository):
    def update_user(user_id: UserId, email: str, display_name: str) -> None:
        existing = user_repository.find_by_id(user_id)
        if existing is None:
            raise ValueError(f"User not found: {user_id.value}")
        user_repository.save(User(id=user_id, email=email, display_name=display_name))
    return update_user
