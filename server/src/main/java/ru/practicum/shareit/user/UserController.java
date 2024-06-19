package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = PathConstants.USERS)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Получение списка пользователей");
        return userService.findAll();
    }

    @GetMapping(PathConstants.BY_ID)
    public UserDto findById(@PathVariable long id) {
        log.info("Получение пользователя по id");
        return userService.findById(id);
    }

    @PatchMapping(PathConstants.BY_ID)
    public UserDto update(@PathVariable long id, @RequestBody UserDto user) {
        log.info("Обновление пользователя");
        return userService.update(id, user);
    }

    @DeleteMapping(PathConstants.BY_ID)
    public void delete(@PathVariable long id) {
        log.info("Удаление пользователя");
        userService.delete(id);
    }

    @PostMapping
    public UserDto save(@RequestBody UserDto user) {
        log.info("Добавление нового пользователя");
        return userService.save(user);
    }
}