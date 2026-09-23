package com.hexagonalchess.organizing;

import java.util.UUID;

public class CreateInvitation {

    private final InvitationRepository invitationRepository;
    private final FriendshipChecker friendshipChecker;

    public CreateInvitation(InvitationRepository invitationRepository, FriendshipChecker friendshipChecker) {
        this.invitationRepository = invitationRepository;
        this.friendshipChecker = friendshipChecker;
    }

    public InvitationId execute(String invitingUserId, String invitedUserId) {
        if (!friendshipChecker.areFriends(invitingUserId, invitedUserId)) {
            throw new IllegalArgumentException("Users are not friends");
        }
        var id = new InvitationId(UUID.randomUUID().toString());
        invitationRepository.save(new GameInvitation(id, invitingUserId, invitedUserId, InvitationStatus.PENDING));
        return id;
    }
}
