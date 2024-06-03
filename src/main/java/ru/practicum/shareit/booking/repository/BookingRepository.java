package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByBookerIdOrderByStartDesc(long userId, Pageable pageable);

    Page<Booking> findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(long userId, LocalDateTime end,
                                                                              LocalDateTime start, Pageable pageable);

    Page<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end, Pageable pageable);

    Page<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findByBookerIdAndStatusIsOrderByStartDesc(long userId, BookingStatus status, Pageable pageable);

    Page<Booking> findByItemOwnerIdOrderByStartDesc(long userId, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(long userId, LocalDateTime end,
                                                                                 LocalDateTime start, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStatusIsOrderByStartDesc(long userId, BookingStatus status, Pageable pageable);

    List<Booking> findByItemIdAndStartIsBeforeOrderByStartDesc(long itemId, LocalDateTime start);

    List<Booking> findByItemIdAndStartIsAfterOrderByStartAsc(long itemId, LocalDateTime start);
    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime end);
}