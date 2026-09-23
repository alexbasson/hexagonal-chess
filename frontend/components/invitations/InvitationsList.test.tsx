import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { InvitationsList } from "./InvitationsList";

const allUsers = [
  { id: "u1", email: "alice@example.com", displayName: "Alice" },
  { id: "u2", email: "bob@example.com", displayName: "Bob" },
];

const pending = [
  { id: "i1", invitingUserId: "u2", invitedUserId: "u1", status: "pending" },
];

describe("InvitationsList", () => {
  it("shows sender name for received invitations", () => {
    render(
      <InvitationsList
        invitations={pending}
        allUsers={allUsers}
        currentUserId="u1"
        direction="received"
        onAccept={vi.fn()}
        onDecline={vi.fn()}
      />
    );
    expect(screen.getByText(/bob/i)).toBeInTheDocument();
  });

  it("shows accept and decline buttons for received pending invitations", () => {
    render(
      <InvitationsList
        invitations={pending}
        allUsers={allUsers}
        currentUserId="u1"
        direction="received"
        onAccept={vi.fn()}
        onDecline={vi.fn()}
      />
    );
    expect(screen.getByRole("button", { name: /accept/i })).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /decline/i })).toBeInTheDocument();
  });

  it("calls onAccept with invitation id", async () => {
    const onAccept = vi.fn();
    render(
      <InvitationsList
        invitations={pending}
        allUsers={allUsers}
        currentUserId="u1"
        direction="received"
        onAccept={onAccept}
        onDecline={vi.fn()}
      />
    );
    await userEvent.click(screen.getByRole("button", { name: /accept/i }));
    expect(onAccept).toHaveBeenCalledWith("i1");
  });

  it("shows empty state when no invitations", () => {
    render(
      <InvitationsList
        invitations={[]}
        allUsers={allUsers}
        currentUserId="u1"
        direction="received"
        onAccept={vi.fn()}
        onDecline={vi.fn()}
      />
    );
    expect(screen.getByText(/no invitations/i)).toBeInTheDocument();
  });
});
