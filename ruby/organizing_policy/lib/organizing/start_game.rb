require_relative 'game_id'
require_relative 'player'
require_relative 'game'
require_relative 'game_repository'
require_relative 'game_initializer'

module Organizing
  class StartGame
    def initialize(game_repository, game_initializer)
      @game_repository  = game_repository
      @game_initializer = game_initializer
    end

    def call(white_name, black_name)
      white = Player.new(name: white_name)
      black = Player.new(name: black_name)
      game_id = @game_initializer.initialize_game(white, black)
      @game_repository.save(Game.new(id: game_id, white: white, black: black))
      game_id
    end
  end
end
