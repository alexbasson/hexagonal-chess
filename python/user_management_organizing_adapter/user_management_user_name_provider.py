from organizing_policy.user_name_provider import UserNameProvider
from user_management_policy.user_id import UserId
from user_management_policy.user_repository import UserRepository


class UserManagementUserNameProvider(UserNameProvider):
    def __init__(self, user_repository: UserRepository):
        self._user_repository = user_repository

    def get_display_name(self, user_id: str) -> str:
        user = self._user_repository.find_by_id(UserId(user_id))
        if user is None:
            raise ValueError(f"User not found: {user_id}")
        return user.display_name
