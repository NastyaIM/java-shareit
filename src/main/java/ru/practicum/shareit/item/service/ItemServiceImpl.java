package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserService userService;

    @Override
    public List<ItemDto> getAllUserItems(long userId) {
        return itemRepository.getAllUserItems(userId);
    }

    @Override
    public ItemDto getById(long id) {
        checkNotFound(id);
        return itemRepository.getById(id);
    }

    @Override
    public ItemDto update(long userId, long id, ItemDto item) {
        checkNotFound(id);
        if (itemRepository.getById(id).getOwner().getId() != userId) {
            throw new NotFoundException("Редактировать предметы может только владелец");
        }
        return itemRepository.update(id, item);
    }

    @Override
    public ItemDto create(ItemDto item, long userId) {
        item.setOwner(userService.getById(userId));
        return itemRepository.create(item);
    }

    @Override
    public List<ItemDto> search(String query) {
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