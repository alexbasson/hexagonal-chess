require_relative 'user_repository'

module UserManagement
  class DeleteUser
    def initialize(user_repository)
      @user_repository = user_repository
    end

    def call(user_id)
      @user_repository.delete(user_id)
    end
  end
end
