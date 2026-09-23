require_relative 'user'
require_relative 'user_repository'

module UserManagement
  class UpdateUser
    def initialize(user_repository)
      @user_repository = user_repository
    end

    def call(user_id, email, display_name)
      existing = @user_repository.find_by_id(user_id)
      raise ArgumentError, "User not found: #{user_id.value}" if existing.nil?
      @user_repository.save(User.new(id: user_id, email: email, display_name: display_name))
    end
  end
end
