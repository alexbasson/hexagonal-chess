package com.hexagonalchess.usermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListUsersTest {

    @Mock private UserRepository userRepository;

    private ListUsers listUsers;

    @BeforeEach
    void setup() {
        listUsers = new ListUsers(userRepository);
    }

    @Test
    void list_users_returns_empty_when_no_users() {
        when(userRepository.findAll()).thenReturn(List.of());
        assertThat(listUsers.execute()).isEmpty();
    }

    @Test
    void list_users_returns_all_users() {
        var users = List.of(
            new User(new UserId("u1"), "alice@example.com", "Alice"),
            new User(new UserId("u2"), "bob@example.com", "Bob")
        );
        when(userRepository.findAll()).thenReturn(users);
        assertThat(listUsers.execute()).isEqualTo(users);
    }
}
