require 'rack/test'
require 'json'
require 'organizing/invitation_id'
require 'organizing/invitation_status'
require 'organizing/game_invitation'
require 'organizing/game_id'
require 'organizing/create_invitation'
require 'organizing/accept_invitation'
require 'organizing/decline_invitation'
require 'organizing/list_invitations'
require 'organizing/invitations_routes'

RSpec.describe Organizing::InvitationsRoutes do
  include Rack::Test::Methods

  let(:create_invitation)  { instance_double(Organizing::CreateInvitation) }
  let(:accept_invitation)  { instance_double(Organizing::AcceptInvitation) }
  let(:decline_invitation) { instance_double(Organizing::DeclineInvitation) }
  let(:list_invitations)   { instance_double(Organizing::ListInvitations) }
  let(:app) { described_class.new(create_invitation, accept_invitation, decline_invitation, list_invitations) }

  let(:invitation_id) { Organizing::InvitationId.new(value: 'inv-1') }
  let(:game_id)       { Organizing::GameId.new(value: 'game-1') }
  let(:invitation) do
    Organizing::GameInvitation.new(
      id: invitation_id, inviting_user_id: 'u1', invited_user_id: 'u2',
      status: Organizing::InvitationStatus::PENDING
    )
  end

  describe 'POST /invitations' do
    it 'returns 201 with invitation id' do
      allow(create_invitation).to receive(:call).with('u1', 'u2').and_return(invitation_id)

      post '/invitations', { invitingUserId: 'u1', invitedUserId: 'u2' }.to_json, 'CONTENT_TYPE' => 'application/json'

      expect(last_response.status).to eq(201)
      expect(JSON.parse(last_response.body)['invitationId']).to eq('inv-1')
    end

    it 'returns 422 when not friends' do
      allow(create_invitation).to receive(:call).and_raise(ArgumentError, 'not friends')

      post '/invitations', { invitingUserId: 'u1', invitedUserId: 'u2' }.to_json, 'CONTENT_TYPE' => 'application/json'

      expect(last_response.status).to eq(422)
    end
  end

  describe 'POST /invitations/:id/accept' do
    it 'returns 200 with game id' do
      allow(accept_invitation).to receive(:call).with(invitation_id).and_return(game_id)

      post '/invitations/inv-1/accept'

      expect(last_response.status).to eq(200)
      expect(JSON.parse(last_response.body)['gameId']).to eq('game-1')
    end

    it 'returns 422 when not pending' do
      allow(accept_invitation).to receive(:call).and_raise(ArgumentError, 'not pending')

      post '/invitations/inv-1/accept'

      expect(last_response.status).to eq(422)
    end
  end

  describe 'POST /invitations/:id/decline' do
    it 'returns 200' do
      allow(decline_invitation).to receive(:call).with(invitation_id)

      post '/invitations/inv-1/decline'

      expect(last_response.status).to eq(200)
    end

    it 'returns 422 when not pending' do
      allow(decline_invitation).to receive(:call).and_raise(ArgumentError, 'not pending')

      post '/invitations/inv-1/decline'

      expect(last_response.status).to eq(422)
    end
  end

  describe 'GET /invitations' do
    it 'returns list of invitations' do
      allow(list_invitations).to receive(:call).with('u1', direction: :sent, status: nil).and_return([invitation])

      get '/invitations?user_id=u1&direction=sent'

      expect(last_response.status).to eq(200)
      body = JSON.parse(last_response.body)
      expect(body.first['id']).to eq('inv-1')
      expect(body.first['invitingUserId']).to eq('u1')
      expect(body.first['invitedUserId']).to eq('u2')
    end
  end
end
