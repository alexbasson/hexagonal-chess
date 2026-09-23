require 'securerandom'
require_relative 'user_id'
require_relative 'user'
require_relative 'user_repository'

module UserManagement
  class CreateUser
    def initialize(user_repository)
      @user_repository = user_repository
    end

    def call(email, display_name)
      user_id = UserId.new(value: SecureRandom.uuid)
      @user_repository.save(User.new(id: user_id, email: email, display_name: display_name))
      user_id
    end
  end
end
