package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto save(long userId, ItemRequestDto request);

    List<ItemRequestDto> findAllUserRequests(long userId);

    List<ItemRequestDto> findAll(long userId, int from, int size);

    ItemRequestDto findById(long userId, long id);
}
