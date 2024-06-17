package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemDataUtils;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserDataUtils;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    private final User user1 = UserDataUtils.getUser1WithId();
    private final User user2 = UserDataUtils.getUser2WithId();
    private final Item item = ItemDataUtils.getItemWithId();
    private final Booking booking = BookingDataUtils.getBookingWithId(user2, item);

    @Test
    void save() {
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        bookingService.save(user2.getId(), bookingDto);

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void saveIfItemIsNotAvailable() {
        item.setAvailable(false);
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        assertThrows(ValidationException.class, () -> bookingService.save(user2.getId(), bookingDto));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void saveIfBookerIsOwner() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        assertThrows(NotFoundException.class, () -> bookingService.save(user1.getId(), bookingDto));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approve() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        Booking expectedBooking = Booking.builder()
                .id(1L)
                .status(BookingStatus.APPROVED)
                .booker(user2)
                .item(item)
                .end(LocalDateTime.of(2022, 12, 11, 12, 12, 12))
                .start(LocalDateTime.of(2021, 12, 11, 12, 12, 12))
                .build();
        when(bookingRepository.save(any(Booking.class))).thenReturn(expectedBooking);

        BookingDtoResponse actualBooking = bookingService.approve(user1.getId(), booking.getId(), true);

        assertEquals(expectedBooking.getStatus(), actualBooking.getStatus());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void approveIfBookingStatusIsApproved() {
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        assertThrows(ValidationException.class, () -> bookingService.approve(user1.getId(), booking.getId(), true));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approveIfUserIsNotOwner() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        assertThrows(NotFoundException.class, () -> bookingService.approve(user2.getId(), booking.getId(), true));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void findById() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        BookingDtoResponse actualBooking = bookingService.findById(user1.getId(), booking.getId());

        assertEquals(booking.getId(), actualBooking.getId());
        assertEquals(booking.getStatus(), actualBooking.getStatus());
        verify(bookingRepository).findById(booking.getId());
    }

    @Test
    void findAllWhenStateIsAll() {
        BookingState state = BookingState.ALL;
        int from = 0;
        int size = 10;
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookingRepository.findByBookerIdOrderByStartDesc(user1.getId(), PageRequest.of(from, size)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDtoResponse> bookings = bookingService.findAll(user1.getId(), state, from, size);

        verify(bookingRepository).findByBookerIdOrderByStartDesc(user1.getId(), PageRequest.of(from, size));
    }

    @Test
    void findAllWhenStateIsCurrent() {
        BookingState state = BookingState.CURRENT;
        int from = 0;
        int size = 10;
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDtoResponse> bookings = bookingService.findAll(user1.getId(), state, from, size);

        verify(bookingRepository).findByBookerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void findAllWhenStateIsPast() {
        BookingState state = BookingState.PAST;
        int from = 0;
        int size = 10;
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDtoResponse> bookings = bookingService.findAll(user1.getId(), state, from, size);

        verify(bookingRepository).findByBookerIdAndEndIsBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void findAllWhenStateIsFuture() {
        BookingState state = BookingState.FUTURE;
        int from = 0;
        int size = 10;
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDtoResponse> bookings = bookingService.findAll(user1.getId(), state, from, size);

        verify(bookingRepository).findByBookerIdAndStartIsAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void findAllWhenStateIsWaitingOrRejected() {
        int from = 0;
        int size = 10;
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookingRepository.findByBookerIdAndStatusIsOrderByStartDesc(anyLong(),
                any(BookingStatus.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        bookingService.findAll(user1.getId(), BookingState.WAITING, from, size);
        bookingService.findAll(user1.getId(), BookingState.REJECTED, from, size);

        verify(bookingRepository, times(2)).findByBookerIdAndStatusIsOrderByStartDesc(anyLong(),
                any(BookingStatus.class), any(PageRequest.class));
    }

    @Test
    void findAllOwnerWhenStateIsAll() {
        BookingState state = BookingState.ALL;
        int from = 0;
        int size = 10;
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(user1.getId(), PageRequest.of(from, size)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDtoResponse> bookings = bookingService.findAllOwner(user1.getId(), state, from, size);

        verify(bookingRepository).findByItemOwnerIdOrderByStartDesc(user1.getId(), PageRequest.of(from, size));
    }

    @Test
    void findAllOwnerWhenStateIsCurrent() {
        BookingState state = BookingState.CURRENT;
        int from = 0;
        int size = 10;
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookingRepository.findByItemOwnerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDtoResponse> bookings = bookingService.findAllOwner(user1.getId(), state, from, size);

        verify(bookingRepository).findByItemOwnerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void findAllOwnerWhenStateIsPast() {
        BookingState state = BookingState.PAST;
        int from = 0;
        int size = 10;
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookingRepository.findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDtoResponse> bookings = bookingService.findAllOwner(user1.getId(), state, from, size);

        verify(bookingRepository).findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void findAllOwnerWhenStateIsFuture() {
        BookingState state = BookingState.FUTURE;
        int from = 0;
        int size = 10;
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookingRepository.findByItemOwnerIdAndStartIsAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        List<BookingDtoResponse> bookings = bookingService.findAllOwner(user1.getId(), state, from, size);

        verify(bookingRepository).findByItemOwnerIdAndStartIsAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    void findAllOwnerWhenStateIsWaitingOrRejected() {
        int from = 0;
        int size = 10;
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(bookingRepository.findByItemOwnerIdAndStatusIsOrderByStartDesc(anyLong(),
                any(BookingStatus.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking)));

        bookingService.findAllOwner(user1.getId(), BookingState.WAITING, from, size);
        bookingService.findAllOwner(user1.getId(), BookingState.REJECTED, from, size);

        verify(bookingRepository, times(2)).findByItemOwnerIdAndStatusIsOrderByStartDesc(anyLong(),
                any(BookingStatus.class), any(PageRequest.class));
    }
}