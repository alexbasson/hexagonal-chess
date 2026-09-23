require 'organizing/invitation_id'
require 'organizing/invitation_status'
require 'organizing/game_invitation'
require 'organizing/invitation_repository'
require 'organizing/list_invitations'

RSpec.describe Organizing::ListInvitations do
  subject(:list_invitations) { described_class.new(invitation_repository) }

  let(:invitation_repository) { instance_double(Organizing::InvitationRepository) }

  let(:sent) do
    Organizing::GameInvitation.new(
      id: Organizing::InvitationId.new(value: 'i1'), inviting_user_id: 'u1', invited_user_id: 'u2',
      status: Organizing::InvitationStatus::PENDING
    )
  end
  let(:received) do
    Organizing::GameInvitation.new(
      id: Organizing::InvitationId.new(value: 'i2'), inviting_user_id: 'u3', invited_user_id: 'u1',
      status: Organizing::InvitationStatus::PENDING
    )
  end
  let(:accepted) do
    Organizing::GameInvitation.new(
      id: Organizing::InvitationId.new(value: 'i3'), inviting_user_id: 'u1', invited_user_id: 'u4',
      status: Organizing::InvitationStatus::ACCEPTED
    )
  end

  describe '#call' do
    it 'returns sent invitations' do
      allow(invitation_repository).to receive(:find_by_inviting_user_id).with('u1').and_return([sent])

      expect(list_invitations.call('u1', direction: :sent)).to eq([sent])
    end

    it 'returns received invitations' do
      allow(invitation_repository).to receive(:find_by_invited_user_id).with('u1').and_return([received])

      expect(list_invitations.call('u1', direction: :received)).to eq([received])
    end

    it 'filters by status' do
      allow(invitation_repository).to receive(:find_by_inviting_user_id).with('u1').and_return([sent, accepted])

      result = list_invitations.call('u1', direction: :sent, status: Organizing::InvitationStatus::PENDING)

      expect(result).to eq([sent])
    end

    it 'returns all when no status filter' do
      allow(invitation_repository).to receive(:find_by_inviting_user_id).with('u1').and_return([sent, accepted])

      expect(list_invitations.call('u1', direction: :sent)).to eq([sent, accepted])
    end
  end
end
