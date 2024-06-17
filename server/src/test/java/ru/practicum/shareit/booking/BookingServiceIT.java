package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemDataUtils;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.UserDataUtils;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingServiceIT {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private BookingServiceImpl bookingService;
    private UserDto user1;
    private UserDto user2;
    private ItemDto item1;
    private ItemDto item2;
    private ItemDto item3;
    private BookingDtoResponse bookingCurrent;
    private BookingDtoResponse bookingPast;
    private BookingDtoResponse bookingFuture;

    @BeforeEach
    public void beforeEach() {
        user1 = userService.save(UserDataUtils.getUserDto1());
        user2 = userService.save(UserDataUtils.getUserDto2());

        item1 = itemService.save(ItemDataUtils.getItemDto1(null), user1.getId());
        item2 = itemService.save(ItemDataUtils.getItemDto2(null), user1.getId());
        item3 = itemService.save(ItemDataUtils.getItemDto3(null), user1.getId());

        bookingCurrent = bookingService.save(user2.getId(), BookingDataUtils.getBookingDtoCurrent(item1, user2));
        bookingFuture = bookingService.save(user2.getId(), BookingDataUtils.getBookingDtoFuture(item2, user2));
        bookingPast = bookingService.save(user2.getId(), BookingDataUtils.getBookingDtoPast(item3, user2));
    }

    @Test
    void approve() {
        assertEquals(BookingStatus.WAITING, bookingCurrent.getStatus());

        bookingCurrent = bookingService.approve(user1.getId(), bookingCurrent.getId(), true);

        assertEquals(BookingStatus.APPROVED, bookingCurrent.getStatus());

        assertThrows(ValidationException.class, () -> bookingService.approve(user1.getId(), bookingCurrent.getId(), true));
        assertThrows(NotFoundException.class, () -> bookingService.approve(user2.getId(), bookingCurrent.getId(), true));
    }

    @Test
    void findById() {
        BookingDtoResponse actualBooking = bookingService.findById(user1.getId(), bookingCurrent.getId());

        assertEquals(bookingCurrent.getId(), actualBooking.getId());
        assertEquals(bookingCurrent.getStatus(), actualBooking.getStatus());
    }

    @SneakyThrows
    @Test
    void findAll() {
        Thread.sleep(1100);
        int from = 0;
        int size = 10;

        List<BookingDtoResponse> bookingsAll = bookingService.findAll(user2.getId(), BookingState.ALL, from, size);

        assertEquals(3, bookingsAll.size());
        assertEquals(List.of(bookingFuture, bookingCurrent, bookingPast), bookingsAll);

        List<BookingDtoResponse> bookingsCurrent = bookingService.findAll(user2.getId(), BookingState.CURRENT, from, size);

        assertEquals(1, bookingsCurrent.size());
        assertEquals(List.of(bookingCurrent), bookingsCurrent);

        List<BookingDtoResponse> bookingsPast = bookingService.findAll(user2.getId(), BookingState.PAST, from, size);

        assertEquals(1, bookingsPast.size());
        assertEquals(List.of(bookingPast), bookingsPast);

        List<BookingDtoResponse> bookingsFuture = bookingService.findAll(user2.getId(), BookingState.FUTURE, from, size);

        assertEquals(1, bookingsFuture.size());
        assertEquals(List.of(bookingFuture), bookingsFuture);

        bookingFuture = bookingService.approve(user1.getId(), bookingFuture.getId(), false);

        List<BookingDtoResponse> bookingsWaiting = bookingService.findAll(user2.getId(), BookingState.WAITING, from, size);

        assertEquals(2, bookingsWaiting.size());
        assertEquals(List.of(bookingCurrent, bookingPast), bookingsWaiting);

        List<BookingDtoResponse> bookingsRejected = bookingService.findAll(user2.getId(), BookingState.REJECTED, from, size);

        assertEquals(1, bookingsRejected.size());
        assertEquals(List.of(bookingFuture), bookingsRejected);
    }

    @SneakyThrows
    @Test
    void findAllOwner() {
        Thread.sleep(1100);
        int from = 0;
        int size = 10;

        List<BookingDtoResponse> bookingsAll = bookingService.findAllOwner(user1.getId(), BookingState.ALL, from, size);

        assertEquals(3, bookingsAll.size());
        assertEquals(List.of(bookingFuture, bookingCurrent, bookingPast), bookingsAll);

        List<BookingDtoResponse> bookingsCurrent = bookingService.findAllOwner(user1.getId(), BookingState.CURRENT, from, size);

        assertEquals(1, bookingsCurrent.size());
        assertEquals(List.of(bookingCurrent), bookingsCurrent);

        List<BookingDtoResponse> bookingsPast = bookingService.findAllOwner(user1.getId(), BookingState.PAST, from, size);

        assertEquals(1, bookingsPast.size());
        assertEquals(List.of(bookingPast), bookingsPast);

        List<BookingDtoResponse> bookingsFuture = bookingService.findAllOwner(user1.getId(), BookingState.FUTURE, from, size);

        assertEquals(1, bookingsFuture.size());
        assertEquals(List.of(bookingFuture), bookingsFuture);

        bookingFuture = bookingService.approve(user1.getId(), bookingFuture.getId(), false);

        List<BookingDtoResponse> bookingsWaiting = bookingService.findAllOwner(user1.getId(), BookingState.WAITING, from, size);

        assertEquals(2, bookingsWaiting.size());
        assertEquals(List.of(bookingCurrent, bookingPast), bookingsWaiting);

        List<BookingDtoResponse> bookingsRejected = bookingService.findAllOwner(user1.getId(), BookingState.REJECTED, from, size);

        assertEquals(1, bookingsRejected.size());
        assertEquals(List.of(bookingFuture), bookingsRejected);
    }
}