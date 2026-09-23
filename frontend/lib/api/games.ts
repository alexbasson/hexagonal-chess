import type { Board, Game } from "@/lib/adapters";
import { fetchJson, type Fetcher } from "./fetch";

export async function getBoard(gameId: string, fetcher?: Fetcher): Promise<Board> {
  return fetchJson(`/api/games/${gameId}/board`, undefined, fetcher);
}

export async function listGames(fetcher?: Fetcher): Promise<Game[]> {
  return fetchJson("/api/games", undefined, fetcher);
}

export async function createGame(whiteName: string, blackName: string, fetcher?: Fetcher): Promise<{ id: string }> {
  return fetchJson("/api/games", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ whiteName, blackName }),
  }, fetcher);
}

export async function makeMove(gameId: string, from: string, to: string, fetcher?: Fetcher): Promise<Board> {
  return fetchJson(`/api/games/${gameId}/moves`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ from, to }),
  }, fetcher);
}
