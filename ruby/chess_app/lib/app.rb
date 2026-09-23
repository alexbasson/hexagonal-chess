require 'gameplay/in_memory_board_repository'
require 'gameplay/in_memory_move_repository'
require 'gameplay/setup_board'
require 'gameplay/make_move'
require 'gameplay/moves_routes'
require 'gameplay/board_routes'
require 'organizing/in_memory_game_repository'
require 'organizing/in_memory_invitation_repository'
require 'organizing/start_game'
require 'organizing/get_game'
require 'organizing/list_games'
require 'organizing/create_invitation'
require 'organizing/accept_invitation'
require 'organizing/decline_invitation'
require 'organizing/list_invitations'
require 'organizing/games_routes'
require 'organizing/invitations_routes'
require 'gameplay_game_initializer'
require 'user_management/in_memory_user_repository'
require 'user_management/in_memory_friendship_repository'
require 'user_management/create_user'
require 'user_management/get_user'
require 'user_management/update_user'
require 'user_management/delete_user'
require 'user_management/list_users'
require 'user_management/add_friend'
require 'user_management/remove_friend'
require 'user_management/list_friends'
require 'user_management/users_routes'
require 'user_management/friends_routes'
require 'user_management_organizing/user_management_user_name_provider'
require 'user_management_organizing/user_management_friendship_checker'

class App
  def initialize
    board_repository = Gameplay::InMemoryBoardRepository.new
    move_repository  = Gameplay::InMemoryMoveRepository.new
    setup_board      = Gameplay::SetupBoard.new(board_repository)
    make_move        = Gameplay::MakeMove.new(board_repository, move_repository)

    game_repository  = Organizing::InMemoryGameRepository.new
    game_initializer = GameplayGameInitializer.new(setup_board)
    start_game       = Organizing::StartGame.new(game_repository, game_initializer)
    get_game         = Organizing::GetGame.new(game_repository)
    list_games       = Organizing::ListGames.new(game_repository)

    user_repository       = UserManagement::InMemoryUserRepository.new
    friendship_repository = UserManagement::InMemoryFriendshipRepository.new
    create_user  = UserManagement::CreateUser.new(user_repository)
    get_user     = UserManagement::GetUser.new(user_repository)
    update_user  = UserManagement::UpdateUser.new(user_repository)
    delete_user  = UserManagement::DeleteUser.new(user_repository)
    list_users   = UserManagement::ListUsers.new(user_repository)
    add_friend   = UserManagement::AddFriend.new(friendship_repository)
    remove_friend = UserManagement::RemoveFriend.new(friendship_repository)
    list_friends  = UserManagement::ListFriends.new(friendship_repository)

    user_name_provider = UserManagementOrganizing::UserManagementUserNameProvider.new(user_repository)
    friendship_checker = UserManagementOrganizing::UserManagementFriendshipChecker.new(friendship_repository)

    invitation_repository = Organizing::InMemoryInvitationRepository.new
    create_invitation  = Organizing::CreateInvitation.new(friendship_checker, invitation_repository)
    accept_invitation  = Organizing::AcceptInvitation.new(invitation_repository, user_name_provider, start_game)
    decline_invitation = Organizing::DeclineInvitation.new(invitation_repository)
    list_invitations   = Organizing::ListInvitations.new(invitation_repository)

    @rack_app = Rack::Cascade.new([
      Gameplay::MovesRoutes.new(make_move),
      Gameplay::BoardRoutes.new(board_repository),
      Organizing::GamesRoutes.new(start_game, get_game, list_games),
      Organizing::InvitationsRoutes.new(create_invitation, accept_invitation, decline_invitation, list_invitations),
      UserManagement::UsersRoutes.new(create_user, get_user, update_user, delete_user, list_users),
      UserManagement::FriendsRoutes.new(add_friend, remove_friend, list_friends)
    ])
  end

  def call(env)
    @rack_app.call(env)
  end
end
