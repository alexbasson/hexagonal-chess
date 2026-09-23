require 'securerandom'
require_relative 'invitation_id'
require_relative 'invitation_status'
require_relative 'game_invitation'
require_relative 'invitation_repository'
require_relative 'friendship_checker'

module Organizing
  class CreateInvitation
    def initialize(friendship_checker, invitation_repository)
      @friendship_checker    = friendship_checker
      @invitation_repository = invitation_repository
    end

    def call(inviting_user_id, invited_user_id)
      unless @friendship_checker.are_friends?(inviting_user_id, invited_user_id)
        raise ArgumentError, "#{inviting_user_id} and #{invited_user_id} are not friends"
      end
      invitation_id = InvitationId.new(value: SecureRandom.uuid)
      @invitation_repository.save(GameInvitation.new(
        id: invitation_id,
        inviting_user_id: inviting_user_id,
        invited_user_id: invited_user_id,
        status: InvitationStatus::PENDING
      ))
      invitation_id
    end
  end
end
