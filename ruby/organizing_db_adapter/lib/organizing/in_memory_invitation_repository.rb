require 'organizing/invitation_repository'

module Organizing
  class InMemoryInvitationRepository
    include InvitationRepository

    def initialize
      @store = {}
    end

    def save(invitation)
      @store[invitation.id] = invitation
    end

    def find_by_id(invitation_id)
      @store[invitation_id]
    end

    def find_by_inviting_user_id(user_id)
      @store.values.select { |i| i.inviting_user_id == user_id }
    end

    def find_by_invited_user_id(user_id)
      @store.values.select { |i| i.invited_user_id == user_id }
    end
  end
end
