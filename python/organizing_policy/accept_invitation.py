from dataclasses import replace
from organizing_policy.game_id import GameId
from organizing_policy.invitation_id import InvitationId
from organizing_policy.invitation_repository import InvitationRepository
from organizing_policy.invitation_status import InvitationStatus
from organizing_policy.user_name_provider import UserNameProvider


def make_accept_invitation(
    invitation_repository: InvitationRepository,
    user_name_provider: UserNameProvider,
    start_game,
):
    def accept_invitation(invitation_id: InvitationId) -> GameId:
        invitation = invitation_repository.find_by_id(invitation_id)
        if invitation is None:
            raise ValueError(f"Invitation not found: {invitation_id.value}")
        if invitation.status != InvitationStatus.PENDING:
            raise ValueError(f"Invitation {invitation_id.value} is not pending")
        white_name = user_name_provider.get_display_name(invitation.inviting_user_id)
        black_name = user_name_provider.get_display_name(invitation.invited_user_id)
        game_id = start_game(white_name, black_name)
        invitation_repository.save(replace(invitation, status=InvitationStatus.ACCEPTED))
        return game_id
    return accept_invitation
