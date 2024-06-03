package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private UserRepository userRepository;
    private ItemRequestRepository itemRequestRepository;
    private ItemRepository itemRepository;

    @Override
    public ItemRequestDto create(long userId, ItemRequestDto request) {
        User requester = checkRequesterNotFound(userId);
        request.setRequester(UserMapper.toUserDto(requester));
        request.setCreated(LocalDateTime.now());
        return RequestMapper.toRequestDto(itemRequestRepository.save(RequestMapper.toRequest(request)));
    }

    @Override
    public List<ItemRequestDto> get(long userId) {
        checkRequesterNotFound(userId);

        List<ItemRequestDto> requests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());

        return addItems(requests);
    }

    @Override
    public List<ItemRequestDto> getAll(long userId, int from, int size) {
        checkRequesterNotFound(userId);
        if (from < 0 || size < 0) {
            throw new ValidationException("from и size не могут быть меньше 0");
        }
        List<Long> ids = userRepository.findIds(userId);
        List<ItemRequestDto> requests = itemRequestRepository
                .findAllByRequesterIdIn(ids, PageRequest.of(from / size, size))
                .getContent().stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        return addItems(requests);
    }

    @Override
    public ItemRequestDto getById(long userId, long id) {
        checkRequesterNotFound(userId);
        ItemRequestDto request = RequestMapper.toRequestDto(itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос не найден")));
        return addItems(request);
    }

    private User checkRequesterNotFound(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private List<ItemRequestDto> addItems(List<ItemRequestDto> requests) {
        return requests.stream()
                .peek(this::addItems)
                .collect(Collectors.toList());
    }

    private ItemRequestDto addItems(ItemRequestDto request) {
        List<Item> items = itemRepository.findAllByRequestId(request.getId());
        request.setItems(items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList()));
        return request;
    }
}
