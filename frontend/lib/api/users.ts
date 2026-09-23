import type { User } from "@/lib/adapters";
import { fetchJson, type Fetcher } from "./fetch";

export async function listUsers(fetcher?: Fetcher): Promise<User[]> {
  return fetchJson("/api/admin/users", undefined, fetcher);
}

export async function createUser(email: string, displayName: string, fetcher?: Fetcher): Promise<{ id: string }> {
  return fetchJson("/api/admin/users", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, displayName }),
  }, fetcher);
}

export async function updateUser(id: string, email: string, displayName: string, fetcher?: Fetcher): Promise<void> {
  await fetchJson("/api/admin/users/" + id, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, displayName }),
  }, fetcher);
}

export async function deleteUser(id: string, fetcher?: Fetcher): Promise<void> {
  await fetchJson("/api/admin/users/" + id, { method: "DELETE" }, fetcher);
}
