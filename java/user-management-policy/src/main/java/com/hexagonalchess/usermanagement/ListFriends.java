package com.hexagonalchess.usermanagement;

import java.util.List;

public class ListFriends {

    private final FriendshipRepository friendshipRepository;

    public ListFriends(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public List<Friendship> execute(UserId ownerId) {
        return friendshipRepository.findByOwnerId(ownerId);
    }
}
