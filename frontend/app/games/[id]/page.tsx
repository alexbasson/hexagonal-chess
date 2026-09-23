"use client";

import { useEffect, useState, useCallback } from "react";
import { useParams, useRouter } from "next/navigation";
import { getStoredUser } from "@/lib/user-store";
import { getBoard, makeMove } from "@/lib/api/games";
import { ChessGame } from "@/components/chess/ChessGame";
import { Button } from "@/components/ui/button";
import type { Board, User } from "@/lib/adapters";

const POLL_INTERVAL_MS = 2000;

export default function GamePage() {
  const params = useParams();
  const gameId = params.id as string;
  const router = useRouter();
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [board, setBoard] = useState<Board | null>(null);
  const [error, setError] = useState<string | null>(null);

  const fetchBoard = useCallback(async () => {
    try {
      const b = await getBoard(gameId);
      setBoard(b);
    } catch {
      setError("Failed to load board.");
    }
  }, [gameId]);

  useEffect(() => {
    const user = getStoredUser();
    if (!user) { router.replace("/"); return; }
    setCurrentUser(user);
    fetchBoard();
    const interval = setInterval(fetchBoard, POLL_INTERVAL_MS);
    return () => clearInterval(interval);
  }, [router, fetchBoard]);

  async function handleMove(from: string, to: string) {
    try {
      const updated = await makeMove(gameId, from, to);
      setBoard(updated);
    } catch {
      setError("Illegal move.");
      setTimeout(() => setError(null), 2000);
    }
  }

  if (!currentUser || !board) return <div className="p-8 text-muted-foreground">Loading…</div>;

  return (
    <main className="max-w-2xl mx-auto p-4 flex flex-col gap-4">
      <div className="flex items-center justify-between">
        <Button variant="outline" size="sm" onClick={() => router.push("/dashboard")}>← Dashboard</Button>
        {error && <p className="text-sm text-destructive">{error}</p>}
      </div>
      <ChessGame board={board} currentUserDisplayName={currentUser.displayName} onMove={handleMove} />
    </main>
  );
}
