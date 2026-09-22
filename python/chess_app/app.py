from fastapi import FastAPI
from gameplay_db_adapter.in_memory_board_repository import InMemoryBoardRepository
from gameplay_db_adapter.in_memory_move_repository import InMemoryMoveRepository
from gameplay_policy.setup_board import make_setup_board
from gameplay_policy.make_move import make_make_move
from organizing_db_adapter.in_memory_game_repository import InMemoryGameRepository
from cross_context_adapter.gameplay_game_initializer import GameplayGameInitializer
from organizing_policy.start_game import make_start_game
from organizing_policy.get_game import make_get_game
from organizing_policy.list_games import make_list_games
from gameplay_api_adapter.moves_router import create_moves_router
from gameplay_api_adapter.board_router import create_board_router
from organizing_api_adapter.games_router import create_games_router


def create_app() -> FastAPI:
    board_repository = InMemoryBoardRepository()
    move_repository = InMemoryMoveRepository()
    setup_board = make_setup_board(board_repository)
    make_move = make_make_move(board_repository, move_repository)

    game_repository = InMemoryGameRepository()
    game_initializer = GameplayGameInitializer(setup_board)
    start_game = make_start_game(game_repository, game_initializer)
    get_game = make_get_game(game_repository)
    list_games = make_list_games(game_repository)

    app = FastAPI()
    app.include_router(create_moves_router(make_move))
    app.include_router(create_board_router(board_repository))
    app.include_router(create_games_router(start_game, get_game, list_games))
    return app
