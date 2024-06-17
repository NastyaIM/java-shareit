package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDtoItem {
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
    private ItemDto item;
    private Long bookerId;
    private BookingStatus status;
}