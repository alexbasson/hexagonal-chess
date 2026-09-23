package com.hexagonalchess.organizing;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryInvitationRepository implements InvitationRepository {

    private final Map<InvitationId, GameInvitation> store = new LinkedHashMap<>();

    @Override
    public void save(GameInvitation invitation) {
        store.put(invitation.id(), invitation);
    }

    @Override
    public Optional<GameInvitation> findById(InvitationId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<GameInvitation> findByInvitingUserId(String userId) {
        return store.values().stream()
            .filter(i -> i.invitingUserId().equals(userId))
            .toList();
    }

    @Override
    public List<GameInvitation> findByInvitedUserId(String userId) {
        return store.values().stream()
            .filter(i -> i.invitedUserId().equals(userId))
            .toList();
    }
}
