package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, UserDto> users = new HashMap<>();
    private long id = 1;

    @Override
    public List<UserDto> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public UserDto getById(long id) {
        return users.get(id);
    }

    @Override
    public UserDto update(long id, UserDto user) {
        UserDto updatedUser = getById(id);
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        return updatedUser;
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public UserDto create(UserDto user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    private Long generateId() {
        return id++;
    }
}