package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(PathConstants.REQUESTS)
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    @Autowired
    private ItemRequestService itemRequestsService;

    @PostMapping
    public ItemRequestDto save(@RequestHeader("X-Sharer-User-Id") long userId,
                               @RequestBody @Valid ItemRequestDto request) {
        log.info("Сохранение запроса")
        return itemRequestsService.save(userId, request);
    }

    @GetMapping
    public List<ItemRequestDto> findAllUserRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получение всех запросов переданного пользователя");
        return itemRequestsService.findAllUserRequests(userId);
    }

    @GetMapping(PathConstants.ALL)
    public List<ItemRequestDto> findAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("Получение все запросов других пользователей, на которые можно ответить");
        return itemRequestsService.findAll(userId, from, size);
    }

    @GetMapping(PathConstants.BY_ID)
    public ItemRequestDto findById(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @PathVariable long id) {
        log.info("Получение запроса по id");
        return itemRequestsService.findById(userId, id);
    }
}