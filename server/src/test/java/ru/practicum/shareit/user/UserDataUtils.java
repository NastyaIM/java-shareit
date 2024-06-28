package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserDataUtils {

    public static User getUser1() {
        return User.builder()
                .name("user1")
                .email("user1@email.com")
                .build();
    }

    public static User getUser2() {
        return User.builder()
                .name("user2")
                .email("user2@email.com")
                .build();
    }

    public static User getUser1WithId() {
        return User.builder()
                .id(1L)
                .name("user1")
                .email("user1@email.com")
                .build();
    }

    public static User getUser2WithId() {
        return User.builder()
                .id(2L)
                .name("user2")
                .email("user2@email.com")
                .build();
    }

    public static UserDto getUserDto1() {
        return UserDto.builder()
                .name("user1")
                .email("user1@email.com")
                .build();
    }

    public static UserDto getUserDto2() {
        return UserDto.builder()
                .name("user2")
                .email("user2@email.com")
                .build();
    }

    public static UserDto getUserDtoWithId() {
        return UserDto.builder()
                .id(1L)
                .name("John")
                .email("john.doe@mail.com")
                .build();
    }
}
