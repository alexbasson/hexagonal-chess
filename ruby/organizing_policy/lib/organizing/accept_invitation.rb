require_relative 'invitation_repository'
require_relative 'invitation_status'
require_relative 'user_name_provider'

module Organizing
  class AcceptInvitation
    def initialize(invitation_repository, user_name_provider, start_game)
      @invitation_repository = invitation_repository
      @user_name_provider    = user_name_provider
      @start_game            = start_game
    end

    def call(invitation_id)
      invitation = @invitation_repository.find_by_id(invitation_id)
      raise ArgumentError, "Invitation not found: #{invitation_id.value}" if invitation.nil?
      raise ArgumentError, "Invitation #{invitation_id.value} is not pending" unless invitation.status == InvitationStatus::PENDING

      white_name = @user_name_provider.get_display_name(invitation.inviting_user_id)
      black_name = @user_name_provider.get_display_name(invitation.invited_user_id)
      game_id    = @start_game.call(white_name, black_name)
      @invitation_repository.save(invitation.with(status: InvitationStatus::ACCEPTED))
      game_id
    end
  end
end
