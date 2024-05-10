package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {
    List<UserDto> getAll();

    UserDto getById(long id);

    UserDto update(long id, UserDto user);

    void delete(long id);

    UserDto create(UserDto user);
}