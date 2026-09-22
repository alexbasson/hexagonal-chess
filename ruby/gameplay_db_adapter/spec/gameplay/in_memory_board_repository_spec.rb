require 'gameplay/board_id'
require 'gameplay/board'
require 'gameplay/square'
require 'gameplay/piece'
require 'gameplay/in_memory_board_repository'

RSpec.describe Gameplay::InMemoryBoardRepository do
  subject(:repo) { described_class.new }

  let(:board_id) { Gameplay::BoardId.new(value: 'board-1') }
  let(:board) do
    Gameplay::Board.new(
      id: board_id, white_player_name: 'Alice', black_player_name: 'Bob',
      active_color: :white, pieces: {}
    )
  end

  it 'returns nil when no board stored' do
    expect(repo.find_by_id(board_id)).to be_nil
  end

  it 'returns a saved board by id' do
    repo.save(board)

    expect(repo.find_by_id(board_id)).to eq(board)
  end
end
