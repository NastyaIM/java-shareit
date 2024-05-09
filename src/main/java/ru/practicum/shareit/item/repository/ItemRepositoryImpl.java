package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
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
    public List<Item> getAllUserItems(long userId) {
        return items.values().stream()
                .filter(itemDto -> itemDto.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Item getById(long id) {
        return items.get(id);
    }

    @Override
    public Item update(long id, Item item) {
        Item updatedItem = getById(id);
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        return updatedItem;
    }

    @Override
    public Item create(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> search(String query) {
        String lowerCaseQuery = query.toLowerCase();
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(lowerCaseQuery)
                        || item.getDescription().toLowerCase().contains(lowerCaseQuery))
                        && item.getAvailable())
                .collect(Collectors.toList());
    }

    private Long generateId() {
        return id++;
    }
}