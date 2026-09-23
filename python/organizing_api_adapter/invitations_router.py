from typing import Optional
from fastapi import APIRouter, HTTPException
from pydantic import BaseModel, ConfigDict
from pydantic.alias_generators import to_camel
from organizing_policy.invitation_id import InvitationId
from organizing_policy.invitation_status import InvitationStatus


class CreateInvitationRequest(BaseModel):
    model_config = ConfigDict(alias_generator=to_camel, populate_by_name=True)

    inviting_user_id: str
    invited_user_id: str


def create_invitations_router(create_invitation, accept_invitation, decline_invitation, list_invitations) -> APIRouter:
    router = APIRouter()

    @router.post("/invitations")
    def post_invitation(body: CreateInvitationRequest):
        try:
            invitation_id = create_invitation(body.inviting_user_id, body.invited_user_id)
            return {"invitationId": invitation_id.value}
        except ValueError as e:
            raise HTTPException(status_code=422, detail=str(e))

    @router.post("/invitations/{invitation_id}/accept")
    def post_accept(invitation_id: str):
        try:
            game_id = accept_invitation(InvitationId(invitation_id))
            return {"gameId": game_id.value}
        except ValueError as e:
            raise HTTPException(status_code=422, detail=str(e))

    @router.post("/invitations/{invitation_id}/decline")
    def post_decline(invitation_id: str):
        try:
            decline_invitation(InvitationId(invitation_id))
            return {}
        except ValueError as e:
            raise HTTPException(status_code=422, detail=str(e))

    @router.get("/invitations")
    def get_invitations(direction: str, user_id: str = "", status: Optional[str] = None):
        status_filter = InvitationStatus(status) if status else None
        invitations = list_invitations(user_id, direction=direction, status=status_filter)
        return [_invitation_response(i) for i in invitations]

    return router


def _invitation_response(invitation):
    return {
        "id": invitation.id.value,
        "invitingUserId": invitation.inviting_user_id,
        "invitedUserId": invitation.invited_user_id,
        "status": invitation.status.value,
    }
