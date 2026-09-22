require 'gameplay/board_id'
require 'gameplay/square'
require 'gameplay/move'
require 'gameplay/in_memory_move_repository'

RSpec.describe Gameplay::InMemoryMoveRepository do
  subject(:repo) { described_class.new }

  let(:board_id) { Gameplay::BoardId.new(value: 'board-1') }
  let(:move) { Gameplay::Move.new(from: Gameplay::Square.new(file: 5, rank: 2), to: Gameplay::Square.new(file: 5, rank: 3)) }

  it 'returns empty list when no moves stored' do
    expect(repo.find_by_board_id(board_id)).to eq([])
  end

  it 'returns saved moves for a board' do
    repo.save(move, board_id)

    expect(repo.find_by_board_id(board_id)).to eq([move])
  end
end
