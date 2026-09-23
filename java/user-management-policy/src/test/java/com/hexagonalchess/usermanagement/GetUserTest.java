package com.hexagonalchess.usermanagement;

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
class GetUserTest {

    @Mock private UserRepository userRepository;

    private GetUser getUser;

    @BeforeEach
    void setup() {
        getUser = new GetUser(userRepository);
    }

    @Test
    void get_user_returns_the_user_for_a_known_id() {
        var id = new UserId("user-1");
        var user = new User(id, "alice@example.com", "Alice");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        var result = getUser.execute(id);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void get_user_throws_when_user_not_found() {
        var id = new UserId("unknown");
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getUser.execute(id))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
