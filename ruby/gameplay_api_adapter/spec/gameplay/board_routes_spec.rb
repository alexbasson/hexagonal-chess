require 'rack/test'
require 'json'
require 'gameplay/board_id'
require 'gameplay/board'
require 'gameplay/square'
require 'gameplay/piece'
require 'gameplay/board_repository'
require 'gameplay/board_routes'

RSpec.describe Gameplay::BoardRoutes do
  include Rack::Test::Methods

  let(:board_repository) { instance_double(Gameplay::BoardRepository) }
  let(:app)              { described_class.new(board_repository) }

  let(:board_id) { 'game-1' }
  let(:board) do
    Gameplay::Board.new(
      id: Gameplay::BoardId.new(value: board_id),
      white_player_name: 'Alice', black_player_name: 'Bob',
      active_color: :white,
      pieces: { Gameplay::Square.new(file: 5, rank: 1) => Gameplay::Piece::King.new(color: :white) }
    )
  end

  describe 'GET /games/:game_id/board' do
    it 'returns 200 with board when found' do
      allow(board_repository).to receive(:find_by_id)
        .with(Gameplay::BoardId.new(value: board_id))
        .and_return(board)

      get "/games/#{board_id}/board"

      expect(last_response.status).to eq(200)
      body = JSON.parse(last_response.body)
      expect(body['id']).to eq(board_id)
      expect(body['pieces'].size).to eq(1)
    end

    it 'returns 404 when board not found' do
      allow(board_repository).to receive(:find_by_id).and_return(nil)

      get "/games/unknown/board"

      expect(last_response.status).to eq(404)
    end
  end
end
