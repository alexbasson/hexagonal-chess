package com.hexagonalchess.usermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListFriendsTest {

    @Mock private FriendshipRepository friendshipRepository;

    private ListFriends listFriends;

    @BeforeEach
    void setup() {
        listFriends = new ListFriends(friendshipRepository);
    }

    @Test
    void list_friends_returns_friends_for_owner() {
        var ownerId = new UserId("user-1");
        var friendships = List.of(
            new Friendship(ownerId, new UserId("user-2")),
            new Friendship(ownerId, new UserId("user-3"))
        );
        when(friendshipRepository.findByOwnerId(ownerId)).thenReturn(friendships);

        var result = listFriends.execute(ownerId);

        assertThat(result).isEqualTo(friendships);
    }

    @Test
    void list_friends_returns_empty_when_no_friends() {
        var ownerId = new UserId("user-1");
        when(friendshipRepository.findByOwnerId(ownerId)).thenReturn(List.of());

        var result = listFriends.execute(ownerId);

        assertThat(result).isEmpty();
    }
}
