import { NextResponse } from "next/server";

const backendUrl = process.env.BACKEND_URL ?? "http://localhost:8080";

export async function DELETE(
  _req: Request,
  { params }: { params: Promise<{ userId: string; friendId: string }> }
) {
  const { userId, friendId } = await params;
  await fetch(`${backendUrl}/users/${userId}/friends/${friendId}`, { method: "DELETE" });
  return new NextResponse(null, { status: 204 });
}
