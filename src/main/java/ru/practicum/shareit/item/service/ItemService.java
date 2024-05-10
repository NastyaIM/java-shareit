package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getAllUserItems(long userId);

    ItemDto getById(long id);

    ItemDto update(long userId, long id, ItemDto item);

    ItemDto create(ItemDto item, long userId);

    List<ItemDto> search(String query);
}