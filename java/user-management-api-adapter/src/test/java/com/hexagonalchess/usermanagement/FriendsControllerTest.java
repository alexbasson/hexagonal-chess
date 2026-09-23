package com.hexagonalchess.usermanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FriendsController.class)
class FriendsControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean AddFriend addFriend;
    @MockBean RemoveFriend removeFriend;
    @MockBean ListFriends listFriends;

    @Test
    void POST_users_id_friends_returns_204() throws Exception {
        mockMvc.perform(post("/users/{id}/friends", "u1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"friendId": "u2"}
                    """))
            .andExpect(status().isNoContent());
        verify(addFriend).execute(new UserId("u1"), new UserId("u2"));
    }

    @Test
    void DELETE_users_id_friends_friendId_returns_204() throws Exception {
        mockMvc.perform(delete("/users/{id}/friends/{friendId}", "u1", "u2"))
            .andExpect(status().isNoContent());
        verify(removeFriend).execute(new UserId("u1"), new UserId("u2"));
    }

    @Test
    void GET_users_id_friends_returns_list_of_friendships() throws Exception {
        when(listFriends.execute(new UserId("u1"))).thenReturn(List.of(
            new Friendship(new UserId("u1"), new UserId("u2"))
        ));
        mockMvc.perform(get("/users/{id}/friends", "u1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].friendId.value").value("u2"));
    }
}
