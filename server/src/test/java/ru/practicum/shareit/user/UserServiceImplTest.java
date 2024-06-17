package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private final User user = UserDataUtils.getUser1WithId();

    @Test
    void save() {
        when(userRepository.save(user)).thenReturn(user);

        UserDto userDto = UserMapper.toUserDto(user);
        UserDto actualUser = userService.save(userDto);

        assertEquals(userDto, actualUser);
        verify(userRepository).save(user);
    }

    @Test
    void findAll() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> actualUsers = userService.findAll();

        assertEquals(users.stream().map(UserMapper::toUserDto).collect(Collectors.toList()),
                actualUsers);
        verify(userRepository).findAll();
    }

    @Test
    void findByIdWhenUserIsFound() {
        long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto actualUser = userService.findById(id);

        assertEquals(UserMapper.toUserDto(user), actualUser);
        verify(userRepository).findById(id);
    }

    @Test
    void findByIdWhenUserIsNotFound() {
        long id = 999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(id));
    }

    @Test
    void updateWhenEmailNotNullAndNameIsNull() {
        long id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto userToUpdate = UserDto.builder()
                .email("newEmail@email.com")
                .build();

        User expectedUser = User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(userToUpdate.getEmail())
                .build();

        when(userRepository.save(expectedUser)).thenReturn(expectedUser);
        UserDto updatedUser = userService.update(id, userToUpdate);

        assertEquals(userToUpdate.getEmail(), updatedUser.getEmail());
        verify(userRepository).save(expectedUser);
    }

    @Test
    void updateWhenEmailIsNullAndNameNotNull() {
        long id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto userToUpdate = UserDto.builder()
                .name("newName")
                .build();

        User expectedUser = User.builder()
                .id(user.getId())
                .name(userToUpdate.getName())
                .email(user.getEmail())
                .build();

        when(userRepository.save(expectedUser)).thenReturn(expectedUser);
        UserDto updatedUser = userService.update(id, userToUpdate);

        assertEquals(userToUpdate.getName(), updatedUser.getName());
        verify(userRepository).save(expectedUser);
    }

    @Test
    void delete() {
        long id = user.getId();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.delete(id);
        verify(userRepository).deleteById(id);
    }
}