package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = PathConstants.USERS)
@AllArgsConstructor
@Slf4j
public class UserController {
    private UserService userService;

    @GetMapping
    public List<User> getAll() {
        log.info("Получение списка пользователей");
        return userService.getAll();
    }

    @GetMapping(PathConstants.BY_ID)
    public User getById(@PathVariable long id) {
        log.info("Получение пользователя по id");
        return userService.getById(id);
    }

    @PatchMapping(PathConstants.BY_ID)
    public User update(@PathVariable long id, @RequestBody User user) {
        log.info("Обновление пользователя");
        return userService.update(id, user);
    }

    @DeleteMapping(PathConstants.BY_ID)
    public void delete(@PathVariable long id) {
        log.info("Удаление пользователя");
        userService.delete(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Добавление нового пользователя");
        return userService.create(user);
    }
}