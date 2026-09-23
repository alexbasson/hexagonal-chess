require_relative 'user_repository'

module UserManagement
  class ListUsers
    def initialize(user_repository)
      @user_repository = user_repository
    end

    def call
      @user_repository.find_all
    end
  end
end
