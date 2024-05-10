package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = PathConstants.ITEMS)
@AllArgsConstructor
@Slf4j
public class ItemController {
    private ItemService itemService;

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получение списка всех предметов");
        return itemService.getAllUserItems(userId);
    }

    @GetMapping(PathConstants.BY_ID)
    public ItemDto getById(@PathVariable long id) {
        log.info("Получение предмета по id");
        return itemService.getById(id);
    }

    @PatchMapping(PathConstants.BY_ID)
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long id, @RequestBody ItemDto item) {
        log.info("Обновление предмета");
        return itemService.update(userId, id, item);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Valid @RequestBody ItemDto item) {
        log.info("Добавление нового предмета");
        return itemService.create(item, userId);
    }

    @GetMapping(PathConstants.ITEMS_SEARCH)
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Поиск по названию или описанию");
        return itemService.search(text);
    }
}