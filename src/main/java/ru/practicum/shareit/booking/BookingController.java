package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = PathConstants.BOOKINGS)
@AllArgsConstructor
@Slf4j
public class BookingController {
    private BookingService bookingService;

    @PostMapping
    public BookingDtoResponse create(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @Valid @RequestBody BookingDto bookingDto) {
        log.info("Добавление нового бронирования");
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping(PathConstants.BY_ID)
    public BookingDtoResponse approve(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable long id,
                                      @RequestParam boolean approved) {
        log.info("Подтверждение бронирования");
        return bookingService.approve(userId, id, approved);
    }

    @GetMapping(PathConstants.BY_ID)
    public BookingDtoResponse get(@RequestHeader("X-Sharer-User-Id") long userId,
                                  @PathVariable long id) {
        log.info("Получение данный о бронировании по id");
        return bookingService.get(userId, id);
    }

    @GetMapping
    public List<BookingDtoResponse> getAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение бронирований пользователя userId");
        return bookingService.getAll(userId, state);
    }

    @GetMapping(PathConstants.BOOKINGS_OWNER)
    public List<BookingDtoResponse> getAllOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение бронирований владельца");
        return bookingService.getAllOwner(userId, state);
    }
}