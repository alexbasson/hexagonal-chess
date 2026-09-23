require 'sinatra/base'
require 'json'
require 'user_management/user_id'

module UserManagement
  class UsersRoutes < Sinatra::Base
    disable :protection
    set :host_authorization, { permitted_hosts: [] }

    def initialize(create_user, get_user, update_user, delete_user, list_users)
      @create_user = create_user
      @get_user    = get_user
      @update_user = update_user
      @delete_user = delete_user
      @list_users  = list_users
      super()
    end

    post '/admin/users' do
      content_type :json
      body = JSON.parse(request.body.read)
      user_id = @create_user.call(body['email'], body['displayName'])
      status 201
      { userId: user_id.value }.to_json
    end

    get '/admin/users' do
      content_type :json
      @list_users.call.map { |u| user_to_h(u) }.to_json
    end

    get '/admin/users/:user_id' do
      content_type :json
      user = @get_user.call(UserId.new(value: params[:user_id]))
      user_to_h(user).to_json
    rescue ArgumentError
      status 404
    end

    put '/admin/users/:user_id' do
      content_type :json
      body = JSON.parse(request.body.read)
      @update_user.call(UserId.new(value: params[:user_id]), body['email'], body['displayName'])
      {}.to_json
    rescue ArgumentError
      status 404
    end

    delete '/admin/users/:user_id' do
      content_type :json
      @delete_user.call(UserId.new(value: params[:user_id]))
      {}.to_json
    end

    private

    def user_to_h(user)
      { id: user.id.value, email: user.email, displayName: user.display_name }
    end
  end
end
