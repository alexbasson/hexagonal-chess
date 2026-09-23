require 'gameplay/board_id'
require 'gameplay/board'
require 'gameplay/square'
require 'gameplay/piece'
require 'gameplay/move'
require 'gameplay/board_repository'
require 'gameplay/move_repository'
require 'gameplay/make_move'

RSpec.describe Gameplay::MakeMove do
  subject(:make_move) { described_class.new(board_repository, move_repository) }

  let(:board_repository) { instance_double(Gameplay::BoardRepository) }
  let(:move_repository)  { instance_double(Gameplay::MoveRepository) }

  let(:board_id) { Gameplay::BoardId.new(value: 'board-1') }
  let(:white_pawn_square) { Gameplay::Square.new(file: 5, rank: 2) }
  let(:target_square)     { Gameplay::Square.new(file: 5, rank: 3) }
  let(:move)              { Gameplay::Move.new(from: white_pawn_square, to: target_square) }

  let(:pieces) { { white_pawn_square => Gameplay::Piece::Pawn.new(color: :white) } }
  let(:board) do
    Gameplay::Board.new(
      id: board_id,
      white_player_name: 'Alice',
      black_player_name: 'Bob',
      active_color: :white,
      pieces: pieces
    )
  end

  before do
    allow(board_repository).to receive(:find_by_id).with(board_id).and_return(board)
    allow(board_repository).to receive(:save)
    allow(move_repository).to receive(:save)
  end

  describe '#call' do
    it 'raises when board not found' do
      allow(board_repository).to receive(:find_by_id).with(board_id).and_return(nil)

      expect { make_move.call(board_id, move) }.to raise_error(ArgumentError, /board not found/i)
    end

    it 'raises when no piece at source square' do
      empty_square = Gameplay::Square.new(file: 1, rank: 3)
      bad_move = Gameplay::Move.new(from: empty_square, to: target_square)

      expect { make_move.call(board_id, bad_move) }.to raise_error(ArgumentError, /no piece/i)
    end

    it "raises when moving opponent's piece" do
      black_pawn_square = Gameplay::Square.new(file: 5, rank: 7)
      black_board = Gameplay::Board.new(
        id: board_id, white_player_name: 'Alice', black_player_name: 'Bob',
        active_color: :white,
        pieces: { black_pawn_square => Gameplay::Piece::Pawn.new(color: :black) }
      )
      allow(board_repository).to receive(:find_by_id).with(board_id).and_return(black_board)
      bad_move = Gameplay::Move.new(from: black_pawn_square, to: Gameplay::Square.new(file: 5, rank: 6))

      expect { make_move.call(board_id, bad_move) }.to raise_error(ArgumentError, /not your piece/i)
    end

    it 'persists the move and returns the updated board' do
      result = make_move.call(board_id, move)

      expect(move_repository).to have_received(:save).with(move, board_id)
      expect(board_repository).to have_received(:save) do |saved_board|
        expect(saved_board.pieces[target_square]).to eq(Gameplay::Piece::Pawn.new(color: :white))
        expect(saved_board.pieces[white_pawn_square]).to be_nil
        expect(saved_board.active_color).to eq(:black)
      end
      expect(result).to be_a(Gameplay::Board)
    end

    it 'pawn advances one square forward' do
      result = make_move.call(board_id, move)

      expect(result.pieces[target_square]).to eq(Gameplay::Piece::Pawn.new(color: :white))
    end

    it 'pawn advances two squares from starting rank' do
      two_square_target = Gameplay::Square.new(file: 5, rank: 4)
      two_square_move   = Gameplay::Move.new(from: white_pawn_square, to: two_square_target)

      result = make_move.call(board_id, two_square_move)

      expect(result.pieces[two_square_target]).to eq(Gameplay::Piece::Pawn.new(color: :white))
    end

    it 'raises when move leaves own king in check' do
      king_square  = Gameplay::Square.new(file: 5, rank: 1)
      enemy_rook   = Gameplay::Square.new(file: 5, rank: 8)
      pinned_sq    = Gameplay::Square.new(file: 5, rank: 4)
      pinned_board = Gameplay::Board.new(
        id: board_id, white_player_name: 'Alice', black_player_name: 'Bob',
        active_color: :white,
        pieces: {
          king_square => Gameplay::Piece::King.new(color: :white),
          pinned_sq   => Gameplay::Piece::Rook.new(color: :white),
          enemy_rook  => Gameplay::Piece::Rook.new(color: :black)
        }
      )
      allow(board_repository).to receive(:find_by_id).with(board_id).and_return(pinned_board)
      exposing_move = Gameplay::Move.new(from: pinned_sq, to: Gameplay::Square.new(file: 6, rank: 4))

      expect { make_move.call(board_id, exposing_move) }.to raise_error(ArgumentError, /leaves king in check/i)
    end
  end
end
