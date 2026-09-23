module UserManagement
  module FriendshipRepository
    def save(friendship) = raise NotImplementedError
    def find_by_owner_id(owner_id) = raise NotImplementedError
    def delete_by_owner_and_friend(owner_id, friend_id) = raise NotImplementedError
  end
end
