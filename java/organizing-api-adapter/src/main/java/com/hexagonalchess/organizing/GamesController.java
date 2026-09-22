package com.hexagonalchess.organizing;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/games")
public class GamesController {

    private final StartGame startGame;
    private final GetGame getGame;
    private final ListGames listGames;

    public GamesController(StartGame startGame, GetGame getGame, ListGames listGames) {
        this.startGame = startGame;
        this.getGame = getGame;
        this.listGames = listGames;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> startGame(@RequestBody StartGameRequest request) {
        var gameId = startGame.execute(request.whiteName(), request.blackName());
        return Map.of("gameId", gameId.value());
    }

    @GetMapping
    public List<Game> listGames() {
        return listGames.execute();
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable String gameId) {
        try {
            return ResponseEntity.ok(getGame.execute(new GameId(gameId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
