import uuid
from organizing_policy.friendship_checker import FriendshipChecker
from organizing_policy.game_invitation import GameInvitation
from organizing_policy.invitation_id import InvitationId
from organizing_policy.invitation_repository import InvitationRepository
from organizing_policy.invitation_status import InvitationStatus


def make_create_invitation(friendship_checker: FriendshipChecker, invitation_repository: InvitationRepository):
    def create_invitation(inviting_user_id: str, invited_user_id: str) -> InvitationId:
        if not friendship_checker.are_friends(inviting_user_id, invited_user_id):
            raise ValueError(f"{inviting_user_id} and {invited_user_id} are not friends")
        invitation_id = InvitationId(str(uuid.uuid4()))
        invitation_repository.save(GameInvitation(
            id=invitation_id,
            inviting_user_id=inviting_user_id,
            invited_user_id=invited_user_id,
            status=InvitationStatus.PENDING,
        ))
        return invitation_id
    return create_invitation
