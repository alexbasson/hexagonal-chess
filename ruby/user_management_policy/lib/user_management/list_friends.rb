require_relative 'friendship_repository'

module UserManagement
  class ListFriends
    def initialize(friendship_repository)
      @friendship_repository = friendship_repository
    end

    def call(owner_id)
      @friendship_repository.find_by_owner_id(owner_id)
    end
  end
end
