package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private final UserDto userDto = UserDataUtils.getUserDtoWithId();
    private final long id = userDto.getId();

    @SneakyThrows
    @Test
    void findAll() {
        when(userService.findAll()).thenReturn(List.of(userDto));

        mockMvc.perform(get(PathConstants.USERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(id), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto.getEmail())));

        verify(userService).findAll();
    }

    @SneakyThrows
    @Test
    void findById() {
        when(userService.findById(id)).thenReturn(userDto);

        mockMvc.perform(get(PathConstants.USERS + PathConstants.BY_ID, id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService).findById(id);
    }

    @SneakyThrows
    @Test
    void findByWrongId() {
        when(userService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get(PathConstants.USERS + PathConstants.BY_ID, id))
                .andExpect(status().isNotFound());

        verify(userService).findById(id);
    }

    @SneakyThrows
    @Test
    void update() {
        UserDto userToUpdate = UserDto.builder()
                .email("newEmail@email.com")
                .build();

        UserDto updatedUser = UserDto.builder()
                .id(1L)
                .name("John")
                .email("newEmail@email.com")
                .build();

        when(userService.update(id, userToUpdate)).thenReturn(updatedUser);

        mockMvc.perform(patch(PathConstants.USERS + PathConstants.BY_ID, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedUser.getName())))
                .andExpect(jsonPath("$.email", is(userToUpdate.getEmail())));

        verify(userService).update(id, userToUpdate);
    }

    @SneakyThrows
    @Test
    void updateWhenUserIsNotValid() {
        UserDto userToUpdate = UserDto.builder()
                .email("Incorrect Email")
                .build();

        when(userService.update(id, userToUpdate)).thenThrow(new ValidationException(""));

        mockMvc.perform(patch(PathConstants.USERS + PathConstants.BY_ID, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isBadRequest());

        verify(userService).update(id, userToUpdate);
    }

    @SneakyThrows
    @Test
    void deleteUser() {

        mockMvc.perform(delete(PathConstants.USERS + PathConstants.BY_ID, id))
                .andExpect(status().isOk());

        verify(userService).delete(id);
    }

    @SneakyThrows
    @Test
    void save() {
        when(userService.save(userDto)).thenReturn(userDto);

        mockMvc.perform(post(PathConstants.USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService).save(userDto);
    }
}