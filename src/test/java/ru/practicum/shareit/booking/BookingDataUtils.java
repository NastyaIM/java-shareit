package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingDataUtils {
    public static Booking getBookingWithId(User user, Item item) {
        return Booking.builder()
                .id(1L)
                .status(BookingStatus.WAITING)
                .booker(user)
                .item(item)
                .end(LocalDateTime.of(2022, 12, 11, 12, 12, 12))
                .start(LocalDateTime.of(2021, 12, 11, 12, 12, 12))
                .build();
    }

    public static Booking getBookingPast() {
        return Booking.builder()
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().minusDays(1L))
                .end(LocalDateTime.now().minusHours(5L))
                .build();
    }

    public static Booking getBookingCurrent() {
        return Booking.builder()
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().minusDays(2L))
                .end(LocalDateTime.now().plusDays(1L))
                .build();
    }

    public static Booking getBookingFuture() {
        return Booking.builder()
                .status(BookingStatus.REJECTED)
                .start(LocalDateTime.now().plusDays(3L))
                .end(LocalDateTime.now().plusDays(2L))
                .build();
    }

    public static BookingDto getBookingDtoPast(ItemDto item, UserDto booker) {
        return BookingDto.builder()
                .itemId(item.getId())
                .booker(booker)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusSeconds(1L))
                .build();
    }

    public static BookingDto getBookingDtoCurrent(ItemDto item, UserDto booker) {
        return BookingDto.builder()
                .itemId(item.getId())
                .booker(booker)
                .start(LocalDateTime.now().plusSeconds(1L))
                .end(LocalDateTime.now().plusSeconds(5L))
                .build();
    }

    public static BookingDto getBookingDtoFuture(ItemDto item, UserDto booker) {
        return BookingDto.builder()
                .itemId(item.getId())
                .booker(booker)
                .start(LocalDateTime.now().plusSeconds(5L))
                .end(LocalDateTime.now().plusDays(2L))
                .build();
    }
}