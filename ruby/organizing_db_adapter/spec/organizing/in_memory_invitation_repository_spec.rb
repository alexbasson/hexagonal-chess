require 'organizing/invitation_id'
require 'organizing/invitation_status'
require 'organizing/game_invitation'
require 'organizing/invitation_repository'
require 'organizing/in_memory_invitation_repository'

RSpec.describe Organizing::InMemoryInvitationRepository do
  subject(:repo) { described_class.new }

  let(:inv_id)     { Organizing::InvitationId.new(value: 'inv-1') }
  let(:invitation) do
    Organizing::GameInvitation.new(
      id: inv_id, inviting_user_id: 'u1', invited_user_id: 'u2',
      status: Organizing::InvitationStatus::PENDING
    )
  end

  it 'returns nil when not found' do
    expect(repo.find_by_id(inv_id)).to be_nil
  end

  it 'saves and finds by id' do
    repo.save(invitation)
    expect(repo.find_by_id(inv_id)).to eq(invitation)
  end

  it 'finds by inviting user id' do
    repo.save(invitation)
    expect(repo.find_by_inviting_user_id('u1')).to eq([invitation])
  end

  it 'finds by invited user id' do
    repo.save(invitation)
    expect(repo.find_by_invited_user_id('u2')).to eq([invitation])
  end

  it 'returns empty when no matching invitations' do
    expect(repo.find_by_inviting_user_id('u1')).to eq([])
  end

  it 'overwrites on save' do
    repo.save(invitation)
    accepted = invitation.with(status: Organizing::InvitationStatus::ACCEPTED)
    repo.save(accepted)
    expect(repo.find_by_id(inv_id)).to eq(accepted)
  end
end
