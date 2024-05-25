package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> findAll();

    UserDto findById(long id);

    UserDto update(long id, UserDto user);

    void delete(long id);

    UserDto save(UserDto user);
}