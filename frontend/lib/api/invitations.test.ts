import { describe, it, expect, vi } from "vitest";
import { listInvitations, createInvitation, acceptInvitation, declineInvitation } from "./invitations";

function mockFetcher(body: unknown, status = 200): typeof fetch {
  return vi.fn().mockResolvedValue({ ok: status < 400, status, json: async () => body });
}

describe("invitations api client", () => {
  it("listInvitations fetches with query params", async () => {
    const invitations = [{ id: "i1", invitingUserId: "u1", invitedUserId: "u2", status: "pending" }];
    const fetcher = mockFetcher(invitations);
    expect(await listInvitations("u1", "received", fetcher)).toEqual(invitations);
    expect(fetcher).toHaveBeenCalledWith("/api/invitations?userId=u1&direction=received", undefined);
  });

  it("createInvitation posts and returns id", async () => {
    const fetcher = mockFetcher({ id: "i1" });
    expect(await createInvitation("u1", "u2", fetcher)).toEqual({ id: "i1" });
    expect(fetcher).toHaveBeenCalledWith("/api/invitations", expect.objectContaining({ method: "POST" }));
  });

  it("acceptInvitation posts to accept endpoint", async () => {
    const fetcher = mockFetcher({ id: "g1" });
    expect(await acceptInvitation("i1", fetcher)).toEqual({ id: "g1" });
    expect(fetcher).toHaveBeenCalledWith("/api/invitations/i1/accept", expect.objectContaining({ method: "POST" }));
  });

  it("declineInvitation posts to decline endpoint", async () => {
    const fetcher = mockFetcher({}, 204);
    await declineInvitation("i1", fetcher);
    expect(fetcher).toHaveBeenCalledWith("/api/invitations/i1/decline", expect.objectContaining({ method: "POST" }));
  });
});
