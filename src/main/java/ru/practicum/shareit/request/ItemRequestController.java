package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(PathConstants.REQUESTS)
@AllArgsConstructor
@Slf4j
public class ItemRequestController {
    private ItemRequestService itemRequestsService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody @Valid ItemRequestDto request) {
        return itemRequestsService.create(userId, request);
    }

    @GetMapping
    public List<ItemRequestDto> getAllUserRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestsService.get(userId);
    }

    @GetMapping(PathConstants.ALL)
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        return itemRequestsService.getAll(userId, from, size);
    }

    @GetMapping(PathConstants.BY_ID)
    public ItemRequestDto getById(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long id) {
        return itemRequestsService.getById(userId, id);
    }
}
