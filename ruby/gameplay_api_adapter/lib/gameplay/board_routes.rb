require 'sinatra/base'
require 'json'
require 'gameplay/board_id'
require 'gameplay/board_repository'

module Gameplay
  class BoardRoutes < Sinatra::Base
    disable :protection
    set :host_authorization, { permitted_hosts: [] }

    def initialize(board_repository)
      @board_repository = board_repository
      super()
    end

    get '/games/:game_id/board' do
      content_type :json
      board = @board_repository.find_by_id(BoardId.new(value: params[:game_id]))
      halt 404 if board.nil?
      board_to_json(board)
    end

    private

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
