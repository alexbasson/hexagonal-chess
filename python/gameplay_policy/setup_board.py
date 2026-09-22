import uuid
from gameplay_policy.board_id import BoardId
from gameplay_policy.board import Board
from gameplay_policy.square import Square
from gameplay_policy.piece import King, Queen, Rook, Bishop, Knight, Pawn
from gameplay_policy.board_repository import BoardRepository

_BACK_RANK = [Rook, Knight, Bishop, Queen, King, Bishop, Knight, Rook]


def make_setup_board(board_repository: BoardRepository):
    def setup_board(white_player_name: str, black_player_name: str) -> BoardId:
        board_id = BoardId(value=str(uuid.uuid4()))
        board = Board(
            id=board_id,
            white_player_name=white_player_name,
            black_player_name=black_player_name,
            active_color="white",
            pieces=_initial_pieces(),
        )
        board_repository.save(board)
        return board_id
    return setup_board


def _initial_pieces() -> dict:
    pieces = {}
    for i, piece_class in enumerate(_BACK_RANK):
        file = i + 1
        pieces[Square(file=file, rank=1)] = piece_class(color="white")
        pieces[Square(file=file, rank=8)] = piece_class(color="black")
        pieces[Square(file=file, rank=2)] = Pawn(color="white")
        pieces[Square(file=file, rank=7)] = Pawn(color="black")
    return pieces
