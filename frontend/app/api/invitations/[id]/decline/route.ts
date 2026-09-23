import { NextResponse } from "next/server";

const backendUrl = process.env.BACKEND_URL ?? "http://localhost:8080";

export async function POST(_req: Request, { params }: { params: Promise<{ id: string }> }) {
  const { id } = await params;
  await fetch(`${backendUrl}/invitations/${id}/decline`, { method: "POST" });
  return new NextResponse(null, { status: 204 });
}
