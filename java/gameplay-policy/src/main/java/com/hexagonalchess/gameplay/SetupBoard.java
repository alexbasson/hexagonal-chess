package com.hexagonalchess.gameplay;

public class SetupBoard {

    private final BoardRepository boardRepository;

    public SetupBoard(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardId execute(String whiteName, String blackName) {
        var boardId = new BoardId("hardcoded-id");
        boardRepository.save(new Board(boardId));
        return boardId;
    }
}
