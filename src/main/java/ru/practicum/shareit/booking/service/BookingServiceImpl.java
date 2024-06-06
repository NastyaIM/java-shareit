package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;

    @Override
    public BookingDtoResponse save(long userId, BookingDto bookingDto) {
        checkDateTime(bookingDto.getStart(), bookingDto.getEnd());

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        if (booker.getId().equals(item.getOwner().getId())) {
            throw new NotFoundException("Владелец вещи не может ее забронировать");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Предмет нельзя забронировать");
        }

        bookingDto.setBooker(UserMapper.toUserDto(booker));
        bookingDto.setStatus(BookingStatus.WAITING);
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto, item));

        return BookingMapper.toBookingDtoResponse(booking);
    }

    @Override
    public BookingDtoResponse approve(long userId, long id, boolean approved) {
        Booking booking = checkNotFound(id);
        Optional<User> owner = userRepository.findById(userId);

        if (!(owner.isPresent() && userId == booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Подтверждать бронирование может только владелец вещи");
        }
        if (approved && booking.getStatus() == BookingStatus.APPROVED) {
            throw new ValidationException("Нельзя подтвердить подтвержденное бронирование");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoResponse findById(long userId, long id) {
        Booking booking = checkNotFound(id);

        if (!(userRepository.findById(userId).isPresent()
                && (userId == booking.getItem().getOwner().getId())
                || userId == booking.getBooker().getId())) {
            throw new NotFoundException("Просматривать информацию о бронировании " +
                    "может только владелец вещи или автор бронирования");
        }

        return BookingMapper.toBookingDtoResponse(booking);
    }

    @Override
    public List<BookingDtoResponse> findAll(long userId, String state, int from, int size) {
        checkPageParams(from, size);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Page<Booking> bookings;
        PageRequest page = PageRequest.of(from / size, size);
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId, page);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId,
                        LocalDateTime.now(), page);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), page);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(userId,
                        BookingStatus.WAITING, page);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(userId,
                        BookingStatus.REJECTED, page);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings.getContent().stream()
                .map(BookingMapper::toBookingDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoResponse> findAllOwner(long userId, String state, int from, int size) {
        checkPageParams(from, size);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Page<Booking> bookings;
        PageRequest page = PageRequest.of(from / size, size);
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(userId, page);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItemOwnerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now(), page);
                break;
            case "PAST":
                bookings = bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(userId,
                        LocalDateTime.now(), page);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), page);
                break;
            case "WAITING":
                bookings = bookingRepository.findByItemOwnerIdAndStatusIsOrderByStartDesc(userId,
                        BookingStatus.WAITING, page);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByItemOwnerIdAndStatusIsOrderByStartDesc(userId,
                        BookingStatus.REJECTED, page);
                break;
            default:
                throw new ValidationException("Unknown state: " + state);
        }
        return bookings.getContent().stream()
                .map(BookingMapper::toBookingDtoResponse)
                .collect(Collectors.toList());
    }

    private void checkDateTime(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || end.isEqual(start)) throw new ValidationException("Неправильное время");
    }

    private Booking checkNotFound(long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
    }

    private void checkPageParams(int from, int size) {
        if (from < 0 || size < 0) {
            throw new ValidationException("from и size не могут быть меньше 0");
        }
    }
}