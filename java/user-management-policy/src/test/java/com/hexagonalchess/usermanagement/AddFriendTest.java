package com.hexagonalchess.usermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddFriendTest {

    @Mock private FriendshipRepository friendshipRepository;

    private AddFriend addFriend;

    @BeforeEach
    void setup() {
        addFriend = new AddFriend(friendshipRepository);
    }

    @Test
    void add_friend_saves_a_friendship() {
        var ownerId = new UserId("user-1");
        var friendId = new UserId("user-2");

        addFriend.execute(ownerId, friendId);

        var captor = ArgumentCaptor.forClass(Friendship.class);
        verify(friendshipRepository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.ownerId()).isEqualTo(ownerId);
        assertThat(saved.friendId()).isEqualTo(friendId);
    }
}
