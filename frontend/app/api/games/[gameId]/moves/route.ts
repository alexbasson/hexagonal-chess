import { NextRequest, NextResponse } from "next/server";
import { getAdapter } from "@/lib/adapters";

const backendUrl = process.env.BACKEND_URL ?? "http://localhost:8080";

export async function POST(request: NextRequest, { params }: { params: Promise<{ gameId: string }> }) {
  const { gameId } = await params;
  const adapter = getAdapter();
  const { from, to } = await request.json();
  const body = adapter.buildMakeMoveRequest(from, to);
  const res = await fetch(`${backendUrl}/games/${gameId}/moves`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!res.ok) return NextResponse.json({ error: "Illegal move" }, { status: res.status });
  const data = await res.json();
  return NextResponse.json(adapter.parseBoard(data as Record<string, unknown>));
}
