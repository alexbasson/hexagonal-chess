import type { Piece } from "./types";

type Raw = Record<string, unknown>;

export function parseBoard(raw: Raw) {
  return {
    id: raw.id as string,
    whitePlayerName: raw.whitePlayerName as string,
    blackPlayerName: raw.blackPlayerName as string,
    activeColor: raw.activeColor as string,
    pieces: raw.pieces as Piece[],
  };
}

export function parseGame(raw: Raw) {
  const white = raw.white as Raw;
  const black = raw.black as Raw;
  return { id: raw.id as string, whiteName: white.name as string, blackName: black.name as string };
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
  return {
    id: raw.id as string,
    invitingUserId: raw.invitingUserId as string,
    invitedUserId: raw.invitedUserId as string,
    status: raw.status as string,
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
  return { id: raw.id as string, email: raw.email as string, displayName: raw.displayName as string };
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
  return { friendId: raw.friendId as string };
}

export function buildAddFriendRequest(friendId: string) {
  return { friendId };
}
