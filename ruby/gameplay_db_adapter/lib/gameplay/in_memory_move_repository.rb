require 'gameplay/move_repository'

module Gameplay
  class InMemoryMoveRepository
    include MoveRepository

    def initialize
      @store = Hash.new { |h, k| h[k] = [] }
    end

    def save(move, board_id)
      @store[board_id] << move
    end

    def find_by_board_id(board_id)
      @store[board_id]
    end
  end
end
