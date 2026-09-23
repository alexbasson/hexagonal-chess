import { describe, it, expect, vi } from "vitest";
import { listUsers, createUser, updateUser, deleteUser } from "./users";

function mockFetcher(body: unknown, status = 200): typeof fetch {
  return vi.fn().mockResolvedValue({ ok: status < 400, status, json: async () => body });
}

describe("users api client", () => {
  it("listUsers fetches from proxy", async () => {
    const users = [{ id: "u1", email: "a@b.com", displayName: "Alice" }];
    const fetcher = mockFetcher(users);
    expect(await listUsers(fetcher)).toEqual(users);
    expect(fetcher).toHaveBeenCalledWith("/api/admin/users", undefined);
  });

  it("createUser posts and returns id", async () => {
    const fetcher = mockFetcher({ id: "u1" });
    expect(await createUser("a@b.com", "Alice", fetcher)).toEqual({ id: "u1" });
    expect(fetcher).toHaveBeenCalledWith("/api/admin/users", expect.objectContaining({ method: "POST" }));
  });

  it("updateUser puts to proxy", async () => {
    const fetcher = mockFetcher({}, 204);
    await updateUser("u1", "a@b.com", "Alice", fetcher);
    expect(fetcher).toHaveBeenCalledWith("/api/admin/users/u1", expect.objectContaining({ method: "PUT" }));
  });

  it("deleteUser deletes from proxy", async () => {
    const fetcher = mockFetcher({}, 204);
    await deleteUser("u1", fetcher);
    expect(fetcher).toHaveBeenCalledWith("/api/admin/users/u1", expect.objectContaining({ method: "DELETE" }));
  });
});
