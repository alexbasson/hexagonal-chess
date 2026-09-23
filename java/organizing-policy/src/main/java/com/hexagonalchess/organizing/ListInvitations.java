package com.hexagonalchess.organizing;

import java.util.List;
import java.util.stream.Stream;

public class ListInvitations {

    private final InvitationRepository invitationRepository;

    public ListInvitations(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public List<GameInvitation> execute(String userId, String direction, String status) {
        Stream<GameInvitation> invitations = direction.equals("sent")
            ? invitationRepository.findByInvitingUserId(userId).stream()
            : invitationRepository.findByInvitedUserId(userId).stream();

        if (status != null) {
            var filterStatus = InvitationStatus.valueOf(status.toUpperCase());
            invitations = invitations.filter(i -> i.status() == filterStatus);
        }
        return invitations.toList();
    }
}
