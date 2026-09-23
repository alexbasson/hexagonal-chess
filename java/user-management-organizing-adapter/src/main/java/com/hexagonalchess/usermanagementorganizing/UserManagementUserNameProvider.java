package com.hexagonalchess.usermanagementorganizing;

import com.hexagonalchess.organizing.UserNameProvider;
import com.hexagonalchess.usermanagement.UserId;
import com.hexagonalchess.usermanagement.UserRepository;

public class UserManagementUserNameProvider implements UserNameProvider {

    private final UserRepository userRepository;

    public UserManagementUserNameProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getDisplayName(String userId) {
        return userRepository.findById(new UserId(userId))
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId))
            .displayName();
    }
}
