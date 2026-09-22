require_relative 'game_id'
require_relative 'game_repository'

module Organizing
  class GetGame
    def initialize(game_repository)
      @game_repository = game_repository
    end

    def call(game_id)
      game = @game_repository.find_by_id(game_id)
      raise ArgumentError, 'Game not found' if game.nil?
      game
    end
  end
end
