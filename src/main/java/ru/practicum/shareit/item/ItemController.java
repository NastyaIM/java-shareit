package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = PathConstants.ITEMS)
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<ItemDtoGetResponse> findAllUserItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        log.info("Получение списка всех предметов");
        return itemService.findAllUserItems(userId, from, size);
    }

    @GetMapping(PathConstants.BY_ID)
    public ItemDtoGetResponse findById(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @PathVariable long id) {
        log.info("Получение предмета по id");
        return itemService.findById(userId, id);
    }

    @PatchMapping(PathConstants.BY_ID)
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable long id, @RequestBody ItemDto item) {
        log.info("Обновление предмета");
        return itemService.update(userId, id, item);
    }

    @PostMapping
    public ItemDto save(@RequestHeader("X-Sharer-User-Id") long userId,
                        @Valid @RequestBody ItemDto item) {
        log.info("Добавление нового предмета");
        return itemService.save(item, userId);
    }

    @GetMapping(PathConstants.ITEMS_SEARCH)
    public List<ItemDto> search(@RequestParam String text,
                                @RequestParam(defaultValue = "0") int from,
                                @RequestParam(defaultValue = "10") int size) {
        log.info("Поиск по названию или описанию");
        return itemService.search(text, from, size);
    }

    @PostMapping(PathConstants.BY_ID + PathConstants.ITEMS_COMMENTS)
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long id,
                                 @Valid @RequestBody CommentDto comment) {
        log.info("Добавление комментария");
        return itemService.addComment(userId, id, comment);
    }
}