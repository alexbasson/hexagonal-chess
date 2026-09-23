require 'organizing/friendship_checker'
require 'user_management/user_id'

module UserManagementOrganizing
  class UserManagementFriendshipChecker
    include Organizing::FriendshipChecker

    def initialize(friendship_repository)
      @friendship_repository = friendship_repository
    end

    def are_friends?(owner_id, friend_id)
      friendships = @friendship_repository.find_by_owner_id(UserManagement::UserId.new(value: owner_id))
      friendships.any? { |f| f.friend_id == UserManagement::UserId.new(value: friend_id) }
    end
  end
end
