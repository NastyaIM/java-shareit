package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private ItemDto item;
    private String authorName;
    @EqualsAndHashCode.Exclude
    private LocalDateTime created;
}