import { describe, it, expect, vi } from "vitest";
import { listFriends, addFriend, removeFriend } from "./friends";

function mockFetcher(body: unknown, status = 200): typeof fetch {
  return vi.fn().mockResolvedValue({ ok: status < 400, status, json: async () => body });
}

describe("friends api client", () => {
  it("listFriends fetches from proxy", async () => {
    const friends = [{ friendId: "u2" }];
    const fetcher = mockFetcher(friends);
    expect(await listFriends("u1", fetcher)).toEqual(friends);
    expect(fetcher).toHaveBeenCalledWith("/api/users/u1/friends", undefined);
  });

  it("addFriend posts to proxy", async () => {
    const fetcher = mockFetcher({}, 204);
    await addFriend("u1", "u2", fetcher);
    expect(fetcher).toHaveBeenCalledWith("/api/users/u1/friends", expect.objectContaining({ method: "POST" }));
  });

  it("removeFriend deletes from proxy", async () => {
    const fetcher = mockFetcher({}, 204);
    await removeFriend("u1", "u2", fetcher);
    expect(fetcher).toHaveBeenCalledWith("/api/users/u1/friends/u2", expect.objectContaining({ method: "DELETE" }));
  });
});
