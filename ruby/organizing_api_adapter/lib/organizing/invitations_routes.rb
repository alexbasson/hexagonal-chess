require 'sinatra/base'
require 'json'
require 'organizing/invitation_id'
require 'organizing/invitation_status'

module Organizing
  class InvitationsRoutes < Sinatra::Base
    disable :protection
    set :host_authorization, { permitted_hosts: [] }

    def initialize(create_invitation, accept_invitation, decline_invitation, list_invitations)
      @create_invitation  = create_invitation
      @accept_invitation  = accept_invitation
      @decline_invitation = decline_invitation
      @list_invitations   = list_invitations
      super()
    end

    post '/invitations' do
      content_type :json
      body = JSON.parse(request.body.read)
      invitation_id = @create_invitation.call(body['invitingUserId'], body['invitedUserId'])
      status 201
      { invitationId: invitation_id.value }.to_json
    rescue ArgumentError => e
      status 422
      { error: e.message }.to_json
    end

    post '/invitations/:invitation_id/accept' do
      content_type :json
      inv_id  = InvitationId.new(value: params[:invitation_id])
      game_id = @accept_invitation.call(inv_id)
      { gameId: game_id.value }.to_json
    rescue ArgumentError => e
      status 422
      { error: e.message }.to_json
    end

    post '/invitations/:invitation_id/decline' do
      content_type :json
      inv_id = InvitationId.new(value: params[:invitation_id])
      @decline_invitation.call(inv_id)
      {}.to_json
    rescue ArgumentError => e
      status 422
      { error: e.message }.to_json
    end

    get '/invitations' do
      content_type :json
      direction    = params[:direction]&.to_sym || :sent
      status_param = params[:status]
      status_val   = status_param ? status_param.to_sym : nil
      invitations  = @list_invitations.call(params[:user_id], direction: direction, status: status_val)
      invitations.map { |i| invitation_to_h(i) }.to_json
    end

    private

    def invitation_to_h(invitation)
      {
        id: invitation.id.value,
        invitingUserId: invitation.inviting_user_id,
        invitedUserId: invitation.invited_user_id,
        status: invitation.status
      }
    end
  end
end
