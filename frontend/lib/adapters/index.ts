import type { BackendAdapter } from "./types";
import * as javaAdapter from "./java";
import * as pythonAdapter from "./python";
import * as rubyAdapter from "./ruby";

export type { BackendAdapter, Board, Game, Invitation, User, Friend, Piece } from "./types";

function detectBackend(backendUrl: string): BackendAdapter {
  if (backendUrl.includes(":8080")) return javaAdapter;
  if (backendUrl.includes(":8000")) return pythonAdapter;
  if (backendUrl.includes(":9292")) return rubyAdapter;
  return pythonAdapter;
}

export function getAdapter(): BackendAdapter {
  const url = process.env.BACKEND_URL ?? "http://localhost:8080";
  return detectBackend(url);
}
