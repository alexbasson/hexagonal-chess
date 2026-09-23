package com.hexagonalchess.organizing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeclineInvitationTest {

    @Mock private InvitationRepository invitationRepository;

    private DeclineInvitation declineInvitation;

    @BeforeEach
    void setup() {
        declineInvitation = new DeclineInvitation(invitationRepository);
    }

    @Test
    void decline_invitation_marks_it_declined() {
        var invitationId = new InvitationId("inv-1");
        var invitation = new GameInvitation(invitationId, "user-1", "user-2", InvitationStatus.PENDING);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        declineInvitation.execute(invitationId);

        var captor = ArgumentCaptor.forClass(GameInvitation.class);
        verify(invitationRepository).save(captor.capture());
        assertThat(captor.getValue().status()).isEqualTo(InvitationStatus.DECLINED);
    }

    @Test
    void decline_invitation_throws_when_not_found() {
        when(invitationRepository.findById(new InvitationId("missing"))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> declineInvitation.execute(new InvitationId("missing")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void decline_invitation_throws_when_not_pending() {
        var invitationId = new InvitationId("inv-1");
        var invitation = new GameInvitation(invitationId, "user-1", "user-2", InvitationStatus.ACCEPTED);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        assertThatThrownBy(() -> declineInvitation.execute(invitationId))
            .isInstanceOf(IllegalStateException.class);
    }
}
