require_relative 'friendship_repository'

module UserManagement
  class RemoveFriend
    def initialize(friendship_repository)
      @friendship_repository = friendship_repository
    end

    def call(owner_id, friend_id)
      @friendship_repository.delete_by_owner_and_friend(owner_id, friend_id)
    end
  end
end
