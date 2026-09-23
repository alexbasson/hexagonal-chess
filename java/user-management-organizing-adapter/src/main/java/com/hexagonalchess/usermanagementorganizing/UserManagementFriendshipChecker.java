package com.hexagonalchess.usermanagementorganizing;

import com.hexagonalchess.organizing.FriendshipChecker;
import com.hexagonalchess.usermanagement.FriendshipRepository;
import com.hexagonalchess.usermanagement.UserId;

public class UserManagementFriendshipChecker implements FriendshipChecker {

    private final FriendshipRepository friendshipRepository;

    public UserManagementFriendshipChecker(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public boolean areFriends(String ownerId, String friendId) {
        return friendshipRepository.findByOwnerId(new UserId(ownerId)).stream()
            .anyMatch(f -> f.friendId().equals(new UserId(friendId)));
    }
}
