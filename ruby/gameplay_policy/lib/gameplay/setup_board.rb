require 'securerandom'
require_relative 'board_id'
require_relative 'board'
require_relative 'square'
require_relative 'piece'
require_relative 'board_repository'

module Gameplay
  class SetupBoard
    BACK_RANK = [Piece::Rook, Piece::Knight, Piece::Bishop, Piece::Queen,
                 Piece::King, Piece::Bishop, Piece::Knight, Piece::Rook].freeze

    def initialize(board_repository)
      @board_repository = board_repository
    end

    def call(white_player_name, black_player_name)
      board_id = BoardId.new(value: SecureRandom.uuid)
      board = Board.new(
        id: board_id,
        white_player_name: white_player_name,
        black_player_name: black_player_name,
        active_color: :white,
        pieces: initial_pieces
      )
      @board_repository.save(board)
      board_id
    end

    private

    def initial_pieces
      pieces = {}
      BACK_RANK.each_with_index do |piece_class, i|
        file = i + 1
        pieces[Square.new(file: file, rank: 1)] = piece_class.new(color: :white)
        pieces[Square.new(file: file, rank: 8)] = piece_class.new(color: :black)
        pieces[Square.new(file: file, rank: 2)] = Piece::Pawn.new(color: :white)
        pieces[Square.new(file: file, rank: 7)] = Piece::Pawn.new(color: :black)
      end
      pieces
    end
  end
end
