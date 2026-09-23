package com.hexagonalchess.usermanagementorganizing;

import com.hexagonalchess.usermanagement.Friendship;
import com.hexagonalchess.usermanagement.FriendshipRepository;
import com.hexagonalchess.usermanagement.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserManagementFriendshipCheckerTest {

    @Mock private FriendshipRepository friendshipRepository;

    private UserManagementFriendshipChecker checker;

    @BeforeEach
    void setup() {
        checker = new UserManagementFriendshipChecker(friendshipRepository);
    }

    @Test
    void are_friends_returns_true_when_friendship_exists() {
        when(friendshipRepository.findByOwnerId(new UserId("u1")))
            .thenReturn(List.of(new Friendship(new UserId("u1"), new UserId("u2"))));

        assertThat(checker.areFriends("u1", "u2")).isTrue();
    }

    @Test
    void are_friends_returns_false_when_no_friendship() {
        when(friendshipRepository.findByOwnerId(new UserId("u1"))).thenReturn(List.of());

        assertThat(checker.areFriends("u1", "u2")).isFalse();
    }
}
