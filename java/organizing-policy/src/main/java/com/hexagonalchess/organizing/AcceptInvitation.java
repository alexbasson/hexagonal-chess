package com.hexagonalchess.organizing;

public class AcceptInvitation {

    private final InvitationRepository invitationRepository;
    private final UserNameProvider userNameProvider;
    private final StartGame startGame;

    public AcceptInvitation(InvitationRepository invitationRepository,
                            UserNameProvider userNameProvider,
                            StartGame startGame) {
        this.invitationRepository = invitationRepository;
        this.userNameProvider = userNameProvider;
        this.startGame = startGame;
    }

    public GameId execute(InvitationId invitationId) {
        var invitation = invitationRepository.findById(invitationId)
            .orElseThrow(() -> new IllegalArgumentException("Invitation not found: " + invitationId.value()));
        if (invitation.status() != InvitationStatus.PENDING) {
            throw new IllegalStateException("Invitation is not pending");
        }
        var invitingName = userNameProvider.getDisplayName(invitation.invitingUserId());
        var invitedName = userNameProvider.getDisplayName(invitation.invitedUserId());
        var gameId = startGame.execute(invitingName, invitedName);
        invitationRepository.save(new GameInvitation(
            invitation.id(), invitation.invitingUserId(), invitation.invitedUserId(), InvitationStatus.ACCEPTED
        ));
        return gameId;
    }
}
