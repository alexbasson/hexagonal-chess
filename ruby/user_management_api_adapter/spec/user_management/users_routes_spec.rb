require 'rack/test'
require 'json'
require 'user_management/user_id'
require 'user_management/user'
require 'user_management/create_user'
require 'user_management/get_user'
require 'user_management/update_user'
require 'user_management/delete_user'
require 'user_management/list_users'
require 'user_management/users_routes'

RSpec.describe UserManagement::UsersRoutes do
  include Rack::Test::Methods

  let(:create_user) { instance_double(UserManagement::CreateUser) }
  let(:get_user)    { instance_double(UserManagement::GetUser) }
  let(:update_user) { instance_double(UserManagement::UpdateUser) }
  let(:delete_user) { instance_double(UserManagement::DeleteUser) }
  let(:list_users)  { instance_double(UserManagement::ListUsers) }
  let(:app)         { described_class.new(create_user, get_user, update_user, delete_user, list_users) }

  let(:user_id) { UserManagement::UserId.new(value: 'u1') }
  let(:user)    { UserManagement::User.new(id: user_id, email: 'alice@example.com', display_name: 'Alice') }

  describe 'POST /admin/users' do
    it 'returns 201 with user id' do
      allow(create_user).to receive(:call).with('alice@example.com', 'Alice').and_return(user_id)

      post '/admin/users', { email: 'alice@example.com', displayName: 'Alice' }.to_json, 'CONTENT_TYPE' => 'application/json'

      expect(last_response.status).to eq(201)
      expect(JSON.parse(last_response.body)['userId']).to eq('u1')
    end
  end

  describe 'GET /admin/users' do
    it 'returns list of users' do
      allow(list_users).to receive(:call).and_return([user])

      get '/admin/users'

      expect(last_response.status).to eq(200)
      body = JSON.parse(last_response.body)
      expect(body.first['id']).to eq('u1')
    end
  end

  describe 'GET /admin/users/:id' do
    it 'returns user when found' do
      allow(get_user).to receive(:call).with(user_id).and_return(user)

      get '/admin/users/u1'

      expect(last_response.status).to eq(200)
      expect(JSON.parse(last_response.body)['displayName']).to eq('Alice')
    end

    it 'returns 404 when not found' do
      allow(get_user).to receive(:call).and_raise(ArgumentError, 'not found')

      get '/admin/users/missing'

      expect(last_response.status).to eq(404)
    end
  end

  describe 'PUT /admin/users/:id' do
    it 'calls update and returns 200' do
      allow(update_user).to receive(:call).with(user_id, 'new@example.com', 'Alice')

      put '/admin/users/u1', { email: 'new@example.com', displayName: 'Alice' }.to_json, 'CONTENT_TYPE' => 'application/json'

      expect(last_response.status).to eq(200)
    end

    it 'returns 404 when not found' do
      allow(update_user).to receive(:call).and_raise(ArgumentError, 'not found')

      put '/admin/users/missing', { email: 'x@x.com', displayName: 'X' }.to_json, 'CONTENT_TYPE' => 'application/json'

      expect(last_response.status).to eq(404)
    end
  end

  describe 'DELETE /admin/users/:id' do
    it 'calls delete and returns 200' do
      allow(delete_user).to receive(:call).with(user_id)

      delete '/admin/users/u1'

      expect(last_response.status).to eq(200)
    end
  end
end
