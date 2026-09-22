require 'gameplay/in_memory_board_repository'
require 'gameplay/in_memory_move_repository'
require 'gameplay/setup_board'
require 'gameplay/make_move'
require 'gameplay/moves_routes'
require 'gameplay/board_routes'
require 'organizing/in_memory_game_repository'
require 'organizing/start_game'
require 'organizing/get_game'
require 'organizing/list_games'
require 'organizing/games_routes'
require 'gameplay_game_initializer'

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

    @rack_app = Rack::Cascade.new([
      Gameplay::MovesRoutes.new(make_move),
      Gameplay::BoardRoutes.new(board_repository),
      Organizing::GamesRoutes.new(start_game, get_game, list_games)
    ])
  end

  def call(env)
    @rack_app.call(env)
  end
end
