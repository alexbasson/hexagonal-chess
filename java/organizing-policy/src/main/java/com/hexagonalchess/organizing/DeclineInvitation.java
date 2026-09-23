package com.hexagonalchess.organizing;

public class DeclineInvitation {

    private final InvitationRepository invitationRepository;

    public DeclineInvitation(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public void execute(InvitationId invitationId) {
        var invitation = invitationRepository.findById(invitationId)
            .orElseThrow(() -> new IllegalArgumentException("Invitation not found: " + invitationId.value()));
        if (invitation.status() != InvitationStatus.PENDING) {
            throw new IllegalStateException("Invitation is not pending");
        }
        invitationRepository.save(new GameInvitation(
            invitation.id(), invitation.invitingUserId(), invitation.invitedUserId(), InvitationStatus.DECLINED
        ));
    }
}
