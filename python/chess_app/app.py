from fastapi import FastAPI
from gameplay_db_adapter.in_memory_board_repository import InMemoryBoardRepository
from gameplay_db_adapter.in_memory_move_repository import InMemoryMoveRepository
from gameplay_policy.setup_board import make_setup_board
from gameplay_policy.make_move import make_make_move
from organizing_db_adapter.in_memory_game_repository import InMemoryGameRepository
from organizing_db_adapter.in_memory_invitation_repository import InMemoryInvitationRepository
from cross_context_adapter.gameplay_game_initializer import GameplayGameInitializer
from organizing_policy.start_game import make_start_game
from organizing_policy.get_game import make_get_game
from organizing_policy.list_games import make_list_games
from organizing_policy.create_invitation import make_create_invitation
from organizing_policy.accept_invitation import make_accept_invitation
from organizing_policy.decline_invitation import make_decline_invitation
from organizing_policy.list_invitations import make_list_invitations
from user_management_db_adapter.in_memory_user_repository import InMemoryUserRepository
from user_management_db_adapter.in_memory_friendship_repository import InMemoryFriendshipRepository
from user_management_policy.create_user import make_create_user
from user_management_policy.get_user import make_get_user
from user_management_policy.update_user import make_update_user
from user_management_policy.delete_user import make_delete_user
from user_management_policy.list_users import make_list_users
from user_management_policy.add_friend import make_add_friend
from user_management_policy.remove_friend import make_remove_friend
from user_management_policy.list_friends import make_list_friends
from user_management_organizing_adapter.user_management_user_name_provider import UserManagementUserNameProvider
from user_management_organizing_adapter.user_management_friendship_checker import UserManagementFriendshipChecker
from gameplay_api_adapter.moves_router import create_moves_router
from gameplay_api_adapter.board_router import create_board_router
from organizing_api_adapter.games_router import create_games_router
from organizing_api_adapter.invitations_router import create_invitations_router
from user_management_api_adapter.users_router import create_users_router
from user_management_api_adapter.friends_router import create_friends_router


def create_app() -> FastAPI:
    board_repository = InMemoryBoardRepository()
    move_repository = InMemoryMoveRepository()
    setup_board = make_setup_board(board_repository)
    make_move = make_make_move(board_repository, move_repository)

    game_repository = InMemoryGameRepository()
    game_initializer = GameplayGameInitializer(setup_board)
    start_game = make_start_game(game_repository, game_initializer)
    get_game = make_get_game(game_repository)
    list_games = make_list_games(game_repository)

    user_repository = InMemoryUserRepository()
    friendship_repository = InMemoryFriendshipRepository()
    create_user = make_create_user(user_repository)
    get_user = make_get_user(user_repository)
    update_user = make_update_user(user_repository)
    delete_user = make_delete_user(user_repository)
    list_users = make_list_users(user_repository)
    add_friend = make_add_friend(friendship_repository)
    remove_friend = make_remove_friend(friendship_repository)
    list_friends = make_list_friends(friendship_repository)

    user_name_provider = UserManagementUserNameProvider(user_repository)
    friendship_checker = UserManagementFriendshipChecker(friendship_repository)

    invitation_repository = InMemoryInvitationRepository()
    create_invitation = make_create_invitation(friendship_checker, invitation_repository)
    accept_invitation = make_accept_invitation(invitation_repository, user_name_provider, start_game)
    decline_invitation = make_decline_invitation(invitation_repository)
    list_invitations = make_list_invitations(invitation_repository)

    app = FastAPI()
    app.include_router(create_moves_router(make_move))
    app.include_router(create_board_router(board_repository))
    app.include_router(create_games_router(start_game, get_game, list_games))
    app.include_router(create_invitations_router(create_invitation, accept_invitation, decline_invitation, list_invitations))
    app.include_router(create_users_router(create_user, get_user, update_user, delete_user, list_users))
    app.include_router(create_friends_router(add_friend, remove_friend, list_friends))
    return app
