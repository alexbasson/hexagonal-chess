package com.hexagonalchess.usermanagement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/users")
public class UsersController {

    private final CreateUser createUser;
    private final ListUsers listUsers;
    private final GetUser getUser;
    private final UpdateUser updateUser;
    private final DeleteUser deleteUser;

    public UsersController(CreateUser createUser, ListUsers listUsers, GetUser getUser,
                           UpdateUser updateUser, DeleteUser deleteUser) {
        this.createUser = createUser;
        this.listUsers = listUsers;
        this.getUser = getUser;
        this.updateUser = updateUser;
        this.deleteUser = deleteUser;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> createUser(@RequestBody UserRequest request) {
        var id = createUser.execute(request.email(), request.displayName());
        return Map.of("id", id.value());
    }

    @GetMapping
    public List<User> listUsers() {
        return listUsers.execute();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        try {
            return ResponseEntity.ok(getUser.execute(new UserId(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable String id, @RequestBody UserRequest request) {
        updateUser.execute(new UserId(id), request.email(), request.displayName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        deleteUser.execute(new UserId(id));
    }
}
