import { describe, it, expect, vi } from "vitest";
import { getBoard, listGames, createGame, makeMove } from "./games";

function mockFetcher(body: unknown): typeof fetch {
  return vi.fn().mockResolvedValue({ ok: true, json: async () => body });
}

describe("games api client", () => {
  describe("getBoard", () => {
    it("fetches from proxy route", async () => {
      const board = { id: "g1", whitePlayerName: "Alice", blackPlayerName: "Bob", activeColor: "white", pieces: [] };
      const fetcher = mockFetcher(board);

      const result = await getBoard("g1", fetcher);

      expect(fetcher).toHaveBeenCalledWith("/api/games/g1/board", undefined);
      expect(result).toEqual(board);
    });
  });

  describe("listGames", () => {
    it("fetches from proxy route", async () => {
      const games = [{ id: "g1", whiteName: "Alice", blackName: "Bob" }];
      const fetcher = mockFetcher(games);

      const result = await listGames(fetcher);

      expect(fetcher).toHaveBeenCalledWith("/api/games", undefined);
      expect(result).toEqual(games);
    });
  });

  describe("createGame", () => {
    it("posts to proxy route and returns game id", async () => {
      const fetcher = mockFetcher({ id: "g1" });

      const result = await createGame("Alice", "Bob", fetcher);

      expect(fetcher).toHaveBeenCalledWith("/api/games", expect.objectContaining({ method: "POST" }));
      expect(result).toEqual({ id: "g1" });
    });
  });

  describe("makeMove", () => {
    it("posts move to proxy route and returns updated board", async () => {
      const board = { id: "g1", whitePlayerName: "Alice", blackPlayerName: "Bob", activeColor: "black", pieces: [] };
      const fetcher = mockFetcher(board);

      const result = await makeMove("g1", "e2", "e4", fetcher);

      expect(fetcher).toHaveBeenCalledWith("/api/games/g1/moves", expect.objectContaining({ method: "POST" }));
      expect(result).toEqual(board);
    });
  });
});
