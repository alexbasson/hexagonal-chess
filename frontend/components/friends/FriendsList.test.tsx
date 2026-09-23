import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { FriendsList } from "./FriendsList";

const users = [
  { id: "u2", email: "bob@example.com", displayName: "Bob" },
  { id: "u3", email: "carol@example.com", displayName: "Carol" },
];

describe("FriendsList", () => {
  it("renders each friend's display name", () => {
    render(<FriendsList friendIds={["u2"]} allUsers={users} onRemove={vi.fn()} />);
    expect(screen.getByText("Bob")).toBeInTheDocument();
  });

  it("calls onRemove with the friend id when remove is clicked", async () => {
    const onRemove = vi.fn();
    render(<FriendsList friendIds={["u2"]} allUsers={users} onRemove={onRemove} />);
    await userEvent.click(screen.getByRole("button", { name: /remove/i }));
    expect(onRemove).toHaveBeenCalledWith("u2");
  });

  it("shows empty state when no friends", () => {
    render(<FriendsList friendIds={[]} allUsers={users} onRemove={vi.fn()} />);
    expect(screen.getByText(/no friends yet/i)).toBeInTheDocument();
  });
});
