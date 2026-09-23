import { NextRequest, NextResponse } from "next/server";
import { getAdapter } from "@/lib/adapters";

const backendUrl = process.env.BACKEND_URL ?? "http://localhost:8080";

export async function GET() {
  const adapter = getAdapter();
  const res = await fetch(`${backendUrl}/admin/users`);
  const data = await res.json();
  return NextResponse.json((data as unknown[]).map((u) => adapter.parseUser(u as Record<string, unknown>)));
}

export async function POST(request: NextRequest) {
  const adapter = getAdapter();
  const { email, displayName } = await request.json();
  const body = adapter.buildCreateUserRequest(email, displayName);
  const res = await fetch(`${backendUrl}/admin/users`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return NextResponse.json(adapter.parseCreateUserResponse(data as Record<string, unknown>), { status: 201 });
}
