package com.hexagonalchess.usermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteUserTest {

    @Mock private UserRepository userRepository;

    private DeleteUser deleteUser;

    @BeforeEach
    void setup() {
        deleteUser = new DeleteUser(userRepository);
    }

    @Test
    void delete_user_removes_the_user_from_the_repository() {
        var id = new UserId("user-1");

        deleteUser.execute(id);

        verify(userRepository).delete(id);
    }
}
