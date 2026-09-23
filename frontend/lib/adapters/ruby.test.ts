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
  it("parseBoard reads snake_case response fields", () => {
    expect(
      parseBoard({ id: "g1", white_player_name: "Alice", black_player_name: "Bob", active_color: "white", pieces: [] })
    ).toEqual({ id: "g1", whitePlayerName: "Alice", blackPlayerName: "Bob", activeColor: "white", pieces: [] });
  });

  it("parseGame maps nested names", () => {
    expect(parseGame({ id: "g1", white: { name: "Alice" }, black: { name: "Bob" } })).toEqual({
      id: "g1", whiteName: "Alice", blackName: "Bob",
    });
  });

  it("parseCreateGameResponse maps gameId to id", () => {
    expect(parseCreateGameResponse({ gameId: "g1" })).toEqual({ id: "g1" });
  });

  it("buildCreateGameRequest uses camelCase (Ruby body reads whiteName/blackName)", () => {
    expect(buildCreateGameRequest("Alice", "Bob")).toEqual({ whiteName: "Alice", blackName: "Bob" });
  });

  it("buildMakeMoveRequest uses from and to", () => {
    expect(buildMakeMoveRequest("e2", "e4")).toEqual({ from: "e2", to: "e4" });
  });

  it("parseInvitation reads camelCase fields", () => {
    expect(
      parseInvitation({ id: "i1", invitingUserId: "u1", invitedUserId: "u2", status: "pending" })
    ).toEqual({ id: "i1", invitingUserId: "u1", invitedUserId: "u2", status: "pending" });
  });

  it("parseCreateInvitationResponse maps invitationId to id", () => {
    expect(parseCreateInvitationResponse({ invitationId: "i1" })).toEqual({ id: "i1" });
  });

  it("buildCreateInvitationRequest uses camelCase (Ruby body reads invitingUserId/invitedUserId)", () => {
    expect(buildCreateInvitationRequest("u1", "u2")).toEqual({ invitingUserId: "u1", invitedUserId: "u2" });
  });

  it("parseAcceptInvitationResponse maps gameId to id", () => {
    expect(parseAcceptInvitationResponse({ gameId: "g1" })).toEqual({ id: "g1" });
  });

  it("parseUser reads camelCase displayName field", () => {
    expect(parseUser({ id: "u1", email: "a@b.com", displayName: "Alice" })).toEqual({
      id: "u1", email: "a@b.com", displayName: "Alice",
    });
  });

  it("parseCreateUserResponse maps userId to id", () => {
    expect(parseCreateUserResponse({ userId: "u1" })).toEqual({ id: "u1" });
  });

  it("buildCreateUserRequest uses camelCase (Ruby body reads displayName)", () => {
    expect(buildCreateUserRequest("a@b.com", "Alice")).toEqual({ email: "a@b.com", displayName: "Alice" });
  });

  it("buildUpdateUserRequest uses camelCase (Ruby body reads displayName)", () => {
    expect(buildUpdateUserRequest("a@b.com", "Alice")).toEqual({ email: "a@b.com", displayName: "Alice" });
  });

  it("parseFriend reads camelCase friendId field", () => {
    expect(parseFriend({ friendId: "u2" })).toEqual({ friendId: "u2" });
  });

  it("buildAddFriendRequest uses camelCase (Ruby body reads friendId)", () => {
    expect(buildAddFriendRequest("u2")).toEqual({ friendId: "u2" });
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
