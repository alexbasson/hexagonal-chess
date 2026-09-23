import { NextRequest, NextResponse } from "next/server";
import { getAdapter } from "@/lib/adapters";

const backendUrl = process.env.BACKEND_URL ?? "http://localhost:8080";

export async function PUT(request: NextRequest, { params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  const adapter = getAdapter();
  const { email, displayName } = await request.json();
  const body = adapter.buildUpdateUserRequest(email, displayName);
  await fetch(`${backendUrl}/admin/users/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  return new NextResponse(null, { status: 204 });
}

export async function DELETE(_req: Request, { params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  await fetch(`${backendUrl}/admin/users/${id}`, { method: "DELETE" });
  return new NextResponse(null, { status: 204 });
}
