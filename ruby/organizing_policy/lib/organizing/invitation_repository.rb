module Organizing
  module InvitationRepository
    def save(invitation) = raise NotImplementedError
    def find_by_id(invitation_id) = raise NotImplementedError
    def find_by_inviting_user_id(user_id) = raise NotImplementedError
    def find_by_invited_user_id(user_id) = raise NotImplementedError
  end
end
