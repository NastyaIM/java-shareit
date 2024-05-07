package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDto {
    @Positive
    private Long id;
    private String name;
    @Email
    @EqualsAndHashCode.Include
    @NotNull
    private String email;

    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}