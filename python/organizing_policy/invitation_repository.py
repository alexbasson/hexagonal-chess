from abc import ABC, abstractmethod
from organizing_policy.invitation_id import InvitationId


class InvitationRepository(ABC):
    @abstractmethod
    def save(self, invitation) -> None: ...

    @abstractmethod
    def find_by_id(self, invitation_id: InvitationId): ...

    @abstractmethod
    def find_by_inviting_user_id(self, user_id: str) -> list: ...

    @abstractmethod
    def find_by_invited_user_id(self, user_id: str) -> list: ...
