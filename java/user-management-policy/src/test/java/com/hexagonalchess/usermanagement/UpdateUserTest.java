package com.hexagonalchess.usermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserTest {

    @Mock private UserRepository userRepository;

    private UpdateUser updateUser;

    @BeforeEach
    void setup() {
        updateUser = new UpdateUser(userRepository);
    }

    @Test
    void update_user_saves_the_updated_user() {
        var id = new UserId("user-1");
        when(userRepository.findById(id)).thenReturn(Optional.of(new User(id, "old@example.com", "Old Name")));

        updateUser.execute(id, "new@example.com", "New Name");

        var captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        var saved = captor.getValue();
        assertThat(saved.id()).isEqualTo(id);
        assertThat(saved.email()).isEqualTo("new@example.com");
        assertThat(saved.displayName()).isEqualTo("New Name");
    }

    @Test
    void update_user_throws_when_user_not_found() {
        var id = new UserId("unknown");
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateUser.execute(id, "x@example.com", "X"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
