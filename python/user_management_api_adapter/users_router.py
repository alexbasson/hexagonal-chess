from fastapi import APIRouter, HTTPException
from pydantic import BaseModel, ConfigDict
from pydantic.alias_generators import to_camel
from user_management_policy.user_id import UserId


class UserRequest(BaseModel):
    model_config = ConfigDict(alias_generator=to_camel, populate_by_name=True)

    email: str
    display_name: str


def create_users_router(create_user, get_user, update_user, delete_user, list_users) -> APIRouter:
    router = APIRouter()

    @router.post("/admin/users")
    def post_user(body: UserRequest):
        user_id = create_user(body.email, body.display_name)
        return {"userId": user_id.value}

    @router.get("/admin/users")
    def get_all_users():
        return [_user_response(u) for u in list_users()]

    @router.get("/admin/users/{user_id}")
    def get_user_by_id(user_id: str):
        try:
            user = get_user(UserId(user_id))
            return _user_response(user)
        except ValueError:
            raise HTTPException(status_code=404)

    @router.put("/admin/users/{user_id}")
    def put_user(user_id: str, body: UserRequest):
        try:
            update_user(UserId(user_id), body.email, body.display_name)
            return {}
        except ValueError:
            raise HTTPException(status_code=404)

    @router.delete("/admin/users/{user_id}")
    def delete_user_by_id(user_id: str):
        delete_user(UserId(user_id))
        return {}

    return router


def _user_response(user):
    return {
        "id": user.id.value,
        "email": user.email,
        "displayName": user.display_name,
    }
