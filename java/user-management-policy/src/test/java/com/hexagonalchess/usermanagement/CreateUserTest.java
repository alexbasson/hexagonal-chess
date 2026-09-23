package com.hexagonalchess.usermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateUserTest {

    @Mock private UserRepository userRepository;

    private CreateUser createUser;

    @BeforeEach
    void setup() {
        createUser = new CreateUser(userRepository);
    }

    @Test
    void create_user_saves_a_user_to_the_repository() {
        var userId = createUser.execute("alice@example.com", "Alice");

        var captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.id()).isEqualTo(userId);
        assertThat(saved.email()).isEqualTo("alice@example.com");
        assertThat(saved.displayName()).isEqualTo("Alice");
    }
}
