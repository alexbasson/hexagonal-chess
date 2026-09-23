package com.hexagonalchess.usermanagement;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users/{id}/friends")
public class FriendsController {

    private final AddFriend addFriend;
    private final RemoveFriend removeFriend;
    private final ListFriends listFriends;

    public FriendsController(AddFriend addFriend, RemoveFriend removeFriend, ListFriends listFriends) {
        this.addFriend = addFriend;
        this.removeFriend = removeFriend;
        this.listFriends = listFriends;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable String id, @RequestBody Map<String, String> body) {
        addFriend.execute(new UserId(id), new UserId(body.get("friendId")));
    }

    @DeleteMapping("/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable String id, @PathVariable String friendId) {
        removeFriend.execute(new UserId(id), new UserId(friendId));
    }

    @GetMapping
    public List<Friendship> listFriends(@PathVariable String id) {
        return listFriends.execute(new UserId(id));
    }
}
