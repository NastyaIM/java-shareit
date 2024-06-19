package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = PathConstants.BOOKINGS)
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse save(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @RequestBody BookingDto bookingDto) {
        log.info("Добавление нового бронирования");
        return bookingService.save(userId, bookingDto);
    }

    @PatchMapping(PathConstants.BY_ID)
    public BookingDtoResponse approve(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable long id,
                                      @RequestParam boolean approved) {
        log.info("Подтверждение бронирования");
        return bookingService.approve(userId, id, approved);
    }

    @GetMapping(PathConstants.BY_ID)
    public BookingDtoResponse findById(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @PathVariable long id) {
        log.info("Получение данных о бронировании по id");
        return bookingService.findById(userId, id);
    }

    @GetMapping
    public List<BookingDtoResponse> findAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(defaultValue = "ALL") String state,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        log.info("Получение бронирований пользователя userId");
        return bookingService.findAll(userId, BookingState.from(state), from, size);
    }

    @GetMapping(PathConstants.BOOKINGS_OWNER)
    public List<BookingDtoResponse> findAllOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") String state,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("Получение бронирований владельца");
        return bookingService.findAllOwner(userId, BookingState.from(state), from, size);
    }
}