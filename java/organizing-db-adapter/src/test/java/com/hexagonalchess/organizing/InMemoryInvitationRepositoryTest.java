package com.hexagonalchess.organizing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryInvitationRepositoryTest {

    private final InMemoryInvitationRepository repository = new InMemoryInvitationRepository();

    @Test
    void findById_returns_empty_when_no_invitation_saved() {
        assertThat(repository.findById(new InvitationId("unknown"))).isEmpty();
    }

    @Test
    void findById_returns_saved_invitation() {
        var inv = new GameInvitation(new InvitationId("inv-1"), "u1", "u2", InvitationStatus.PENDING);
        repository.save(inv);
        assertThat(repository.findById(new InvitationId("inv-1"))).contains(inv);
    }

    @Test
    void findByInvitingUserId_returns_matching_invitations() {
        var inv1 = new GameInvitation(new InvitationId("inv-1"), "u1", "u2", InvitationStatus.PENDING);
        var inv2 = new GameInvitation(new InvitationId("inv-2"), "u3", "u1", InvitationStatus.PENDING);
        repository.save(inv1);
        repository.save(inv2);
        assertThat(repository.findByInvitingUserId("u1")).containsExactly(inv1);
    }

    @Test
    void findByInvitedUserId_returns_matching_invitations() {
        var inv1 = new GameInvitation(new InvitationId("inv-1"), "u1", "u2", InvitationStatus.PENDING);
        var inv2 = new GameInvitation(new InvitationId("inv-2"), "u3", "u1", InvitationStatus.PENDING);
        repository.save(inv1);
        repository.save(inv2);
        assertThat(repository.findByInvitedUserId("u1")).containsExactly(inv2);
    }

    @Test
    void save_updates_existing_invitation() {
        var id = new InvitationId("inv-1");
        repository.save(new GameInvitation(id, "u1", "u2", InvitationStatus.PENDING));
        repository.save(new GameInvitation(id, "u1", "u2", InvitationStatus.ACCEPTED));
        assertThat(repository.findById(id).get().status()).isEqualTo(InvitationStatus.ACCEPTED);
    }
}
