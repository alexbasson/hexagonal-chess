package com.hexagonalchess.usermanagement;

import java.util.List;

public interface FriendshipRepository {
    void save(Friendship friendship);
    List<Friendship> findByOwnerId(UserId ownerId);
    void deleteByOwnerAndFriend(UserId ownerId, UserId friendId);
}
