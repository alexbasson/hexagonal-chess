package com.hexagonalchess.usermanagement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UserId, User> store = new LinkedHashMap<>();

    @Override
    public void save(User user) {
        store.put(user.id(), user);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(UserId id) {
        store.remove(id);
    }
}
