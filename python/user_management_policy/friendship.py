from dataclasses import dataclass
from user_management_policy.user_id import UserId


@dataclass(frozen=True)
class Friendship:
    owner_id: UserId
    friend_id: UserId
