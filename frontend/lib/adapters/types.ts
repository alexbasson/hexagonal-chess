export type Piece = {
  square: { file: number; rank: number };
  piece: { type: string; color: string };
};

export type Board = {
  id: string;
  whitePlayerName: string;
  blackPlayerName: string;
  activeColor: string;
  pieces: Piece[];
};

export type Game = {
  id: string;
  whiteName: string;
  blackName: string;
};

export type Invitation = {
  id: string;
  invitingUserId: string;
  invitedUserId: string;
  status: string;
};

export type User = {
  id: string;
  email: string;
  displayName: string;
};

export type Friend = {
  friendId: string;
};

export type BackendAdapter = {
  parseBoard: (raw: Record<string, unknown>) => Board;
  parseGame: (raw: Record<string, unknown>) => Game;
  parseCreateGameResponse: (raw: Record<string, unknown>) => { id: string };
  buildCreateGameRequest: (whiteName: string, blackName: string) => Record<string, unknown>;
  buildMakeMoveRequest: (from: string, to: string) => Record<string, unknown>;
  parseInvitation: (raw: Record<string, unknown>) => Invitation;
  parseCreateInvitationResponse: (raw: Record<string, unknown>) => { id: string };
  buildCreateInvitationRequest: (invitingUserId: string, invitedUserId: string) => Record<string, unknown>;
  parseAcceptInvitationResponse: (raw: Record<string, unknown>) => { id: string };
  parseUser: (raw: Record<string, unknown>) => User;
  parseCreateUserResponse: (raw: Record<string, unknown>) => { id: string };
  buildCreateUserRequest: (email: string, displayName: string) => Record<string, unknown>;
  buildUpdateUserRequest: (email: string, displayName: string) => Record<string, unknown>;
  parseFriend: (raw: Record<string, unknown>) => Friend;
  buildAddFriendRequest: (friendId: string) => Record<string, unknown>;
};
