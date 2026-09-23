from user_management_policy.user_id import UserId
from user_management_policy.user_repository import UserRepository


def make_delete_user(user_repository: UserRepository):
    def delete_user(user_id: UserId) -> None:
        user_repository.delete(user_id)
    return delete_user
