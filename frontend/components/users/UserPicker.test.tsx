import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { UserPicker } from "./UserPicker";

const users = [
  { id: "u1", email: "alice@example.com", displayName: "Alice" },
  { id: "u2", email: "bob@example.com", displayName: "Bob" },
];

describe("UserPicker", () => {
  it("renders each user's display name", () => {
    render(<UserPicker users={users} onSelect={vi.fn()} />);
    expect(screen.getByText("Alice")).toBeInTheDocument();
    expect(screen.getByText("Bob")).toBeInTheDocument();
  });

  it("calls onSelect with the chosen user when clicked", async () => {
    const onSelect = vi.fn();
    render(<UserPicker users={users} onSelect={onSelect} />);
    await userEvent.click(screen.getByText("Alice"));
    expect(onSelect).toHaveBeenCalledWith(users[0]);
  });

  it("renders a create new user option", () => {
    render(<UserPicker users={users} onSelect={vi.fn()} />);
    expect(screen.getByRole("button", { name: /create new user/i })).toBeInTheDocument();
  });
});
