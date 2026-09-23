from dataclasses import replace
from organizing_policy.invitation_id import InvitationId
from organizing_policy.invitation_repository import InvitationRepository
from organizing_policy.invitation_status import InvitationStatus


def make_decline_invitation(invitation_repository: InvitationRepository):
    def decline_invitation(invitation_id: InvitationId) -> None:
        invitation = invitation_repository.find_by_id(invitation_id)
        if invitation is None:
            raise ValueError(f"Invitation not found: {invitation_id.value}")
        if invitation.status != InvitationStatus.PENDING:
            raise ValueError(f"Invitation {invitation_id.value} is not pending")
        invitation_repository.save(replace(invitation, status=InvitationStatus.DECLINED))
    return decline_invitation
