require 'organizing/game_repository'

module Organizing
  class InMemoryGameRepository
    include GameRepository

    def initialize
      @store = {}
    end

    def save(game)
      @store[game.id] = game
    end

    def find_by_id(game_id)
      @store[game_id]
    end

    def find_all
      @store.values
    end
  end
end
