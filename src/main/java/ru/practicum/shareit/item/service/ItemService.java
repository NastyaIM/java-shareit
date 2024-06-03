package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;

import java.util.List;

public interface ItemService {
    List<ItemDtoGetResponse> getAllUserItems(long userId, int from, int size);

    ItemDtoGetResponse getById(long userId, long id);

    ItemDto update(long userId, long id, ItemDto item);

    ItemDto create(ItemDto item, long userId);

    List<ItemDto> search(String query, int from, int size);

    CommentDto addComment(long userId, long id, CommentDto comment);
}