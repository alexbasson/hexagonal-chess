package com.hexagonalchess.usermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RemoveFriendTest {

    @Mock private FriendshipRepository friendshipRepository;

    private RemoveFriend removeFriend;

    @BeforeEach
    void setup() {
        removeFriend = new RemoveFriend(friendshipRepository);
    }

    @Test
    void remove_friend_deletes_the_friendship() {
        var ownerId = new UserId("user-1");
        var friendId = new UserId("user-2");

        removeFriend.execute(ownerId, friendId);

        verify(friendshipRepository).deleteByOwnerAndFriend(ownerId, friendId);
    }
}
