import type { Invitation } from "@/lib/adapters";
import { fetchJson, fetchVoid, type Fetcher } from "./fetch";

export async function listInvitations(
  userId: string,
  direction: "sent" | "received",
  fetcher?: Fetcher
): Promise<Invitation[]> {
  return fetchJson(`/api/invitations?userId=${userId}&direction=${direction}`, undefined, fetcher);
}

export async function createInvitation(
  invitingUserId: string,
  invitedUserId: string,
  fetcher?: Fetcher
): Promise<{ id: string }> {
  return fetchJson("/api/invitations", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ invitingUserId, invitedUserId }),
  }, fetcher);
}

export async function acceptInvitation(id: string, fetcher?: Fetcher): Promise<{ id: string }> {
  return fetchJson(`/api/invitations/${id}/accept`, { method: "POST" }, fetcher);
}

export async function declineInvitation(id: string, fetcher?: Fetcher): Promise<void> {
  await fetchVoid(`/api/invitations/${id}/decline`, { method: "POST" }, fetcher);
}
