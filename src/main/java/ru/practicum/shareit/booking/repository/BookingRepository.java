package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(long userId);

    List<Booking> findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(long userId, LocalDateTime end, LocalDateTime start);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime start);

    List<Booking> findByBookerIdAndStatusIsOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(long userId);

    List<Booking> findByItemOwnerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(long userId, LocalDateTime end, LocalDateTime start);

    List<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime start);

    List<Booking> findByItemOwnerIdAndStatusIsOrderByStartDesc(long userId, BookingStatus status);

    List<Booking> findByItemIdAndStartIsBeforeOrderByStartDesc(long itemId, LocalDateTime start);

    List<Booking> findByItemIdAndStartIsAfterOrderByStartAsc(long itemId, LocalDateTime start);
}