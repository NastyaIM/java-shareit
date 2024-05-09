package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAllUserItems(long userId);

    Item getById(long id);

    Item update(long userId, long id, Item item);

    Item create(Item item, long userId);

    List<Item> search(String query);
}