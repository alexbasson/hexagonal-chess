package com.hexagonalchess.organizing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateInvitationTest {

    @Mock private InvitationRepository invitationRepository;
    @Mock private FriendshipChecker friendshipChecker;

    private CreateInvitation createInvitation;

    @BeforeEach
    void setup() {
        createInvitation = new CreateInvitation(invitationRepository, friendshipChecker);
    }

    @Test
    void create_invitation_saves_a_pending_invitation() {
        when(friendshipChecker.areFriends("user-1", "user-2")).thenReturn(true);

        var invitationId = createInvitation.execute("user-1", "user-2");

        var captor = ArgumentCaptor.forClass(GameInvitation.class);
        verify(invitationRepository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.id()).isEqualTo(invitationId);
        assertThat(saved.invitingUserId()).isEqualTo("user-1");
        assertThat(saved.invitedUserId()).isEqualTo("user-2");
        assertThat(saved.status()).isEqualTo(InvitationStatus.PENDING);
    }

    @Test
    void create_invitation_throws_when_not_friends() {
        when(friendshipChecker.areFriends("user-1", "user-2")).thenReturn(false);

        assertThatThrownBy(() -> createInvitation.execute("user-1", "user-2"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
