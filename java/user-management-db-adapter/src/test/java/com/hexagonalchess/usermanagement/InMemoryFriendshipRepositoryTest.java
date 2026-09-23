package com.hexagonalchess.usermanagement;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryFriendshipRepositoryTest {

    private final InMemoryFriendshipRepository repository = new InMemoryFriendshipRepository();

    @Test
    void findByOwnerId_returns_empty_when_no_friendships_saved() {
        assertThat(repository.findByOwnerId(new UserId("u1"))).isEmpty();
    }

    @Test
    void findByOwnerId_returns_saved_friendships_for_that_owner() {
        var ownerId = new UserId("u1");
        var otherId = new UserId("u2");
        repository.save(new Friendship(ownerId, new UserId("u2")));
        repository.save(new Friendship(otherId, new UserId("u3")));

        assertThat(repository.findByOwnerId(ownerId))
            .containsExactly(new Friendship(ownerId, new UserId("u2")));
    }

    @Test
    void deleteByOwnerAndFriend_removes_the_friendship() {
        var ownerId = new UserId("u1");
        var friendId = new UserId("u2");
        repository.save(new Friendship(ownerId, friendId));

        repository.deleteByOwnerAndFriend(ownerId, friendId);

        assertThat(repository.findByOwnerId(ownerId)).isEmpty();
    }

    @Test
    void deleteByOwnerAndFriend_only_removes_the_matching_friendship() {
        var ownerId = new UserId("u1");
        repository.save(new Friendship(ownerId, new UserId("u2")));
        repository.save(new Friendship(ownerId, new UserId("u3")));

        repository.deleteByOwnerAndFriend(ownerId, new UserId("u2"));

        assertThat(repository.findByOwnerId(ownerId))
            .containsExactly(new Friendship(ownerId, new UserId("u3")));
    }
}
