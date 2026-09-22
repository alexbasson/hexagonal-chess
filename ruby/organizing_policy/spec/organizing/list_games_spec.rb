require 'organizing/game_id'
require 'organizing/player'
require 'organizing/game'
require 'organizing/game_repository'
require 'organizing/list_games'

RSpec.describe Organizing::ListGames do
  subject(:list_games) { described_class.new(game_repository) }

  let(:game_repository) { instance_double(Organizing::GameRepository) }

  describe '#call' do
    it 'returns all games from repository' do
      games = [Organizing::Game.new(
        id: Organizing::GameId.new(value: 'g1'),
        white: Organizing::Player.new(name: 'Alice'),
        black: Organizing::Player.new(name: 'Bob')
      )]
      allow(game_repository).to receive(:find_all).and_return(games)

      expect(list_games.call).to eq(games)
    end
  end
end
