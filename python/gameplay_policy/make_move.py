from dataclasses import replace
from gameplay_policy.board import Board
from gameplay_policy.board_id import BoardId
from gameplay_policy.move import Move
from gameplay_policy.square import Square
from gameplay_policy.piece import King, Queen, Rook, Bishop, Knight, Pawn
from gameplay_policy.board_repository import BoardRepository
from gameplay_policy.move_repository import MoveRepository


def make_make_move(board_repository: BoardRepository, move_repository: MoveRepository):
    def make_move(board_id: BoardId, move: Move) -> Board:
        board = board_repository.find_by_id(board_id)
        if board is None:
            raise ValueError("board not found")

        piece = board.pieces.get(move.from_square)
        if piece is None:
            raise ValueError("no piece at source square")
        if piece.color != board.active_color:
            raise ValueError("not your piece")
        if not _legal_move(board, move.from_square, move.to_square):
            raise ValueError("illegal move")

        updated_board = _apply_move(board, move)
        if _in_check(updated_board, board.active_color):
            raise ValueError("leaves king in check")

        move_repository.save(move, board_id)
        board_repository.save(updated_board)
        return updated_board

    return make_move


def _apply_move(board: Board, move: Move) -> Board:
    new_pieces = dict(board.pieces)
    new_pieces[move.to_square] = new_pieces.pop(move.from_square)
    next_color = "black" if board.active_color == "white" else "white"
    return replace(board, pieces=new_pieces, active_color=next_color)


def _in_check(board: Board, color: str) -> bool:
    king_sq = next(
        (sq for sq, p in board.pieces.items() if isinstance(p, King) and p.color == color),
        None,
    )
    if king_sq is None:
        return False
    opponent = "black" if color == "white" else "white"
    return any(
        _legal_move(board, sq, king_sq)
        for sq, p in board.pieces.items()
        if p.color == opponent
    )


def _legal_move(board: Board, from_sq: Square, to_sq: Square) -> bool:
    piece = board.pieces.get(from_sq)
    if piece is None:
        return False
    df = to_sq.file - from_sq.file
    dr = to_sq.rank - from_sq.rank

    match piece:
        case King():
            return abs(df) <= 1 and abs(dr) <= 1 and (df != 0 or dr != 0)
        case Queen():
            return _straight(board, from_sq, to_sq) or _diagonal(board, from_sq, to_sq)
        case Rook():
            return _straight(board, from_sq, to_sq)
        case Bishop():
            return _diagonal(board, from_sq, to_sq)
        case Knight():
            return (abs(df) == 2 and abs(dr) == 1) or (abs(df) == 1 and abs(dr) == 2)
        case Pawn():
            return _pawn_move(board, piece, from_sq, to_sq, df, dr)
    return False


def _pawn_move(board, piece, from_sq, to_sq, df, dr) -> bool:
    direction = 1 if piece.color == "white" else -1
    if df == 0 and dr == direction:
        return board.pieces.get(to_sq) is None
    if abs(df) == 1 and dr == direction:
        target = board.pieces.get(to_sq)
        return target is not None and target.color != piece.color
    return False


def _straight(board: Board, from_sq: Square, to_sq: Square) -> bool:
    df = to_sq.file - from_sq.file
    dr = to_sq.rank - from_sq.rank
    if df != 0 and dr != 0:
        return False
    step_f = 0 if df == 0 else df // abs(df)
    step_r = 0 if dr == 0 else dr // abs(dr)
    return _path_clear(board, from_sq, to_sq, step_f, step_r)


def _diagonal(board: Board, from_sq: Square, to_sq: Square) -> bool:
    df = to_sq.file - from_sq.file
    dr = to_sq.rank - from_sq.rank
    if abs(df) != abs(dr):
        return False
    return _path_clear(board, from_sq, to_sq, df // abs(df), dr // abs(dr))


def _path_clear(board: Board, from_sq: Square, to_sq: Square, step_f: int, step_r: int) -> bool:
    f, r = from_sq.file + step_f, from_sq.rank + step_r
    while (f, r) != (to_sq.file, to_sq.rank):
        if board.pieces.get(Square(file=f, rank=r)):
            return False
        f += step_f
        r += step_r
    target = board.pieces.get(to_sq)
    return target is None or target.color != board.pieces[from_sq].color
