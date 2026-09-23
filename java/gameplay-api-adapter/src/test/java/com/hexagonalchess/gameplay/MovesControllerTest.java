package com.hexagonalchess.gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovesController.class)
class MovesControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean MakeMove makeMove;

    @Test
    void POST_moves_returns_200_with_updated_board() throws Exception {
        var boardId = new BoardId("game1");
        var updatedBoard = new Board(boardId, "Alice", "Bob", Color.BLACK, Map.of());
        when(makeMove.execute(boardId, new Move(new Square(5, 2), new Square(5, 3)))).thenReturn(updatedBoard);

        mockMvc.perform(post("/games/{gameId}/moves", "game1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"from": "e2", "to": "e3"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activeColor").value("black"));
    }
}
