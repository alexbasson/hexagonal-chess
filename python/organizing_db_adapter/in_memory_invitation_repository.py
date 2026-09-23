from organizing_policy.game_invitation import GameInvitation
from organizing_policy.invitation_id import InvitationId
from organizing_policy.invitation_repository import InvitationRepository


class InMemoryInvitationRepository(InvitationRepository):
    def __init__(self):
        self._store: dict[str, GameInvitation] = {}

    def save(self, invitation: GameInvitation) -> None:
        self._store[invitation.id.value] = invitation

    def find_by_id(self, invitation_id: InvitationId) -> GameInvitation | None:
        return self._store.get(invitation_id.value)

    def find_by_inviting_user_id(self, user_id: str) -> list[GameInvitation]:
        return [i for i in self._store.values() if i.inviting_user_id == user_id]

    def find_by_invited_user_id(self, user_id: str) -> list[GameInvitation]:
        return [i for i in self._store.values() if i.invited_user_id == user_id]
