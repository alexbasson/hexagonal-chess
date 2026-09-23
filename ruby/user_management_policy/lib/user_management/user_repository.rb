module UserManagement
  module UserRepository
    def save(user) = raise NotImplementedError
    def find_by_id(user_id) = raise NotImplementedError
    def find_all = raise NotImplementedError
    def delete(user_id) = raise NotImplementedError
  end
end
