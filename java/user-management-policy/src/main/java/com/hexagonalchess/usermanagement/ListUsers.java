package com.hexagonalchess.usermanagement;

import java.util.List;

public class ListUsers {

    private final UserRepository userRepository;

    public ListUsers(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> execute() {
        return userRepository.findAll();
    }
}
