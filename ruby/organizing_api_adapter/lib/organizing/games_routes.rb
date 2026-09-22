require 'sinatra/base'
require 'json'
require 'organizing/game_id'
require 'organizing/start_game'
require 'organizing/get_game'
require 'organizing/list_games'

module Organizing
  class GamesRoutes < Sinatra::Base
    disable :protection
    set :host_authorization, { permitted_hosts: [] }

    def initialize(start_game, get_game, list_games)
      @start_game = start_game
      @get_game   = get_game
      @list_games = list_games
      super()
    end

    post '/games' do
      content_type :json
      body = JSON.parse(request.body.read)
      game_id = @start_game.call(body['white_name'], body['black_name'])
      status 201
      { game_id: game_id.value }.to_json
    end

    get '/games' do
      content_type :json
      @list_games.call.map { |g| game_to_h(g) }.to_json
    end

    get '/games/:game_id' do
      content_type :json
      game = @get_game.call(GameId.new(value: params[:game_id]))
      game_to_h(game).to_json
    rescue ArgumentError
      status 404
    end

    private

    def game_to_h(game)
      {
        id: game.id.value,
        white: { name: game.white.name },
        black: { name: game.black.name }
      }
    end
  end
end
