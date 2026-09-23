package com.hexagonalchess.organizing;

public record GameInvitation(InvitationId id, String invitingUserId, String invitedUserId, InvitationStatus status) {}
