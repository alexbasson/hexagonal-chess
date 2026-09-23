package com.hexagonalchess.organizing;

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

@WebMvcTest(InvitationsController.class)
class InvitationsControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean CreateInvitation createInvitation;
    @MockBean AcceptInvitation acceptInvitation;
    @MockBean DeclineInvitation declineInvitation;
    @MockBean ListInvitations listInvitations;

    @Test
    void POST_invitations_returns_201_with_invitation_id() throws Exception {
        when(createInvitation.execute("u1", "u2")).thenReturn(new InvitationId("inv-1"));
        mockMvc.perform(post("/invitations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"invitingUserId": "u1", "invitedUserId": "u2"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("inv-1"));
    }

    @Test
    void POST_invitations_returns_422_when_not_friends() throws Exception {
        when(createInvitation.execute("u1", "u2"))
            .thenThrow(new IllegalArgumentException("not friends"));
        mockMvc.perform(post("/invitations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"invitingUserId": "u1", "invitedUserId": "u2"}
                    """))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void POST_invitations_id_accept_returns_200_with_game_id() throws Exception {
        when(acceptInvitation.execute(new InvitationId("inv-1"))).thenReturn(new GameId("game-1"));
        mockMvc.perform(post("/invitations/{id}/accept", "inv-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.gameId").value("game-1"));
    }

    @Test
    void POST_invitations_id_accept_returns_422_when_not_pending() throws Exception {
        when(acceptInvitation.execute(new InvitationId("inv-1")))
            .thenThrow(new IllegalStateException("not pending"));
        mockMvc.perform(post("/invitations/{id}/accept", "inv-1"))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void POST_invitations_id_decline_returns_204() throws Exception {
        mockMvc.perform(post("/invitations/{id}/decline", "inv-1"))
            .andExpect(status().isNoContent());
        verify(declineInvitation).execute(new InvitationId("inv-1"));
    }

    @Test
    void GET_invitations_returns_filtered_list() throws Exception {
        var inv = new GameInvitation(new InvitationId("inv-1"), "u1", "u2", InvitationStatus.PENDING);
        when(listInvitations.execute("u1", "sent", "pending")).thenReturn(List.of(inv));
        mockMvc.perform(get("/invitations")
                .param("userId", "u1")
                .param("direction", "sent")
                .param("status", "pending"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].invitingUserId").value("u1"));
    }
}
