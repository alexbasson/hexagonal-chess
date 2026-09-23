import type { Friend } from "@/lib/adapters";
import { fetchJson, type Fetcher } from "./fetch";

export async function listFriends(userId: string, fetcher?: Fetcher): Promise<Friend[]> {
  return fetchJson(`/api/users/${userId}/friends`, undefined, fetcher);
}

export async function addFriend(userId: string, friendId: string, fetcher?: Fetcher): Promise<void> {
  await fetchJson(`/api/users/${userId}/friends`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ friendId }),
  }, fetcher);
}

export async function removeFriend(userId: string, friendId: string, fetcher?: Fetcher): Promise<void> {
  await fetchJson(`/api/users/${userId}/friends/${friendId}`, { method: "DELETE" }, fetcher);
}
