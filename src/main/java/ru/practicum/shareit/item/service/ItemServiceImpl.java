package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserService userService;

    @Override
    public List<Item> getAllUserItems(long userId) {
        return itemRepository.getAllUserItems(userId);
    }

    @Override
    public Item getById(long id) {
        checkNotFound(id);
        return itemRepository.getById(id);
    }

    @Override
    public Item update(long userId, long id, Item item) {
        checkNotFound(id);
        if (itemRepository.getById(id).getOwner().getId() != userId) {
            throw new NotFoundException("Редактировать предметы может только владелец");
        }
        return itemRepository.update(id, item);
    }

    @Override
    public Item create(Item item, long userId) {
        item.setOwner(userService.getById(userId));
        return itemRepository.create(item);
    }

    @Override
    public List<Item> search(String query) {
        if (query.isEmpty() || query.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(query);
    }

    private void checkNotFound(long id) {
        if (itemRepository.getById(id) == null) {
            throw new NotFoundException("Предмета с таким id не существует");
        }
    }
}