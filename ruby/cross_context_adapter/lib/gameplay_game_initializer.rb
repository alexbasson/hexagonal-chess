require 'organizing/game_id'
require 'organizing/game_initializer'
require 'gameplay/board_id'
require 'gameplay/setup_board'

class GameplayGameInitializer
  include Organizing::GameInitializer

  def initialize(setup_board)
    @setup_board = setup_board
  end

  def initialize_game(white_player, black_player)
    board_id = @setup_board.call(white_player.name, black_player.name)
    Organizing::GameId.new(value: board_id.value)
  end
end
