package com.hexagonalchess.gameplay;

import org.springframework.web.bind.annotation.*;

@RestController
public class MovesController {

    private final MakeMove makeMove;

    public MovesController(MakeMove makeMove) {
        this.makeMove = makeMove;
    }

    @PostMapping("/games/{gameId}/moves")
    public BoardDTO makeMove(@PathVariable String gameId, @RequestBody MoveRequest request) {
        return BoardDTO.from(makeMove.execute(new BoardId(gameId), request.toMove()));
    }
}
