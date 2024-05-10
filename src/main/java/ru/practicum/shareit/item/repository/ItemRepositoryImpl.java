package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long id = 1;

    @Override
    public List<ItemDto> getAllUserItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(long id) {
        Item item = items.get(id);
        return item != null ? ItemMapper.toItemDto(item) : null;
    }

    @Override
    public ItemDto update(long id, ItemDto item) {
        Item updatedItem = items.get(id);
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto create(ItemDto item) {
        item.setId(generateId());
        items.put(item.getId(), ItemMapper.toItem(item));
        return item;
    }

    @Override
    public List<ItemDto> search(String query) {
        String lowerCaseQuery = query.toLowerCase();
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(lowerCaseQuery)
                        || item.getDescription().toLowerCase().contains(lowerCaseQuery))
                        && item.getAvailable())
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private Long generateId() {
        return id++;
    }
}