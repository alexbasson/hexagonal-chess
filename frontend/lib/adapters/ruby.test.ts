import { describe, it, expect } from "vitest";
import {
  parseBoard,
  parseGame,
  parseCreateGameResponse,
  buildCreateGameRequest,
  buildMakeMoveRequest,
  parseInvitation,
  parseCreateInvitationResponse,
  buildCreateInvitationRequest,
  parseAcceptInvitationResponse,
  parseUser,
  parseCreateUserResponse,
  buildCreateUserRequest,
  buildUpdateUserRequest,
  parseFriend,
  buildAddFriendRequest,
  buildListInvitationsParams,
} from "./ruby";

describe("ruby adapter", () => {
  it("parseBoard normalizes snake_case fields", () => {
    expect(
      parseBoard({ id: "g1", white_player_name: "Alice", black_player_name: "Bob", active_color: "white", pieces: [] })
    ).toEqual({ id: "g1", whitePlayerName: "Alice", blackPlayerName: "Bob", activeColor: "white", pieces: [] });
  });

  it("parseGame maps nested names", () => {
    expect(parseGame({ id: "g1", white: { name: "Alice" }, black: { name: "Bob" } })).toEqual({
      id: "g1", whiteName: "Alice", blackName: "Bob",
    });
  });

  it("parseCreateGameResponse maps game_id to id", () => {
    expect(parseCreateGameResponse({ game_id: "g1" })).toEqual({ id: "g1" });
  });

  it("buildCreateGameRequest uses snake_case", () => {
    expect(buildCreateGameRequest("Alice", "Bob")).toEqual({ white_name: "Alice", black_name: "Bob" });
  });

  it("buildMakeMoveRequest uses from and to (not from_square)", () => {
    expect(buildMakeMoveRequest("e2", "e4")).toEqual({ from: "e2", to: "e4" });
  });

  it("parseInvitation maps snake_case fields", () => {
    expect(
      parseInvitation({ id: "i1", inviting_user_id: "u1", invited_user_id: "u2", status: "pending" })
    ).toEqual({ id: "i1", invitingUserId: "u1", invitedUserId: "u2", status: "pending" });
  });

  it("parseCreateInvitationResponse maps invitation_id to id", () => {
    expect(parseCreateInvitationResponse({ invitation_id: "i1" })).toEqual({ id: "i1" });
  });

  it("buildCreateInvitationRequest uses snake_case", () => {
    expect(buildCreateInvitationRequest("u1", "u2")).toEqual({ inviting_user_id: "u1", invited_user_id: "u2" });
  });

  it("parseAcceptInvitationResponse maps game_id to id", () => {
    expect(parseAcceptInvitationResponse({ game_id: "g1" })).toEqual({ id: "g1" });
  });

  it("parseUser maps snake_case fields", () => {
    expect(parseUser({ id: "u1", email: "a@b.com", display_name: "Alice" })).toEqual({
      id: "u1", email: "a@b.com", displayName: "Alice",
    });
  });

  it("parseCreateUserResponse maps user_id to id", () => {
    expect(parseCreateUserResponse({ user_id: "u1" })).toEqual({ id: "u1" });
  });

  it("buildCreateUserRequest uses snake_case", () => {
    expect(buildCreateUserRequest("a@b.com", "Alice")).toEqual({ email: "a@b.com", display_name: "Alice" });
  });

  it("buildUpdateUserRequest uses snake_case", () => {
    expect(buildUpdateUserRequest("a@b.com", "Alice")).toEqual({ email: "a@b.com", display_name: "Alice" });
  });

  it("parseFriend maps friend_id to friendId", () => {
    expect(parseFriend({ friend_id: "u2" })).toEqual({ friendId: "u2" });
  });

  it("buildAddFriendRequest uses snake_case", () => {
    expect(buildAddFriendRequest("u2")).toEqual({ friend_id: "u2" });
  });

  describe("buildListInvitationsParams", () => {
    it("uses snake_case user_id param", () => {
      expect(buildListInvitationsParams("u1", "received")).toEqual({ user_id: "u1", direction: "received" });
    });

    it("includes status when provided", () => {
      expect(buildListInvitationsParams("u1", "sent", "pending")).toEqual({ user_id: "u1", direction: "sent", status: "pending" });
    });
  });
});
