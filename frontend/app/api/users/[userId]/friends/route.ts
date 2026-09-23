import { NextRequest, NextResponse } from "next/server";
import { getAdapter } from "@/lib/adapters";

const backendUrl = process.env.BACKEND_URL ?? "http://localhost:8080";

export async function GET(_req: Request, { params }: { params: Promise<{ userId: string }> }) {
  const { userId } = await params;
  const adapter = getAdapter();
  const res = await fetch(`${backendUrl}/users/${userId}/friends`);
  const data = await res.json();
  return NextResponse.json((data as unknown[]).map((f) => adapter.parseFriend(f as Record<string, unknown>)));
}

export async function POST(request: NextRequest, { params }: { params: Promise<{ userId: string }> }) {
  const { userId } = await params;
  const adapter = getAdapter();
  const { friendId } = await request.json();
  const body = adapter.buildAddFriendRequest(friendId);
  await fetch(`${backendUrl}/users/${userId}/friends`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  return new NextResponse(null, { status: 204 });
}
