require 'user_management/friendship_repository'

module UserManagement
  class InMemoryFriendshipRepository
    include FriendshipRepository

    def initialize
      @store = []
    end

    def save(friendship)
      @store << friendship
    end

    def find_by_owner_id(owner_id)
      @store.select { |f| f.owner_id == owner_id }
    end

    def delete_by_owner_and_friend(owner_id, friend_id)
      @store.reject! { |f| f.owner_id == owner_id && f.friend_id == friend_id }
    end
  end
end
