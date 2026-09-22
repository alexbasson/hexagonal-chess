require 'organizing/game_id'
require 'organizing/player'
require 'organizing/game'
require 'organizing/game_repository'
require 'organizing/game_initializer'
require 'organizing/start_game'

RSpec.describe Organizing::StartGame do
  subject(:start_game) { described_class.new(game_repository, game_initializer) }

  let(:game_repository)  { instance_double(Organizing::GameRepository) }
  let(:game_initializer) { instance_double(Organizing::GameInitializer) }
  let(:game_id)          { Organizing::GameId.new(value: 'game-1') }

  before do
    allow(game_initializer).to receive(:initialize_game).and_return(game_id)
    allow(game_repository).to receive(:save)
  end

  describe '#call' do
    it 'returns the game id from the initializer' do
      result = start_game.call('Alice', 'Bob')

      expect(result).to eq(game_id)
    end

    it 'calls initializer with player objects' do
      start_game.call('Alice', 'Bob')

      expect(game_initializer).to have_received(:initialize_game) do |white, black|
        expect(white.name).to eq('Alice')
        expect(black.name).to eq('Bob')
      end
    end

    it 'persists the game with both players' do
      start_game.call('Alice', 'Bob')

      expect(game_repository).to have_received(:save) do |game|
        expect(game.id).to eq(game_id)
        expect(game.white.name).to eq('Alice')
        expect(game.black.name).to eq('Bob')
      end
    end
  end
end
