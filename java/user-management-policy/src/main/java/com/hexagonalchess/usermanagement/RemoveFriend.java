package com.hexagonalchess.usermanagement;

public class RemoveFriend {

    private final FriendshipRepository friendshipRepository;

    public RemoveFriend(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public void execute(UserId ownerId, UserId friendId) {
        friendshipRepository.deleteByOwnerAndFriend(ownerId, friendId);
    }
}
