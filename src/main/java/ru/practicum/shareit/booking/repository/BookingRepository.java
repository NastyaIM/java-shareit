package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "select b.* from bookings as b " +
            "join items as i on i.id = b.item_id " +
            "where b.booker_id = ?1 and i.id = ?2 " +
            "and b.status = 'APPROVED' " +
            "and b.end_date < ?3 ", nativeQuery = true)
    List<Booking> findAllByUserBookings(long userId, long id, LocalDateTime end);
}