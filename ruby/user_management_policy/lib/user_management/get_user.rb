require_relative 'user_repository'

module UserManagement
  class GetUser
    def initialize(user_repository)
      @user_repository = user_repository
    end

    def call(user_id)
      user = @user_repository.find_by_id(user_id)
      raise ArgumentError, "User not found: #{user_id.value}" if user.nil?
      user
    end
  end
end
