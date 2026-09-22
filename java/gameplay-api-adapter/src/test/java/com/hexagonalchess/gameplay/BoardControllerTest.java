package com.hexagonalchess.gameplay;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean BoardRepository boardRepository;

    @Test
    void GET_board_returns_404_when_board_not_found() throws Exception {
        when(boardRepository.findById(new BoardId("unknown"))).thenReturn(Optional.empty());
        mockMvc.perform(get("/games/{gameId}/board", "unknown"))
            .andExpect(status().isNotFound());
    }

    @Test
    void GET_board_returns_board_state() throws Exception {
        var boardId = new BoardId("game1");
        var board = new Board(boardId, "Alice", "Bob", Color.WHITE, Map.of());
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        mockMvc.perform(get("/games/{gameId}/board", "game1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.whitePlayerName").value("Alice"))
            .andExpect(jsonPath("$.activeColor").value("WHITE"));
    }
}
