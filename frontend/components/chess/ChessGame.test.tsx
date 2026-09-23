import { describe, it, expect, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import { ChessGame } from "./ChessGame";

vi.mock("react-chessboard", () => ({
  Chessboard: ({ options }: { options: { boardOrientation: string } }) => (
    <div data-testid="chessboard" data-orientation={options?.boardOrientation} />
  ),
}));

const board = {
  id: "g1",
  whitePlayerName: "Alice",
  blackPlayerName: "Bob",
  activeColor: "white",
  pieces: [],
};

describe("ChessGame", () => {
  it("renders the chessboard", () => {
    render(<ChessGame board={board} currentUserDisplayName="Alice" onMove={vi.fn()} />);
    expect(screen.getByTestId("chessboard")).toBeInTheDocument();
  });

  it("orients board from white's perspective for white player", () => {
    render(<ChessGame board={board} currentUserDisplayName="Alice" onMove={vi.fn()} />);
    expect(screen.getByTestId("chessboard")).toHaveAttribute("data-orientation", "white");
  });

  it("orients board from black's perspective for black player", () => {
    render(
      <ChessGame
        board={{ ...board, whitePlayerName: "Bob", blackPlayerName: "Alice" }}
        currentUserDisplayName="Alice"
        onMove={vi.fn()}
      />
    );
    expect(screen.getByTestId("chessboard")).toHaveAttribute("data-orientation", "black");
  });

  it("shows whose turn it is", () => {
    render(<ChessGame board={board} currentUserDisplayName="Alice" onMove={vi.fn()} />);
    expect(screen.getByText(/white's turn/i)).toBeInTheDocument();
  });

  it("shows waiting message when it is not the current user's turn", () => {
    const blackTurnBoard = { ...board, activeColor: "black" };
    render(<ChessGame board={blackTurnBoard} currentUserDisplayName="Alice" onMove={vi.fn()} />);
    expect(screen.getByText(/waiting for bob/i)).toBeInTheDocument();
  });
});
