require 'rack/test'
require 'json'
require 'gameplay/board_id'
require 'gameplay/board'
require 'gameplay/square'
require 'gameplay/piece'
require 'gameplay/move'
require 'gameplay/make_move'
require 'gameplay/moves_routes'

RSpec.describe Gameplay::MovesRoutes do
  include Rack::Test::Methods

  let(:make_move) { instance_double(Gameplay::MakeMove) }
  let(:app)       { described_class.new(make_move) }

  let(:board_id) { 'game-1' }
  let(:updated_board) do
    Gameplay::Board.new(
      id: Gameplay::BoardId.new(value: board_id),
      white_player_name: 'Alice', black_player_name: 'Bob',
      active_color: :black, pieces: {}
    )
  end

  describe 'POST /games/:game_id/moves' do
    it 'returns 200 with the updated board' do
      allow(make_move).to receive(:call).and_return(updated_board)

      post "/games/#{board_id}/moves", { from: 'e2', to: 'e4' }.to_json,
           'CONTENT_TYPE' => 'application/json'

      expect(last_response.status).to eq(200)
      body = JSON.parse(last_response.body)
      expect(body['active_color']).to eq('black')
    end

    it 'passes parsed move to make_move' do
      allow(make_move).to receive(:call).and_return(updated_board)

      post "/games/#{board_id}/moves", { from: 'e2', to: 'e4' }.to_json,
           'CONTENT_TYPE' => 'application/json'

      expect(make_move).to have_received(:call) do |bid, move|
        expect(bid).to eq(Gameplay::BoardId.new(value: board_id))
        expect(move.from).to eq(Gameplay::Square.new(file: 5, rank: 2))
        expect(move.to).to eq(Gameplay::Square.new(file: 5, rank: 4))
      end
    end

    it 'returns 422 when move is illegal' do
      allow(make_move).to receive(:call).and_raise(ArgumentError, 'Illegal move')

      post "/games/#{board_id}/moves", { from: 'e2', to: 'e5' }.to_json,
           'CONTENT_TYPE' => 'application/json'

      expect(last_response.status).to eq(422)
    end
  end
end
