package com.hexagonalchess.organizing;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invitations")
public class InvitationsController {

    private final CreateInvitation createInvitation;
    private final AcceptInvitation acceptInvitation;
    private final DeclineInvitation declineInvitation;
    private final ListInvitations listInvitations;

    public InvitationsController(CreateInvitation createInvitation, AcceptInvitation acceptInvitation,
                                 DeclineInvitation declineInvitation, ListInvitations listInvitations) {
        this.createInvitation = createInvitation;
        this.acceptInvitation = acceptInvitation;
        this.declineInvitation = declineInvitation;
        this.listInvitations = listInvitations;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createInvitation(@RequestBody InvitationRequest request) {
        try {
            var id = createInvitation.execute(request.invitingUserId(), request.invitedUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<Map<String, String>> acceptInvitation(@PathVariable String id) {
        try {
            var gameId = acceptInvitation.execute(new InvitationId(id));
            return ResponseEntity.ok(Map.of("gameId", gameId.value()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PostMapping("/{id}/decline")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void declineInvitation(@PathVariable String id) {
        declineInvitation.execute(new InvitationId(id));
    }

    @GetMapping
    public List<GameInvitation> listInvitations(@RequestParam String userId,
                                                @RequestParam String direction,
                                                @RequestParam(required = false) String status) {
        return listInvitations.execute(userId, direction, status);
    }
}
