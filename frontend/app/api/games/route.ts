import { NextRequest, NextResponse } from "next/server";
import { getAdapter } from "@/lib/adapters";

const backendUrl = process.env.BACKEND_URL ?? "http://localhost:8080";
const adapter = getAdapter();

export async function GET() {
  const res = await fetch(`${backendUrl}/games`);
  if (!res.ok) return NextResponse.json([]);
  const data = await res.json();
  const games = (data as unknown[]).map((g) => adapter.parseGame(g as Record<string, unknown>));
  return NextResponse.json(games);
}

export async function POST(request: NextRequest) {
  const { whiteName, blackName } = await request.json();
  const body = adapter.buildCreateGameRequest(whiteName, blackName);
  const res = await fetch(`${backendUrl}/games`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return NextResponse.json(adapter.parseCreateGameResponse(data as Record<string, unknown>), { status: 201 });
}
