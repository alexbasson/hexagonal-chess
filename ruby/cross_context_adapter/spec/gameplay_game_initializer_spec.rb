require 'organizing/game_id'
require 'organizing/player'
require 'organizing/game_initializer'
require 'gameplay/board_id'
require 'gameplay/setup_board'
require 'gameplay_game_initializer'

RSpec.describe GameplayGameInitializer do
  subject(:initializer) { described_class.new(setup_board) }

  let(:setup_board) { instance_double(Gameplay::SetupBoard) }

  describe '#initialize_game' do
    it 'returns a GameId derived from the board id' do
      allow(setup_board).to receive(:call).and_return(Gameplay::BoardId.new(value: 'board-abc'))

      result = initializer.initialize_game(
        Organizing::Player.new(name: 'Alice'),
        Organizing::Player.new(name: 'Bob')
      )

      expect(result).to eq(Organizing::GameId.new(value: 'board-abc'))
    end

    it 'passes player names to setup_board' do
      allow(setup_board).to receive(:call).and_return(Gameplay::BoardId.new(value: 'x'))

      initializer.initialize_game(
        Organizing::Player.new(name: 'Alice'),
        Organizing::Player.new(name: 'Bob')
      )

      expect(setup_board).to have_received(:call).with('Alice', 'Bob')
    end
  end
end
