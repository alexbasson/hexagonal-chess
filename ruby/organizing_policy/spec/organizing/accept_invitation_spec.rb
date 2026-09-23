require 'organizing/game_id'
require 'organizing/invitation_id'
require 'organizing/invitation_status'
require 'organizing/game_invitation'
require 'organizing/invitation_repository'
require 'organizing/user_name_provider'
require 'organizing/start_game'
require 'organizing/accept_invitation'

RSpec.describe Organizing::AcceptInvitation do
  let(:invitation_repository) { instance_double(Organizing::InvitationRepository) }
  let(:user_name_provider)    { instance_double(Organizing::UserNameProvider) }
  let(:start_game)            { instance_double(Organizing::StartGame) }

  subject(:accept_invitation) { described_class.new(invitation_repository, user_name_provider, start_game) }

  let(:invitation_id) { Organizing::InvitationId.new(value: 'inv-1') }
  let(:game_id)       { Organizing::GameId.new(value: 'game-1') }
  let(:pending_inv)   do
    Organizing::GameInvitation.new(
      id: invitation_id, inviting_user_id: 'u1', invited_user_id: 'u2',
      status: Organizing::InvitationStatus::PENDING
    )
  end

  before do
    allow(invitation_repository).to receive(:find_by_id).with(invitation_id).and_return(pending_inv)
    allow(invitation_repository).to receive(:save)
    allow(user_name_provider).to receive(:get_display_name).with('u1').and_return('Alice')
    allow(user_name_provider).to receive(:get_display_name).with('u2').and_return('Bob')
    allow(start_game).to receive(:call).with('Alice', 'Bob').and_return(game_id)
  end

  describe '#call' do
    it 'starts a game with player names' do
      accept_invitation.call(invitation_id)

      expect(start_game).to have_received(:call).with('Alice', 'Bob')
    end

    it 'returns the game id' do
      expect(accept_invitation.call(invitation_id)).to eq(game_id)
    end

    it 'marks the invitation accepted' do
      accept_invitation.call(invitation_id)

      expect(invitation_repository).to have_received(:save) do |inv|
        expect(inv.status).to eq(Organizing::InvitationStatus::ACCEPTED)
      end
    end

    it 'raises when invitation not found' do
      allow(invitation_repository).to receive(:find_by_id).and_return(nil)

      expect { accept_invitation.call(invitation_id) }.to raise_error(ArgumentError)
    end

    it 'raises when invitation is not pending' do
      declined = pending_inv.with(status: Organizing::InvitationStatus::DECLINED)
      allow(invitation_repository).to receive(:find_by_id).and_return(declined)

      expect { accept_invitation.call(invitation_id) }.to raise_error(ArgumentError)
    end
  end
end
