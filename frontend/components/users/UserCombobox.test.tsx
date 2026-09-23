import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { UserCombobox } from "./UserCombobox";

const users = [
  { id: "u1", email: "alice@example.com", displayName: "Alice" },
  { id: "u2", email: "bob@example.com", displayName: "Bob" },
  { id: "u3", email: "carol@example.com", displayName: "Carol" },
];

describe("UserCombobox", () => {
  it("renders a search input", () => {
    render(<UserCombobox users={users} onSelect={vi.fn()} placeholder="Search by email" />);
    expect(screen.getByPlaceholderText(/search by email/i)).toBeInTheDocument();
  });

  it("shows matching users when typing a partial email", async () => {
    render(<UserCombobox users={users} onSelect={vi.fn()} placeholder="Search by email" />);
    await userEvent.type(screen.getByRole("combobox"), "alice");
    expect(screen.getByText("alice@example.com")).toBeInTheDocument();
    expect(screen.queryByText("bob@example.com")).not.toBeInTheDocument();
  });

  it("calls onSelect with the chosen user", async () => {
    const onSelect = vi.fn();
    render(<UserCombobox users={users} onSelect={onSelect} placeholder="Search by email" />);
    await userEvent.type(screen.getByRole("combobox"), "alice");
    await userEvent.click(screen.getByText("alice@example.com"));
    expect(onSelect).toHaveBeenCalledWith(users[0]);
  });

  it("shows display name alongside email in results", async () => {
    render(<UserCombobox users={users} onSelect={vi.fn()} placeholder="Search by email" />);
    await userEvent.type(screen.getByRole("combobox"), "bob");
    expect(screen.getByText("Bob")).toBeInTheDocument();
    expect(screen.getByText("bob@example.com")).toBeInTheDocument();
  });
});
