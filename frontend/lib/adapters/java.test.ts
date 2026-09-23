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
} from "./java";

describe("java adapter", () => {
  describe("parseBoard", () => {
    it("passes camelCase board fields through", () => {
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
  });

  describe("parseGame", () => {
    it("maps nested white/black name to flat fields", () => {
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
    it("uses camelCase field names", () => {
      expect(buildCreateGameRequest("Alice", "Bob")).toEqual({
        whiteName: "Alice",
        blackName: "Bob",
      });
    });
  });

  describe("buildMakeMoveRequest", () => {
    it("uses from and to", () => {
      expect(buildMakeMoveRequest("e2", "e4")).toEqual({ from: "e2", to: "e4" });
    });
  });

  describe("parseInvitation", () => {
    it("passes camelCase invitation fields through", () => {
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
    it("maps id directly", () => {
      expect(parseCreateInvitationResponse({ id: "i1" })).toEqual({ id: "i1" });
    });
  });

  describe("buildCreateInvitationRequest", () => {
    it("uses camelCase field names", () => {
      expect(buildCreateInvitationRequest("u1", "u2")).toEqual({
        invitingUserId: "u1",
        invitedUserId: "u2",
      });
    });
  });

  describe("parseAcceptInvitationResponse", () => {
    it("maps gameId to id", () => {
      expect(parseAcceptInvitationResponse({ gameId: "g1" })).toEqual({ id: "g1" });
    });
  });

  describe("parseUser", () => {
    it("passes camelCase user fields through", () => {
      expect(
        parseUser({ id: "u1", email: "a@b.com", displayName: "Alice" })
      ).toEqual({ id: "u1", email: "a@b.com", displayName: "Alice" });
    });

    it("unwraps nested UserId object from Java serialization", () => {
      expect(
        parseUser({ id: { value: "u1" }, email: "a@b.com", displayName: "Alice" })
      ).toEqual({ id: "u1", email: "a@b.com", displayName: "Alice" });
    });
  });

  describe("parseCreateUserResponse", () => {
    it("maps id directly", () => {
      expect(parseCreateUserResponse({ id: "u1" })).toEqual({ id: "u1" });
    });
  });

  describe("buildCreateUserRequest", () => {
    it("uses camelCase field names", () => {
      expect(buildCreateUserRequest("a@b.com", "Alice")).toEqual({
        email: "a@b.com",
        displayName: "Alice",
      });
    });
  });

  describe("buildUpdateUserRequest", () => {
    it("uses camelCase field names", () => {
      expect(buildUpdateUserRequest("a@b.com", "Alice")).toEqual({
        email: "a@b.com",
        displayName: "Alice",
      });
    });
  });

  describe("parseFriend", () => {
    it("maps friendId", () => {
      expect(parseFriend({ friendId: "u2" })).toEqual({ friendId: "u2" });
    });

    it("unwraps nested UserId object from Java serialization", () => {
      expect(parseFriend({ friendId: { value: "u2" } })).toEqual({ friendId: "u2" });
    });
  });

  describe("buildAddFriendRequest", () => {
    it("uses camelCase field names", () => {
      expect(buildAddFriendRequest("u2")).toEqual({ friendId: "u2" });
    });
  });
});
