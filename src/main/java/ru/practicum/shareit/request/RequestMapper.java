package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;

public class RequestMapper {
    public static ItemRequestDto toRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requester(request.getRequester() != null ?
                        UserMapper.toUserDto(request.getRequester()) : null)
                .created(request.getCreated())
                .items(new ArrayList<>())
                .build();
    }

    public static ItemRequest toRequest(ItemRequestDto request) {
        return ItemRequest.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requester(request.getRequester() != null ?
                        UserMapper.toUser(request.getRequester()) : null)
                .created(request.getCreated())
                .build();
    }
}