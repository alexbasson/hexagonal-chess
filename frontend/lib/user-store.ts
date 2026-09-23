import type { User } from "@/lib/adapters";

const KEY = "chess_current_user";

export function getStoredUser(): User | null {
  try {
    const raw = localStorage.getItem(KEY);
    return raw ? (JSON.parse(raw) as User) : null;
  } catch {
    return null;
  }
}

export function storeUser(user: User): void {
  try {
    localStorage.setItem(KEY, JSON.stringify(user));
  } catch {
    // ignore
  }
}

export function clearUser(): void {
  try {
    localStorage.removeItem(KEY);
  } catch {
    // ignore
  }
}
