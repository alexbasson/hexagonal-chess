import { NextRequest, NextResponse } from "next/server";
import { getAdapter } from "@/lib/adapters";

const backendUrl = process.env.BACKEND_URL ?? "http://localhost:8080";

export async function GET(request: NextRequest) {
  const adapter = getAdapter();
  const { searchParams } = new URL(request.url);
  const userId = searchParams.get("userId");
  const direction = searchParams.get("direction");
  const status = searchParams.get("status");

  const query = new URLSearchParams();
  if (userId) query.set("user_id", userId);
  if (direction) query.set("direction", direction);
  if (status) query.set("status", status);

  const res = await fetch(`${backendUrl}/invitations?${query}`);
  const data = await res.json();
  return NextResponse.json((data as unknown[]).map((i) => adapter.parseInvitation(i as Record<string, unknown>)));
}

export async function POST(request: NextRequest) {
  const adapter = getAdapter();
  const { invitingUserId, invitedUserId } = await request.json();
  const body = adapter.buildCreateInvitationRequest(invitingUserId, invitedUserId);
  const res = await fetch(`${backendUrl}/invitations`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!res.ok) return NextResponse.json({ error: "Not friends" }, { status: res.status });
  const data = await res.json();
  return NextResponse.json(adapter.parseCreateInvitationResponse(data as Record<string, unknown>), { status: 201 });
}
