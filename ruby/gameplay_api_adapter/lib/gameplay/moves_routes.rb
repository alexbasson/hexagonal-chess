require 'sinatra/base'
require 'json'
require 'gameplay/board_id'
require 'gameplay/square'
require 'gameplay/move'

module Gameplay
  class MovesRoutes < Sinatra::Base
    disable :protection
    set :host_authorization, { permitted_hosts: [] }

    def initialize(make_move)
      @make_move = make_move
      super()
    end

    post '/games/:game_id/moves' do
      content_type :json
      body = JSON.parse(request.body.read)
      move = parse_move(body)
      board = @make_move.call(BoardId.new(value: params[:game_id]), move)
      board_to_json(board)
    rescue ArgumentError => e
      status 422
      { error: e.message }.to_json
    end

    private

    def parse_move(body)
      Move.new(from: parse_square(body['from']), to: parse_square(body['to']))
    end

    def parse_square(notation)
      file = notation[0].ord - 'a'.ord + 1
      rank = notation[1].to_i
      Square.new(file: file, rank: rank)
    end

    def board_to_json(board)
      {
        id: board.id.value,
        white_player_name: board.white_player_name,
        black_player_name: board.black_player_name,
        active_color: board.active_color.to_s,
        pieces: board.pieces.map { |sq, p| { square: { file: sq.file, rank: sq.rank }, piece: piece_to_h(p) } }
      }.to_json
    end

    def piece_to_h(piece)
      { type: piece.class.name.split('::').last, color: piece.color.to_s }
    end
  end
end
