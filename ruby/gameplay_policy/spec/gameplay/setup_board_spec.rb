require 'gameplay/board_repository'
require 'gameplay/setup_board'

RSpec.describe Gameplay::SetupBoard do
  subject(:setup_board) { described_class.new(board_repository) }

  let(:board_repository) { instance_double(Gameplay::BoardRepository) }

  before { allow(board_repository).to receive(:save) }

  describe '#call' do
    it 'returns a board id' do
      result = setup_board.call('Alice', 'Bob')

      expect(result).to be_a(Gameplay::BoardId)
    end

    it 'persists the board with player names' do
      setup_board.call('Alice', 'Bob')

      expect(board_repository).to have_received(:save) do |board|
        expect(board.white_player_name).to eq('Alice')
        expect(board.black_player_name).to eq('Bob')
      end
    end

    it 'initial position places all 32 pieces' do
      setup_board.call('Alice', 'Bob')

      expect(board_repository).to have_received(:save) do |board|
        expect(board.pieces.size).to eq(32)
        expect(board.pieces[Gameplay::Square.new(file: 5, rank: 1)]).to eq(Gameplay::Piece::King.new(color: :white))
        expect(board.pieces[Gameplay::Square.new(file: 4, rank: 1)]).to eq(Gameplay::Piece::Queen.new(color: :white))
        expect(board.pieces[Gameplay::Square.new(file: 5, rank: 8)]).to eq(Gameplay::Piece::King.new(color: :black))
        expect(board.pieces[Gameplay::Square.new(file: 5, rank: 2)]).to eq(Gameplay::Piece::Pawn.new(color: :white))
        expect(board.pieces[Gameplay::Square.new(file: 5, rank: 7)]).to eq(Gameplay::Piece::Pawn.new(color: :black))
      end
    end
  end
end
