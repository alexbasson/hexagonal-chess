from dataclasses import dataclass
from organizing_policy.invitation_id import InvitationId
from organizing_policy.invitation_status import InvitationStatus


@dataclass(frozen=True)
class GameInvitation:
    id: InvitationId
    inviting_user_id: str
    invited_user_id: str
    status: InvitationStatus
