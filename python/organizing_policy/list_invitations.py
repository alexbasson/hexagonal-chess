from organizing_policy.invitation_repository import InvitationRepository


def make_list_invitations(invitation_repository: InvitationRepository):
    def list_invitations(user_id: str, direction: str, status=None) -> list:
        if direction == "sent":
            invitations = invitation_repository.find_by_inviting_user_id(user_id)
        else:
            invitations = invitation_repository.find_by_invited_user_id(user_id)
        if status is not None:
            invitations = [i for i in invitations if i.status == status]
        return invitations
    return list_invitations
