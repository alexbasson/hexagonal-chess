package com.hexagonalchess.usermanagement;

public class UpdateUser {

    private final UserRepository userRepository;

    public UpdateUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(UserId id, String email, String displayName) {
        userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id.value()));
        userRepository.save(new User(id, email, displayName));
    }
}
