"use client";

import { Chessboard } from "react-chessboard";
import { Chess } from "chess.js";
import type { Board } from "@/lib/adapters";

type Props = {
  board: Board;
  currentUserDisplayName: string;
  onMove: (from: string, to: string) => void;
};

function piecesToFen(board: Board): string {
  try {
    const chess = new Chess();
    chess.clear();
    for (const { square, piece } of board.pieces as Array<{
      square: { file: number; rank: number };
      piece: { type: string; color: string };
    }>) {
      const file = String.fromCharCode(96 + square.file);
      const sq = `${file}${square.rank}` as Parameters<typeof chess.put>[1];
      const typeMap: Record<string, string> = { Pawn: "p", Rook: "r", Knight: "n", Bishop: "b", Queen: "q", King: "k" };
      chess.put({ type: typeMap[piece.type] as "p", color: piece.color === "white" ? "w" : "b" }, sq);
    }
    return chess.fen();
  } catch {
    return "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
  }
}

export function ChessGame({ board, currentUserDisplayName, onMove }: Props) {
  const isWhite = board.whitePlayerName === currentUserDisplayName;
  const isBlack = board.blackPlayerName === currentUserDisplayName;
  const orientation: "white" | "black" = isBlack ? "black" : "white";
  const isMyTurn =
    (board.activeColor === "white" && isWhite) || (board.activeColor === "black" && isBlack);

  const opponentName = isWhite ? board.blackPlayerName : board.whitePlayerName;
  const statusText = isMyTurn
    ? `${board.activeColor === "white" ? "White" : "Black"}'s turn — your move`
    : `Waiting for ${opponentName}…`;

  const fen = piecesToFen(board);

  return (
    <div className="flex flex-col items-center gap-4">
      <p className="text-sm font-medium">{statusText}</p>
      <div className="w-full max-w-[560px]">
        <Chessboard
          options={{
            position: fen,
            boardOrientation: orientation,
            allowDragging: isMyTurn,
            onPieceDrop: ({ sourceSquare, targetSquare }) => {
              if (!isMyTurn || !targetSquare) return false;
              onMove(sourceSquare, targetSquare);
              return true;
            },
          }}
        />
      </div>
      <div className="flex justify-between w-full max-w-[560px] text-sm text-muted-foreground">
        <span>White: {board.whitePlayerName}</span>
        <span>Black: {board.blackPlayerName}</span>
      </div>
    </div>
  );
}
