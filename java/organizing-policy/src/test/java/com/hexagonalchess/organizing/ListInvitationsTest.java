package com.hexagonalchess.organizing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListInvitationsTest {

    @Mock private InvitationRepository invitationRepository;

    private ListInvitations listInvitations;

    @BeforeEach
    void setup() {
        listInvitations = new ListInvitations(invitationRepository);
    }

    @Test
    void list_sent_invitations_returns_invitations_by_inviting_user() {
        var inv = new GameInvitation(new InvitationId("inv-1"), "user-1", "user-2", InvitationStatus.PENDING);
        when(invitationRepository.findByInvitingUserId("user-1")).thenReturn(List.of(inv));

        var result = listInvitations.execute("user-1", "sent", null);

        assertThat(result).containsExactly(inv);
    }

    @Test
    void list_received_invitations_returns_invitations_by_invited_user() {
        var inv = new GameInvitation(new InvitationId("inv-1"), "user-2", "user-1", InvitationStatus.PENDING);
        when(invitationRepository.findByInvitedUserId("user-1")).thenReturn(List.of(inv));

        var result = listInvitations.execute("user-1", "received", null);

        assertThat(result).containsExactly(inv);
    }

    @Test
    void list_invitations_filters_by_status() {
        var pending = new GameInvitation(new InvitationId("inv-1"), "user-1", "user-2", InvitationStatus.PENDING);
        var accepted = new GameInvitation(new InvitationId("inv-2"), "user-1", "user-3", InvitationStatus.ACCEPTED);
        when(invitationRepository.findByInvitingUserId("user-1")).thenReturn(List.of(pending, accepted));

        var result = listInvitations.execute("user-1", "sent", "pending");

        assertThat(result).containsExactly(pending);
    }
}
