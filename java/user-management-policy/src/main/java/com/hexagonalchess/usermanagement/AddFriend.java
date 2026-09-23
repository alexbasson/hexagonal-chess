package com.hexagonalchess.usermanagement;

public class AddFriend {

    private final FriendshipRepository friendshipRepository;

    public AddFriend(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public void execute(UserId ownerId, UserId friendId) {
        friendshipRepository.save(new Friendship(ownerId, friendId));
    }
}
