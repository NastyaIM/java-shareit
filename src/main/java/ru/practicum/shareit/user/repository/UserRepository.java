package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User getById(long id);

    User update(long id, User user);

    void delete(long id);

    User create(User user);
}