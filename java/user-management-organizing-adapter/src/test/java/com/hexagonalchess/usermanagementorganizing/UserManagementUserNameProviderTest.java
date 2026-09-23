package com.hexagonalchess.usermanagementorganizing;

import com.hexagonalchess.usermanagement.User;
import com.hexagonalchess.usermanagement.UserId;
import com.hexagonalchess.usermanagement.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserManagementUserNameProviderTest {

    @Mock private UserRepository userRepository;

    private UserManagementUserNameProvider provider;

    @BeforeEach
    void setup() {
        provider = new UserManagementUserNameProvider(userRepository);
    }

    @Test
    void get_display_name_returns_the_users_display_name() {
        when(userRepository.findById(new UserId("u1")))
            .thenReturn(Optional.of(new User(new UserId("u1"), "alice@example.com", "Alice")));

        assertThat(provider.getDisplayName("u1")).isEqualTo("Alice");
    }

    @Test
    void get_display_name_throws_when_user_not_found() {
        when(userRepository.findById(new UserId("missing"))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> provider.getDisplayName("missing"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
