require 'organizing/invitation_id'
require 'organizing/invitation_status'
require 'organizing/game_invitation'
require 'organizing/invitation_repository'
require 'organizing/friendship_checker'
require 'organizing/create_invitation'

RSpec.describe Organizing::CreateInvitation do
  subject(:create_invitation) { described_class.new(friendship_checker, invitation_repository) }

  let(:friendship_checker)    { instance_double(Organizing::FriendshipChecker) }
  let(:invitation_repository) { instance_double(Organizing::InvitationRepository) }

  before do
    allow(friendship_checker).to receive(:are_friends?).and_return(true)
    allow(invitation_repository).to receive(:save)
  end

  describe '#call' do
    it 'saves a pending invitation' do
      create_invitation.call('u1', 'u2')

      expect(invitation_repository).to have_received(:save) do |inv|
        expect(inv.inviting_user_id).to eq('u1')
        expect(inv.invited_user_id).to eq('u2')
        expect(inv.status).to eq(Organizing::InvitationStatus::PENDING)
      end
    end

    it 'returns an invitation id' do
      result = create_invitation.call('u1', 'u2')

      expect(result).to be_a(Organizing::InvitationId)
    end

    it 'raises when not friends' do
      allow(friendship_checker).to receive(:are_friends?).and_return(false)

      expect { create_invitation.call('u1', 'u2') }.to raise_error(ArgumentError)
    end
  end
end
