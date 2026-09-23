package com.hexagonalchess.usermanagement;

import java.util.UUID;

public class CreateUser {

    private final UserRepository userRepository;

    public CreateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserId execute(String email, String displayName) {
        var id = new UserId(UUID.randomUUID().toString());
        userRepository.save(new User(id, email, displayName));
        return id;
    }
}
