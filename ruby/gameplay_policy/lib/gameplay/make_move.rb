require_relative 'board_id'
require_relative 'board'
require_relative 'square'
require_relative 'piece'
require_relative 'move'
require_relative 'board_repository'
require_relative 'move_repository'

module Gameplay
  class MakeMove
    def initialize(board_repository, move_repository)
      @board_repository = board_repository
      @move_repository  = move_repository
    end

    def call(board_id, move)
      board = @board_repository.find_by_id(board_id)
      raise ArgumentError, 'Board not found' if board.nil?

      piece = board.pieces[move.from]
      raise ArgumentError, 'No piece at source square' if piece.nil?
      raise ArgumentError, 'Not your piece' if piece.color != board.active_color

      raise ArgumentError, 'Illegal move' unless legal_move?(board, move.from, move.to)

      updated_board = apply_move(board, move)
      raise ArgumentError, 'Leaves king in check' if in_check?(updated_board, board.active_color)

      @move_repository.save(move, board_id)
      @board_repository.save(updated_board)
      updated_board
    end

    private

    def apply_move(board, move)
      new_pieces = board.pieces.dup
      new_pieces[move.to] = new_pieces.delete(move.from)
      next_color = board.active_color == :white ? :black : :white
      board.with(pieces: new_pieces, active_color: next_color)
    end

    def in_check?(board, color)
      king_square = board.pieces.find { |_, p| p.is_a?(Piece::King) && p.color == color }&.first
      return false if king_square.nil?

      opponent = color == :white ? :black : :white
      board.pieces.any? do |sq, p|
        p.color == opponent && legal_move?(board, sq, king_square)
      end
    end

    def legal_move?(board, from, to)
      piece = board.pieces[from]
      return false if piece.nil?

      df = to.file - from.file
      dr = to.rank - from.rank

      case piece
      when Piece::King   then df.abs <= 1 && dr.abs <= 1 && !(df == 0 && dr == 0)
      when Piece::Queen  then straight?(board, from, to) || diagonal?(board, from, to)
      when Piece::Rook   then straight?(board, from, to)
      when Piece::Bishop then diagonal?(board, from, to)
      when Piece::Knight then (df.abs == 2 && dr.abs == 1) || (df.abs == 1 && dr.abs == 2)
      when Piece::Pawn   then pawn_move?(board, piece, from, to, df, dr)
      else false
      end
    end

    def pawn_move?(board, piece, from, to, df, dr)
      direction = piece.color == :white ? 1 : -1
      if df == 0 && dr == direction
        board.pieces[to].nil?
      elsif df.abs == 1 && dr == direction
        !board.pieces[to].nil? && board.pieces[to].color != piece.color
      else
        false
      end
    end

    def straight?(board, from, to)
      df = to.file - from.file
      dr = to.rank - from.rank
      return false unless df == 0 || dr == 0

      step_f = df.zero? ? 0 : df / df.abs
      step_r = dr.zero? ? 0 : dr / dr.abs
      path_clear?(board, from, to, step_f, step_r)
    end

    def diagonal?(board, from, to)
      df = to.file - from.file
      dr = to.rank - from.rank
      return false unless df.abs == dr.abs

      path_clear?(board, from, to, df / df.abs, dr / dr.abs)
    end

    def path_clear?(board, from, to, step_f, step_r)
      f = from.file + step_f
      r = from.rank + step_r
      while f != to.file || r != to.rank
        return false if board.pieces[Square.new(file: f, rank: r)]
        f += step_f
        r += step_r
      end
      target = board.pieces[to]
      target.nil? || target.color != board.pieces[from].color
    end
  end
end
