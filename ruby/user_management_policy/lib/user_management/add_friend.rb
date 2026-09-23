require_relative 'friendship'
require_relative 'friendship_repository'

module UserManagement
  class AddFriend
    def initialize(friendship_repository)
      @friendship_repository = friendship_repository
    end

    def call(owner_id, friend_id)
      @friendship_repository.save(Friendship.new(owner_id: owner_id, friend_id: friend_id))
    end
  end
end
