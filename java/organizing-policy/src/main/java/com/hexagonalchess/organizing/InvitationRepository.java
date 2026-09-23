package com.hexagonalchess.organizing;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository {
    void save(GameInvitation invitation);
    Optional<GameInvitation> findById(InvitationId id);
    List<GameInvitation> findByInvitingUserId(String userId);
    List<GameInvitation> findByInvitedUserId(String userId);
}
