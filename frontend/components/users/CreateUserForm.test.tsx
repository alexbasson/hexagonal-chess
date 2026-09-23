import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { CreateUserForm } from "./CreateUserForm";

describe("CreateUserForm", () => {
  it("renders email and display name fields", () => {
    render(<CreateUserForm onSubmit={vi.fn()} />);
    expect(screen.getByLabelText(/email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/display name/i)).toBeInTheDocument();
  });

  it("calls onSubmit with email and display name", async () => {
    const onSubmit = vi.fn();
    render(<CreateUserForm onSubmit={onSubmit} />);
    await userEvent.type(screen.getByLabelText(/email/i), "alice@example.com");
    await userEvent.type(screen.getByLabelText(/display name/i), "Alice");
    await userEvent.click(screen.getByRole("button", { name: /create/i }));
    expect(onSubmit).toHaveBeenCalledWith({ email: "alice@example.com", displayName: "Alice" });
  });
});
