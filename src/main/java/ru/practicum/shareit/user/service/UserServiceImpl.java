package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) {
        return UserMapper.toUserDto(checkNotFound(id));
    }

    @Override
    public UserDto update(long id, UserDto userDto) {
        User user = checkNotFound(id);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (isValidEmail(userDto.getEmail())) {
                user.setEmail(userDto.getEmail());
            } else {
                throw new ValidationException("Некорректный email");
            }
        }
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void delete(long id) {
        checkNotFound(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDto save(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    private User checkNotFound(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id не существует"));
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.length() > 5;
    }
}