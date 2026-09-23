require_relative 'invitation_repository'

module Organizing
  class ListInvitations
    def initialize(invitation_repository)
      @invitation_repository = invitation_repository
    end

    def call(user_id, direction:, status: nil)
      invitations = if direction == :sent
                      @invitation_repository.find_by_inviting_user_id(user_id)
                    else
                      @invitation_repository.find_by_invited_user_id(user_id)
                    end
      status ? invitations.select { |i| i.status == status } : invitations
    end
  end
end
