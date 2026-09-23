require 'sinatra/base'
require 'json'
require 'user_management/user_id'

module UserManagement
  class FriendsRoutes < Sinatra::Base
    disable :protection
    set :host_authorization, { permitted_hosts: [] }

    def initialize(add_friend, remove_friend, list_friends)
      @add_friend    = add_friend
      @remove_friend = remove_friend
      @list_friends  = list_friends
      super()
    end

    post '/users/:user_id/friends' do
      content_type :json
      body = JSON.parse(request.body.read)
      owner_id  = UserId.new(value: params[:user_id])
      friend_id = UserId.new(value: body['friendId'])
      @add_friend.call(owner_id, friend_id)
      {}.to_json
    end

    delete '/users/:user_id/friends/:friend_id' do
      content_type :json
      owner_id  = UserId.new(value: params[:user_id])
      friend_id = UserId.new(value: params[:friend_id])
      @remove_friend.call(owner_id, friend_id)
      {}.to_json
    end

    get '/users/:user_id/friends' do
      content_type :json
      owner_id    = UserId.new(value: params[:user_id])
      friendships = @list_friends.call(owner_id)
      friendships.map { |f| { friendId: f.friend_id.value } }.to_json
    end
  end
end
