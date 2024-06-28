package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDtoResponse save(long userId, BookingDto bookingDto);

    BookingDtoResponse approve(long userId, long id, boolean approved);

    BookingDtoResponse findById(long userId, long id);

    List<BookingDtoResponse> findAll(long userId, BookingState state, int from, int size);

    List<BookingDtoResponse> findAllOwner(long userId, BookingState state, int from, int size);
}