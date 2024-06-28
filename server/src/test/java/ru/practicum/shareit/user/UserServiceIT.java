package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceIT {
    @Autowired
    private UserServiceImpl userService;
    private UserDto user1;
    private UserDto user2;

    @BeforeEach
    public void beforeEach() {
        user1 = userService.save(UserDataUtils.getUserDto1());
        user2 = userService.save(UserDataUtils.getUserDto2());
    }

    @Test
    void findAll() {
        List<UserDto> users = userService.findAll();

        assertThat(users).isNotEmpty();
        assertEquals(2, users.size());
        assertEquals(1L, user1.getId());
        assertEquals(2L, user2.getId());
    }

    @Test
    void findById() {
        UserDto user = userService.findById(1);

        assertEquals("user1", user.getName());
        assertEquals("user1@email.com", user.getEmail());

        assertThrows(NotFoundException.class, () -> userService.findById(999));
    }

    @Test
    void update() {
        UserDto userToUpdate = UserDto.builder()
                .email("newEmail@email.com")
                .build();

        UserDto updatedUser = userService.update(user1.getId(), userToUpdate);

        assertEquals(2, userService.findAll().size());
        assertEquals("newEmail@email.com", updatedUser.getEmail());
        assertEquals("user1", updatedUser.getName());

        userToUpdate = UserDto.builder()
                .name("newUser1")
                .build();

        updatedUser = userService.update(user1.getId(), userToUpdate);
        assertEquals("newEmail@email.com", updatedUser.getEmail());
        assertEquals("newUser1", updatedUser.getName());
    }

    @Test
    void delete() {
        assertEquals(2, userService.findAll().size());

        userService.delete(user1.getId());

        assertEquals(1, userService.findAll().size());
        assertEquals(List.of(user2), userService.findAll());
    }
}