package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
//    private Long id;
    private String name;
    @Email
    @NotNull
    private String email;
}
