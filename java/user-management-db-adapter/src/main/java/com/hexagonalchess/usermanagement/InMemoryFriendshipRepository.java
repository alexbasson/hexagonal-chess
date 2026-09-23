package com.hexagonalchess.usermanagement;

import java.util.ArrayList;
import java.util.List;

public class InMemoryFriendshipRepository implements FriendshipRepository {

    private final List<Friendship> store = new ArrayList<>();

    @Override
    public void save(Friendship friendship) {
        store.add(friendship);
    }

    @Override
    public List<Friendship> findByOwnerId(UserId ownerId) {
        return store.stream()
            .filter(f -> f.ownerId().equals(ownerId))
            .toList();
    }

    @Override
    public void deleteByOwnerAndFriend(UserId ownerId, UserId friendId) {
        store.removeIf(f -> f.ownerId().equals(ownerId) && f.friendId().equals(friendId));
    }
}
