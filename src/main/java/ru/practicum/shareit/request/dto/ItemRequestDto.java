package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ItemRequestDto {
    @Positive
    private Long id;
    @NotNull
    private String description;
    private UserDto requester;
    @EqualsAndHashCode.Exclude
    private LocalDateTime created;
    private List<ItemDto> items;
}