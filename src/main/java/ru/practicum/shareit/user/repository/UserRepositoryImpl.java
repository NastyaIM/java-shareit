package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public List<UserDto> getAll() {
        return users.values().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(long id) {
        User user = users.get(id);
        return user != null ? UserMapper.toUserDto(user) : null;
    }

    @Override
    public UserDto update(long id, UserDto user) {
        User updatedUser = users.get(id);
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public UserDto create(UserDto user) {
        user.setId(generateId());
        users.put(user.getId(), UserMapper.toUser(user));
        return user;
    }

    private Long generateId() {
        return id++;
    }
}