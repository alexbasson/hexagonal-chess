from dataclasses import dataclass
from user_management_policy.user_id import UserId


@dataclass(frozen=True)
class User:
    id: UserId
    email: str
    display_name: str
