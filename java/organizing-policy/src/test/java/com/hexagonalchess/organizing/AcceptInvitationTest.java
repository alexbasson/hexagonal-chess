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
class AcceptInvitationTest {

    @Mock private InvitationRepository invitationRepository;
    @Mock private UserNameProvider userNameProvider;
    @Mock private StartGame startGame;

    private AcceptInvitation acceptInvitation;

    @BeforeEach
    void setup() {
        acceptInvitation = new AcceptInvitation(invitationRepository, userNameProvider, startGame);
    }

    @Test
    void accept_invitation_starts_a_game_and_marks_accepted() {
        var invitationId = new InvitationId("inv-1");
        var invitation = new GameInvitation(invitationId, "user-1", "user-2", InvitationStatus.PENDING);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));
        when(userNameProvider.getDisplayName("user-1")).thenReturn("Alice");
        when(userNameProvider.getDisplayName("user-2")).thenReturn("Bob");
        when(startGame.execute("Alice", "Bob")).thenReturn(new GameId("game-1"));

        var gameId = acceptInvitation.execute(invitationId);

        assertThat(gameId).isEqualTo(new GameId("game-1"));
        var captor = ArgumentCaptor.forClass(GameInvitation.class);
        verify(invitationRepository).save(captor.capture());
        assertThat(captor.getValue().status()).isEqualTo(InvitationStatus.ACCEPTED);
    }

    @Test
    void accept_invitation_throws_when_not_found() {
        when(invitationRepository.findById(new InvitationId("missing"))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> acceptInvitation.execute(new InvitationId("missing")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void accept_invitation_throws_when_not_pending() {
        var invitationId = new InvitationId("inv-1");
        var invitation = new GameInvitation(invitationId, "user-1", "user-2", InvitationStatus.DECLINED);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        assertThatThrownBy(() -> acceptInvitation.execute(invitationId))
            .isInstanceOf(IllegalStateException.class);
    }
}
