require 'organizing/user_name_provider'
require 'user_management/user_id'

module UserManagementOrganizing
  class UserManagementUserNameProvider
    include Organizing::UserNameProvider

    def initialize(user_repository)
      @user_repository = user_repository
    end

    def get_display_name(user_id)
      user = @user_repository.find_by_id(UserManagement::UserId.new(value: user_id))
      raise ArgumentError, "User not found: #{user_id}" if user.nil?
      user.display_name
    end
  end
end
