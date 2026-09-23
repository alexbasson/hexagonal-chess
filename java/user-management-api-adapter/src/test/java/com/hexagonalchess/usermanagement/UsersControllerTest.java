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

@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean CreateUser createUser;
    @MockBean ListUsers listUsers;
    @MockBean GetUser getUser;
    @MockBean UpdateUser updateUser;
    @MockBean DeleteUser deleteUser;

    @Test
    void POST_admin_users_returns_201_with_user_id() throws Exception {
        when(createUser.execute("alice@example.com", "Alice")).thenReturn(new UserId("u1"));
        mockMvc.perform(post("/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"email": "alice@example.com", "displayName": "Alice"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("u1"));
    }

    @Test
    void GET_admin_users_returns_list_of_users() throws Exception {
        when(listUsers.execute()).thenReturn(List.of(
            new User(new UserId("u1"), "alice@example.com", "Alice")
        ));
        mockMvc.perform(get("/admin/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].email").value("alice@example.com"));
    }

    @Test
    void GET_admin_users_id_returns_user() throws Exception {
        when(getUser.execute(new UserId("u1")))
            .thenReturn(new User(new UserId("u1"), "alice@example.com", "Alice"));
        mockMvc.perform(get("/admin/users/{id}", "u1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("alice@example.com"))
            .andExpect(jsonPath("$.displayName").value("Alice"));
    }

    @Test
    void GET_admin_users_id_returns_404_when_not_found() throws Exception {
        when(getUser.execute(new UserId("missing"))).thenThrow(new IllegalArgumentException("not found"));
        mockMvc.perform(get("/admin/users/{id}", "missing"))
            .andExpect(status().isNotFound());
    }

    @Test
    void PUT_admin_users_id_returns_204() throws Exception {
        mockMvc.perform(put("/admin/users/{id}", "u1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"email": "new@example.com", "displayName": "New Name"}
                    """))
            .andExpect(status().isNoContent());
        verify(updateUser).execute(new UserId("u1"), "new@example.com", "New Name");
    }

    @Test
    void DELETE_admin_users_id_returns_204() throws Exception {
        mockMvc.perform(delete("/admin/users/{id}", "u1"))
            .andExpect(status().isNoContent());
        verify(deleteUser).execute(new UserId("u1"));
    }
}
