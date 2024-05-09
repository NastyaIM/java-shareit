package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(long id) {
        checkNotFound(id);
        return userRepository.getById(id);
    }

    @Override
    public User update(long id, User user) {
        checkNotFound(id);
        if (userRepository.getAll().contains(user) && !userRepository.getById(id).getEmail().equals(user.getEmail())) {
            throw new AlreadyExistsException("Пользователь с таким email уже существует");
        }
        return userRepository.update(id, user);
    }

    @Override
    public void delete(long id) {
        checkNotFound(id);
        userRepository.delete(id);
    }

    @Override
    public User create(User user) {
        if (userRepository.getAll().contains(user)) {
            throw new AlreadyExistsException("Пользователь с таким email уже существует");
        }
        return userRepository.create(user);
    }

    private void checkNotFound(long id) {
        if (userRepository.getById(id) == null) {
            throw new NotFoundException("Пользователя с таким id не существует");
        }
    }
}