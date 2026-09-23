package com.hexagonalchess.usermanagement;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryUserRepositoryTest {

    private final InMemoryUserRepository repository = new InMemoryUserRepository();

    @Test
    void findById_returns_empty_when_no_user_saved() {
        assertThat(repository.findById(new UserId("unknown"))).isEmpty();
    }

    @Test
    void findAll_returns_empty_list_initially() {
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void findById_returns_saved_user() {
        var user = new User(new UserId("u1"), "alice@example.com", "Alice");
        repository.save(user);
        assertThat(repository.findById(new UserId("u1"))).contains(user);
    }

    @Test
    void findAll_returns_all_saved_users() {
        var u1 = new User(new UserId("u1"), "alice@example.com", "Alice");
        var u2 = new User(new UserId("u2"), "bob@example.com", "Bob");
        repository.save(u1);
        repository.save(u2);
        assertThat(repository.findAll()).containsExactlyInAnyOrder(u1, u2);
    }

    @Test
    void delete_removes_the_user() {
        var id = new UserId("u1");
        repository.save(new User(id, "alice@example.com", "Alice"));
        repository.delete(id);
        assertThat(repository.findById(id)).isEmpty();
    }
}
