require_relative 'game_repository'

module Organizing
  class ListGames
    def initialize(game_repository)
      @game_repository = game_repository
    end

    def call
      @game_repository.find_all
    end
  end
end
