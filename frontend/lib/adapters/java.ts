import type { Piece } from "./types";

type Raw = Record<string, unknown>;

export function parseBoard(raw: Raw) {
  const idObj = raw.id as Raw;
  return {
    id: (idObj.value ?? raw.id) as string,
    whitePlayerName: raw.whitePlayerName as string,
    blackPlayerName: raw.blackPlayerName as string,
    activeColor: (raw.activeColor as string).toLowerCase(),
    pieces: raw.pieces as Piece[],
  };
}

export function parseGame(raw: Raw) {
  const idObj = raw.id as Raw;
  const white = raw.white as Raw;
  const black = raw.black as Raw;
  return { id: (idObj.value ?? raw.id) as string, whiteName: white.name as string, blackName: black.name as string };
}

export function parseCreateGameResponse(raw: Raw) {
  return { id: raw.gameId as string };
}

export function buildCreateGameRequest(whiteName: string, blackName: string) {
  return { whiteName, blackName };
}

export function buildMakeMoveRequest(from: string, to: string) {
  return { from, to };
}

export function parseInvitation(raw: Raw) {
  const idObj = raw.id as Raw;
  return {
    id: (idObj.value ?? raw.id) as string,
    invitingUserId: raw.invitingUserId as string,
    invitedUserId: raw.invitedUserId as string,
    status: (raw.status as string).toLowerCase(),
  };
}

export function parseCreateInvitationResponse(raw: Raw) {
  return { id: raw.id as string };
}

export function buildCreateInvitationRequest(invitingUserId: string, invitedUserId: string) {
  return { invitingUserId, invitedUserId };
}

export function parseAcceptInvitationResponse(raw: Raw) {
  return { id: raw.gameId as string };
}

export function parseUser(raw: Raw) {
  const idObj = raw.id as Raw;
  const id = (idObj.value ?? raw.id) as string;
  return { id, email: raw.email as string, displayName: raw.displayName as string };
}

export function parseCreateUserResponse(raw: Raw) {
  return { id: raw.id as string };
}

export function buildCreateUserRequest(email: string, displayName: string) {
  return { email, displayName };
}

export function buildUpdateUserRequest(email: string, displayName: string) {
  return { email, displayName };
}

export function parseFriend(raw: Raw) {
  const friendIdObj = raw.friendId as Raw;
  const friendId = (friendIdObj.value ?? raw.friendId) as string;
  return { friendId };
}

export function buildAddFriendRequest(friendId: string) {
  return { friendId };
}

export function buildListInvitationsParams(userId: string, direction: string, status?: string | null): Record<string, string> {
  const params: Record<string, string> = { userId, direction };
  if (status) params.status = status;
  return params;
}
