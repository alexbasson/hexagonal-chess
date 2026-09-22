require 'organizing/game_id'
require 'organizing/player'
require 'organizing/game'
require 'organizing/in_memory_game_repository'

RSpec.describe Organizing::InMemoryGameRepository do
  subject(:repo) { described_class.new }

  let(:game_id) { Organizing::GameId.new(value: 'g1') }
  let(:game)    { Organizing::Game.new(id: game_id, white: Organizing::Player.new(name: 'Alice'), black: Organizing::Player.new(name: 'Bob')) }

  it 'returns nil when game not stored' do
    expect(repo.find_by_id(game_id)).to be_nil
  end

  it 'returns a saved game by id' do
    repo.save(game)

    expect(repo.find_by_id(game_id)).to eq(game)
  end

  it 'returns all games' do
    repo.save(game)

    expect(repo.find_all).to eq([game])
  end
end
