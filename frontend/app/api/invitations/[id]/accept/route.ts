import { NextResponse } from "next/server";
import { getAdapter } from "@/lib/adapters";

const backendUrl = process.env.BACKEND_URL ?? "http://localhost:8080";

export async function POST(_req: Request, { params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  const adapter = getAdapter();
  const res = await fetch(`${backendUrl}/invitations/${id}/accept`, { method: "POST" });
  if (!res.ok) return NextResponse.json({ error: "Cannot accept" }, { status: res.status });
  const data = await res.json();
  return NextResponse.json(adapter.parseAcceptInvitationResponse(data as Record<string, unknown>));
}
