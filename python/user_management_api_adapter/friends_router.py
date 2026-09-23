from fastapi import APIRouter
from pydantic import BaseModel, ConfigDict
from pydantic.alias_generators import to_camel
from user_management_policy.user_id import UserId


class AddFriendRequest(BaseModel):
    model_config = ConfigDict(alias_generator=to_camel, populate_by_name=True)

    friend_id: str


def create_friends_router(add_friend, remove_friend, list_friends) -> APIRouter:
    router = APIRouter()

    @router.post("/users/{user_id}/friends")
    def post_friend(user_id: str, body: AddFriendRequest):
        add_friend(UserId(user_id), UserId(body.friend_id))
        return {}

    @router.delete("/users/{user_id}/friends/{friend_id}")
    def delete_friend(user_id: str, friend_id: str):
        remove_friend(UserId(user_id), UserId(friend_id))
        return {}

    @router.get("/users/{user_id}/friends")
    def get_friends(user_id: str):
        friendships = list_friends(UserId(user_id))
        return [{"friendId": f.friend_id.value} for f in friendships]

    return router
