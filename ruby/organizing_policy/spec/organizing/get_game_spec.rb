require 'organizing/game_id'
require 'organizing/player'
require 'organizing/game'
require 'organizing/game_repository'
require 'organizing/get_game'

RSpec.describe Organizing::GetGame do
  subject(:get_game) { described_class.new(game_repository) }

  let(:game_repository) { instance_double(Organizing::GameRepository) }
  let(:game_id)         { Organizing::GameId.new(value: 'game-1') }
  let(:game)            { Organizing::Game.new(id: game_id, white: Organizing::Player.new(name: 'Alice'), black: Organizing::Player.new(name: 'Bob')) }

  describe '#call' do
    it 'returns the game when found' do
      allow(game_repository).to receive(:find_by_id).with(game_id).and_return(game)

      expect(get_game.call(game_id)).to eq(game)
    end

    it 'raises when game not found' do
      allow(game_repository).to receive(:find_by_id).with(game_id).and_return(nil)

      expect { get_game.call(game_id) }.to raise_error(ArgumentError, /game not found/i)
    end
  end
end
