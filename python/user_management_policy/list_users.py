from user_management_policy.user_repository import UserRepository


def make_list_users(user_repository: UserRepository):
    def list_users() -> list:
        return user_repository.find_all()
    return list_users
