require 'user_management/user_repository'

module UserManagement
  class InMemoryUserRepository
    include UserRepository

    def initialize
      @store = {}
    end

    def save(user)
      @store[user.id] = user
    end

    def find_by_id(user_id)
      @store[user_id]
    end

    def find_all
      @store.values
    end

    def delete(user_id)
      @store.delete(user_id)
    end
  end
end
