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
} from "./python";

describe("python adapter", () => {
  describe("parseBoard", () => {
    it("reads camelCase response fields", () => {
      expect(
        parseBoard({
          id: "g1",
          whitePlayerName: "Alice",
          blackPlayerName: "Bob",
          activeColor: "white",
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
      expect(parseBoard({ id: "g1", whitePlayerName: "A", blackPlayerName: "B", activeColor: "white", pieces: [piece] }).pieces).toEqual([piece]);
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
    it("maps gameId to id", () => {
      expect(parseCreateGameResponse({ gameId: "g1" })).toEqual({ id: "g1" });
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
    it("reads camelCase invitation fields", () => {
      expect(
        parseInvitation({
          id: "i1",
          invitingUserId: "u1",
          invitedUserId: "u2",
          status: "pending",
        })
      ).toEqual({ id: "i1", invitingUserId: "u1", invitedUserId: "u2", status: "pending" });
    });
  });

  describe("parseCreateInvitationResponse", () => {
    it("maps invitationId to id", () => {
      expect(parseCreateInvitationResponse({ invitationId: "i1" })).toEqual({ id: "i1" });
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
    it("maps gameId to id", () => {
      expect(parseAcceptInvitationResponse({ gameId: "g1" })).toEqual({ id: "g1" });
    });
  });

  describe("parseUser", () => {
    it("reads camelCase user fields", () => {
      expect(
        parseUser({ id: "u1", email: "a@b.com", displayName: "Alice" })
      ).toEqual({ id: "u1", email: "a@b.com", displayName: "Alice" });
    });
  });

  describe("parseCreateUserResponse", () => {
    it("maps userId to id", () => {
      expect(parseCreateUserResponse({ userId: "u1" })).toEqual({ id: "u1" });
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
    it("maps friendId field", () => {
      expect(parseFriend({ friendId: "u2" })).toEqual({ friendId: "u2" });
    });
  });

  describe("buildAddFriendRequest", () => {
    it("uses snake_case field names", () => {
      expect(buildAddFriendRequest("u2")).toEqual({ friend_id: "u2" });
    });
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
