import type { Piece } from "./types";

type Raw = Record<string, unknown>;

export function parseBoard(raw: Raw) {
  return {
    id: raw.id as string,
    whitePlayerName: raw.white_player_name as string,
    blackPlayerName: raw.black_player_name as string,
    activeColor: raw.active_color as string,
    pieces: raw.pieces as Piece[],
  };
}

export function parseGame(raw: Raw) {
  const white = raw.white as Raw;
  const black = raw.black as Raw;
  return { id: raw.id as string, whiteName: white.name as string, blackName: black.name as string };
}

export function parseCreateGameResponse(raw: Raw) {
  return { id: raw.game_id as string };
}

export function buildCreateGameRequest(whiteName: string, blackName: string) {
  return { white_name: whiteName, black_name: blackName };
}

export function buildMakeMoveRequest(from: string, to: string) {
  return { from_square: from, to_square: to };
}

export function parseInvitation(raw: Raw) {
  return {
    id: raw.id as string,
    invitingUserId: raw.inviting_user_id as string,
    invitedUserId: raw.invited_user_id as string,
    status: raw.status as string,
  };
}

export function parseCreateInvitationResponse(raw: Raw) {
  return { id: raw.invitation_id as string };
}

export function buildCreateInvitationRequest(invitingUserId: string, invitedUserId: string) {
  return { inviting_user_id: invitingUserId, invited_user_id: invitedUserId };
}

export function parseAcceptInvitationResponse(raw: Raw) {
  return { id: raw.game_id as string };
}

export function parseUser(raw: Raw) {
  return { id: raw.id as string, email: raw.email as string, displayName: raw.display_name as string };
}

export function parseCreateUserResponse(raw: Raw) {
  return { id: raw.user_id as string };
}

export function buildCreateUserRequest(email: string, displayName: string) {
  return { email, display_name: displayName };
}

export function buildUpdateUserRequest(email: string, displayName: string) {
  return { email, display_name: displayName };
}

export function parseFriend(raw: Raw) {
  return { friendId: raw.friend_id as string };
}

export function buildAddFriendRequest(friendId: string) {
  return { friend_id: friendId };
}

export function buildListInvitationsParams(userId: string, direction: string, status?: string | null): Record<string, string> {
  const params: Record<string, string> = { user_id: userId, direction };
  if (status) params.status = status;
  return params;
}
