package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {
    BookingDtoResponse create(long userId, BookingDto bookingDto);

    BookingDtoResponse approve(long userId, long id, boolean approved);

    BookingDtoResponse get(long userId, long id);

    List<BookingDtoResponse> getAll(long userId, String state);

    List<BookingDtoResponse> getAllOwner(long userId, String state);
}