require_relative 'invitation_repository'
require_relative 'invitation_status'

module Organizing
  class DeclineInvitation
    def initialize(invitation_repository)
      @invitation_repository = invitation_repository
    end

    def call(invitation_id)
      invitation = @invitation_repository.find_by_id(invitation_id)
      raise ArgumentError, "Invitation not found: #{invitation_id.value}" if invitation.nil?
      raise ArgumentError, "Invitation #{invitation_id.value} is not pending" unless invitation.status == InvitationStatus::PENDING

      @invitation_repository.save(invitation.with(status: InvitationStatus::DECLINED))
    end
  end
end
