import uuid
from user_management_policy.user import User
from user_management_policy.user_id import UserId
from user_management_policy.user_repository import UserRepository


def make_create_user(user_repository: UserRepository):
    def create_user(email: str, display_name: str) -> UserId:
        user_id = UserId(value=str(uuid.uuid4()))
        user_repository.save(User(id=user_id, email=email, display_name=display_name))
        return user_id

    return create_user
