package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class BookingDto {
    @Positive
    private Long id;
    @FutureOrPresent
    @NotNull
    @EqualsAndHashCode.Exclude
    private LocalDateTime start;
    @FutureOrPresent
    @NotNull
    @EqualsAndHashCode.Exclude
    private LocalDateTime end;
    private Long itemId;
    private UserDto booker;
    private BookingStatus status;
}