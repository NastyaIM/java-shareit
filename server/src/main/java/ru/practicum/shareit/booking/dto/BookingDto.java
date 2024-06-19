package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    @EqualsAndHashCode.Exclude
    private LocalDateTime start;
    @EqualsAndHashCode.Exclude
    private LocalDateTime end;
    private Long itemId;
    private UserDto booker;
    private BookingStatus status;
}