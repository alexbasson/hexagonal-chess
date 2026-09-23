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
} from "./python";

describe("python adapter", () => {
  describe("parseBoard", () => {
    it("normalizes snake_case fields to camelCase", () => {
      expect(
        parseBoard({
          id: "g1",
          white_player_name: "Alice",
          black_player_name: "Bob",
          active_color: "white",
          pieces: [],
        })
      ).toEqual({
        id: "g1",
        whitePlayerName: "Alice",
        blackPlayerName: "Bob",
        activeColor: "white",
        pieces: [],
      });
    });

    it("passes pieces through unchanged", () => {
      const piece = { square: { file: 5, rank: 2 }, piece: { type: "Pawn", color: "white" } };
      expect(parseBoard({ id: "g1", white_player_name: "A", black_player_name: "B", active_color: "white", pieces: [piece] }).pieces).toEqual([piece]);
    });
  });

  describe("parseGame", () => {
    it("maps game response fields", () => {
      expect(
        parseGame({ id: "g1", white: { name: "Alice" }, black: { name: "Bob" } })
      ).toEqual({ id: "g1", whiteName: "Alice", blackName: "Bob" });
    });
  });

  describe("parseCreateGameResponse", () => {
    it("maps game_id to id", () => {
      expect(parseCreateGameResponse({ game_id: "g1" })).toEqual({ id: "g1" });
    });
  });

  describe("buildCreateGameRequest", () => {
    it("uses snake_case field names", () => {
      expect(buildCreateGameRequest("Alice", "Bob")).toEqual({
        white_name: "Alice",
        black_name: "Bob",
      });
    });
  });

  describe("buildMakeMoveRequest", () => {
    it("uses from_square and to_square", () => {
      expect(buildMakeMoveRequest("e2", "e4")).toEqual({
        from_square: "e2",
        to_square: "e4",
      });
    });
  });

  describe("parseInvitation", () => {
    it("maps snake_case invitation fields", () => {
      expect(
        parseInvitation({
          id: "i1",
          inviting_user_id: "u1",
          invited_user_id: "u2",
          status: "pending",
        })
      ).toEqual({ id: "i1", invitingUserId: "u1", invitedUserId: "u2", status: "pending" });
    });
  });

  describe("parseCreateInvitationResponse", () => {
    it("maps invitation_id to id", () => {
      expect(parseCreateInvitationResponse({ invitation_id: "i1" })).toEqual({ id: "i1" });
    });
  });

  describe("buildCreateInvitationRequest", () => {
    it("uses snake_case field names", () => {
      expect(buildCreateInvitationRequest("u1", "u2")).toEqual({
        inviting_user_id: "u1",
        invited_user_id: "u2",
      });
    });
  });

  describe("parseAcceptInvitationResponse", () => {
    it("maps game_id to id", () => {
      expect(parseAcceptInvitationResponse({ game_id: "g1" })).toEqual({ id: "g1" });
    });
  });

  describe("parseUser", () => {
    it("maps snake_case user fields", () => {
      expect(
        parseUser({ id: "u1", email: "a@b.com", display_name: "Alice" })
      ).toEqual({ id: "u1", email: "a@b.com", displayName: "Alice" });
    });
  });

  describe("parseCreateUserResponse", () => {
    it("maps user_id to id", () => {
      expect(parseCreateUserResponse({ user_id: "u1" })).toEqual({ id: "u1" });
    });
  });

  describe("buildCreateUserRequest", () => {
    it("uses snake_case field names", () => {
      expect(buildCreateUserRequest("a@b.com", "Alice")).toEqual({
        email: "a@b.com",
        display_name: "Alice",
      });
    });
  });

  describe("buildUpdateUserRequest", () => {
    it("uses snake_case field names", () => {
      expect(buildUpdateUserRequest("a@b.com", "Alice")).toEqual({
        email: "a@b.com",
        display_name: "Alice",
      });
    });
  });

  describe("parseFriend", () => {
    it("maps friend_id to friendId", () => {
      expect(parseFriend({ friend_id: "u2" })).toEqual({ friendId: "u2" });
    });
  });

  describe("buildAddFriendRequest", () => {
    it("uses snake_case field names", () => {
      expect(buildAddFriendRequest("u2")).toEqual({ friend_id: "u2" });
    });
  });
});
