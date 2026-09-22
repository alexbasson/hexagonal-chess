require 'gameplay/board_repository'

module Gameplay
  class InMemoryBoardRepository
    include BoardRepository

    def initialize
      @store = {}
    end

    def save(board)
      @store[board.id] = board
    end

    def find_by_id(board_id)
      @store[board_id]
    end
  end
end
