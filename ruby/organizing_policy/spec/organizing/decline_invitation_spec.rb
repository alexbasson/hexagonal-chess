require 'organizing/invitation_id'
require 'organizing/invitation_status'
require 'organizing/game_invitation'
require 'organizing/invitation_repository'
require 'organizing/decline_invitation'

RSpec.describe Organizing::DeclineInvitation do
  subject(:decline_invitation) { described_class.new(invitation_repository) }

  let(:invitation_repository) { instance_double(Organizing::InvitationRepository) }
  let(:invitation_id)         { Organizing::InvitationId.new(value: 'inv-1') }
  let(:pending_inv) do
    Organizing::GameInvitation.new(
      id: invitation_id, inviting_user_id: 'u1', invited_user_id: 'u2',
      status: Organizing::InvitationStatus::PENDING
    )
  end

  before do
    allow(invitation_repository).to receive(:find_by_id).with(invitation_id).and_return(pending_inv)
    allow(invitation_repository).to receive(:save)
  end

  describe '#call' do
    it 'marks the invitation declined' do
      decline_invitation.call(invitation_id)

      expect(invitation_repository).to have_received(:save) do |inv|
        expect(inv.status).to eq(Organizing::InvitationStatus::DECLINED)
      end
    end

    it 'raises when not found' do
      allow(invitation_repository).to receive(:find_by_id).and_return(nil)

      expect { decline_invitation.call(invitation_id) }.to raise_error(ArgumentError)
    end

    it 'raises when not pending' do
      accepted = pending_inv.with(status: Organizing::InvitationStatus::ACCEPTED)
      allow(invitation_repository).to receive(:find_by_id).and_return(accepted)

      expect { decline_invitation.call(invitation_id) }.to raise_error(ArgumentError)
    end
  end
end
