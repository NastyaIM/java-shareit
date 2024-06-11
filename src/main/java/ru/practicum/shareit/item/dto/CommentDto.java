package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private ItemDto item;
    private String authorName;
    @PastOrPresent
    @EqualsAndHashCode.Exclude
    private LocalDateTime created;
}