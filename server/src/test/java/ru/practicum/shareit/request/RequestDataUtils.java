package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

public class RequestDataUtils {
    public static ItemRequest getItemRequest1() {
        return ItemRequest.builder()
                .description("request1 description")
                .build();
    }

    public static ItemRequest getItemRequest2() {
        return ItemRequest.builder()
                .description("request2 description")
                .build();
    }

    public static ItemRequestDto getItemRequestDto1() {
        return ItemRequestDto.builder()
                .description("request1 description")
                .build();
    }

    public static ItemRequestDto getItemRequestDto2() {
        return ItemRequestDto.builder()
                .description("request2 description")
                .build();
    }


    public static ItemRequest getItemRequestWithId() {
        return ItemRequest.builder()
                .id(1L)
                .description("request description")
                .created(LocalDateTime.now())
                .build();
    }

    public static ItemRequestDto getItemRequestDtoWithId() {
        return ItemRequestDto.builder()
                .id(1L)
                .description("request description")
                .created(LocalDateTime.now())
                .build();
    }

    public static ItemRequestDto getItemRequestDto() {
        return ItemRequestDto.builder()
                .id(1L)
                .description("request description")
                .created(LocalDateTime.now())
                .build();
    }
}
