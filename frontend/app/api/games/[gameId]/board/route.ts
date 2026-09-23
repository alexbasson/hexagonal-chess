import { NextResponse } from "next/server";
import { getAdapter } from "@/lib/adapters";

const backendUrl = process.env.BACKEND_URL ?? "http://localhost:8080";

export async function GET(_req: Request, { params }: { params: Promise<{ gameId: string }> }) {
  const { gameId } = await params;
  const adapter = getAdapter();
  const res = await fetch(`${backendUrl}/games/${gameId}/board`);
  if (!res.ok) return NextResponse.json({ error: "Not found" }, { status: res.status });
  const data = await res.json();
  return NextResponse.json(adapter.parseBoard(data as Record<string, unknown>));
}
