package com.hexagonalchess.organizing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GamesController.class)
class GamesControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean StartGame startGame;
    @MockBean GetGame getGame;
    @MockBean ListGames listGames;

    @Test
    void POST_games_returns_201_with_game_id() throws Exception {
        when(startGame.execute("Alice", "Bob")).thenReturn(new GameId("game1"));
        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"whiteName": "Alice", "blackName": "Bob"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.gameId").value("game1"));
    }

    @Test
    void GET_games_returns_list_of_games() throws Exception {
        when(listGames.execute()).thenReturn(List.of(
            new Game(new GameId("g1"), new Player("Alice"), new Player("Bob"))
        ));
        mockMvc.perform(get("/games"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id.value").value("g1"));
    }

    @Test
    void GET_games_gameId_returns_game() throws Exception {
        var gameId = new GameId("g1");
        when(getGame.execute(gameId)).thenReturn(new Game(gameId, new Player("Alice"), new Player("Bob")));
        mockMvc.perform(get("/games/{gameId}", "g1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.white.name").value("Alice"));
    }

    @Test
    void GET_games_gameId_returns_404_when_not_found() throws Exception {
        when(getGame.execute(new GameId("missing"))).thenThrow(new IllegalArgumentException("missing"));
        mockMvc.perform(get("/games/{gameId}", "missing"))
            .andExpect(status().isNotFound());
    }
}
