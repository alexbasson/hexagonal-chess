require 'rack/test'
require 'json'
require 'user_management/user_id'
require 'user_management/friendship'
require 'user_management/add_friend'
require 'user_management/remove_friend'
require 'user_management/list_friends'
require 'user_management/friends_routes'

RSpec.describe UserManagement::FriendsRoutes do
  include Rack::Test::Methods

  let(:add_friend)    { instance_double(UserManagement::AddFriend) }
  let(:remove_friend) { instance_double(UserManagement::RemoveFriend) }
  let(:list_friends)  { instance_double(UserManagement::ListFriends) }
  let(:app)           { described_class.new(add_friend, remove_friend, list_friends) }

  let(:owner_id)   { UserManagement::UserId.new(value: 'u1') }
  let(:friend_id)  { UserManagement::UserId.new(value: 'u2') }
  let(:friendship) { UserManagement::Friendship.new(owner_id: owner_id, friend_id: friend_id) }

  describe 'POST /users/:id/friends' do
    it 'calls add_friend and returns 200' do
      allow(add_friend).to receive(:call).with(owner_id, friend_id)

      post '/users/u1/friends', { friendId: 'u2' }.to_json, 'CONTENT_TYPE' => 'application/json'

      expect(last_response.status).to eq(200)
    end
  end

  describe 'DELETE /users/:id/friends/:friend_id' do
    it 'calls remove_friend and returns 200' do
      allow(remove_friend).to receive(:call).with(owner_id, friend_id)

      delete '/users/u1/friends/u2'

      expect(last_response.status).to eq(200)
    end
  end

  describe 'GET /users/:id/friends' do
    it 'returns list of friend ids' do
      allow(list_friends).to receive(:call).with(owner_id).and_return([friendship])

      get '/users/u1/friends'

      expect(last_response.status).to eq(200)
      body = JSON.parse(last_response.body)
      expect(body.first['friendId']).to eq('u2')
    end
  end
end
