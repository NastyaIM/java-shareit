package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    List<ItemDto> getAllUserItems(long userId);

    ItemDto getById(long id);

    ItemDto update(long id, ItemDto item);

    ItemDto create(ItemDto item);

    List<ItemDto> search(String query);
}