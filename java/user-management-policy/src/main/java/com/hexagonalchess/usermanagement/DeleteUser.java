package com.hexagonalchess.usermanagement;

public class DeleteUser {

    private final UserRepository userRepository;

    public DeleteUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UserId id) {
        userRepository.delete(id);
    }
}
