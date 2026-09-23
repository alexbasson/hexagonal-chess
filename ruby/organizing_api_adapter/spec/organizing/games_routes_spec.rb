require 'rack/test'
require 'json'
require 'organizing/game_id'
require 'organizing/player'
require 'organizing/game'
require 'organizing/start_game'
require 'organizing/get_game'
require 'organizing/list_games'
require 'organizing/games_routes'

RSpec.describe Organizing::GamesRoutes do
  include Rack::Test::Methods

  let(:start_game) { instance_double(Organizing::StartGame) }
  let(:get_game)   { instance_double(Organizing::GetGame) }
  let(:list_games) { instance_double(Organizing::ListGames) }
  let(:app)        { described_class.new(start_game, get_game, list_games) }

  let(:game_id) { Organizing::GameId.new(value: 'game-1') }
  let(:game)    { Organizing::Game.new(id: game_id, white: Organizing::Player.new(name: 'Alice'), black: Organizing::Player.new(name: 'Bob')) }

  describe 'POST /games' do
    it 'returns 201 with game id' do
      allow(start_game).to receive(:call).with('Alice', 'Bob').and_return(game_id)

      post '/games', { whiteName: 'Alice', blackName: 'Bob' }.to_json, 'CONTENT_TYPE' => 'application/json'

      expect(last_response.status).to eq(201)
      expect(JSON.parse(last_response.body)['gameId']).to eq('game-1')
    end
  end

  describe 'GET /games' do
    it 'returns list of games' do
      allow(list_games).to receive(:call).and_return([game])

      get '/games'

      expect(last_response.status).to eq(200)
      body = JSON.parse(last_response.body)
      expect(body.first['id']).to eq('game-1')
    end
  end

  describe 'GET /games/:game_id' do
    it 'returns game when found' do
      allow(get_game).to receive(:call).with(game_id).and_return(game)

      get "/games/game-1"

      expect(last_response.status).to eq(200)
      expect(JSON.parse(last_response.body)['white']['name']).to eq('Alice')
    end

    it 'returns 404 when game not found' do
      allow(get_game).to receive(:call).and_raise(ArgumentError, 'Game not found')

      get "/games/missing"

      expect(last_response.status).to eq(404)
    end
  end
end
