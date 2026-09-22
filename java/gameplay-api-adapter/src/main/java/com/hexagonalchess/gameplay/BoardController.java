package com.hexagonalchess.gameplay;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardController {

    private final BoardRepository boardRepository;

    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @GetMapping("/games/{gameId}/board")
    public ResponseEntity<Board> getBoard(@PathVariable String gameId) {
        return boardRepository.findById(new BoardId(gameId))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
