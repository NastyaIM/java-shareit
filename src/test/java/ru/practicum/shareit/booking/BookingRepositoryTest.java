package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.ItemDataUtils;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserDataUtils;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    private User user1;
    private User user2;
    private Item item1;
    private Booking bookingCurrent;
    private Booking bookingPast;
    private Booking bookingFuture;
    private final PageRequest page = PageRequest.of(0, 10);

    @BeforeEach
    public void beforeEach() {
        user1 = userRepository.save(UserDataUtils.getUser1());
        user2 = userRepository.save(UserDataUtils.getUser2());
        item1 = itemRepository.save(ItemDataUtils.getItem1());
        item1.setOwner(user1);
        bookingCurrent = bookingRepository.save(BookingDataUtils.getBookingCurrent());
        bookingFuture = bookingRepository.save(BookingDataUtils.getBookingFuture());
        bookingPast = bookingRepository.save(BookingDataUtils.getBookingPast());
        bookingCurrent.setBooker(user2);
        bookingCurrent.setItem(item1);
        bookingFuture.setBooker(user2);
        bookingFuture.setItem(item1);
        bookingPast.setBooker(user2);
        bookingPast.setItem(item1);

    }

    @Test
    void findByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(user2.getId(), page).getContent();

        assertEquals(3, bookings.size());
        assertEquals(List.of(bookingFuture, bookingPast, bookingCurrent), bookings);
    }

    @Test
    void findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(user2.getId(),
                LocalDateTime.now(), LocalDateTime.now(), page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(List.of(bookingCurrent), bookings);
    }

    @Test
    void findByBookerIdAndEndIsBeforeOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(user2.getId(),
                LocalDateTime.now(), page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(List.of(bookingPast), bookings);
    }

    @Test
    void findByBookerIdAndStartIsAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(user2.getId(),
                LocalDateTime.now(), page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(List.of(bookingFuture), bookings);
    }

    @Test
    void findByBookerIdAndStatusIsOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(user2.getId(),
                BookingStatus.WAITING, page).getContent();

        assertEquals(2, bookings.size());
        assertEquals(List.of(bookingPast, bookingCurrent), bookings);

        bookings = bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(user2.getId(),
                BookingStatus.REJECTED, page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(List.of(bookingFuture), bookings);
    }

    @Test
    void findByItemOwnerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(user1.getId(), page).getContent();

        assertEquals(3, bookings.size());
        assertEquals(List.of(bookingFuture, bookingPast, bookingCurrent), bookings);
    }

    @Test
    void findByItemOwnerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(user1.getId(),
                LocalDateTime.now(), LocalDateTime.now(), page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(List.of(bookingCurrent), bookings);
    }

    @Test
    void findByItemOwnerIdAndEndIsBeforeOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(user1.getId(),
                LocalDateTime.now(), page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(List.of(bookingPast), bookings);
    }

    @Test
    void findByItemOwnerIdAndStartIsAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(user1.getId(),
                LocalDateTime.now(), page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(List.of(bookingFuture), bookings);
    }

    @Test
    void findByItemOwnerIdAndStatusIsOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStatusIsOrderByStartDesc(user1.getId(),
                BookingStatus.WAITING, page).getContent();

        assertEquals(2, bookings.size());
        assertEquals(List.of(bookingPast, bookingCurrent), bookings);

        bookings = bookingRepository.findByItemOwnerIdAndStatusIsOrderByStartDesc(user1.getId(),
                BookingStatus.REJECTED, page).getContent();

        assertEquals(1, bookings.size());
        assertEquals(List.of(bookingFuture), bookings);
    }

    @Test
    void findByItemIdAndStartIsBeforeOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findByItemIdAndStartIsBeforeOrderByStartDesc(item1.getId(),
                LocalDateTime.now());

        assertEquals(2, bookings.size());
        assertEquals(List.of(bookingPast, bookingCurrent), bookings);
    }

    @Test
    void findByItemIdAndStartIsAfterOrderByStartAsc() {
        List<Booking> bookings = bookingRepository.findByItemIdAndStartIsAfterOrderByStartAsc(item1.getId(),
                LocalDateTime.now());

        assertEquals(1, bookings.size());
        assertEquals(List.of(bookingFuture), bookings);
    }

    @Test
    void findAllByUserBookings() {
        List<Booking> bookings = bookingRepository.findAllByUserBookings(user1.getId(), item1.getId(), LocalDateTime.now());

        assertThat(bookings).isEmpty();
    }
}