package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> getAllUserItems(long userId);

    Item getById(long id);

    Item update(long id, Item item);

    Item create(Item item);

    List<Item> search(String query);
}