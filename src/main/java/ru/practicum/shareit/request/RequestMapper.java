package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;

public class RequestMapper {
    public static ItemRequestDto toRequestDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                UserMapper.toUserDto(request.getRequester()),
                request.getCreated()
        );
    }

    public static ItemRequest toRequest(ItemRequestDto request) {
        return new ItemRequest(
                request.getId(),
                request.getDescription(),
                UserMapper.toUser(request.getRequester()),
                request.getCreated()
        );
    }
}
