package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemDataUtils;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserDataUtils;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    private final User user = UserDataUtils.getUser1WithId();
    private final ItemRequest request = RequestDataUtils.getItemRequestWithId();
    private final Item item = ItemDataUtils.getItemWithId();
    private final long userId = user.getId();
    ItemRequestDto requestDto;

    @BeforeEach
    public void beforeEach() {
        request.setRequester(user);
        requestDto = RequestMapper.toRequestDto(request);
    }


    @Test
    void save() {
        when(itemRequestRepository.save(request)).thenReturn(request);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ItemRequestDto actualRequest = itemRequestService.save(userId, requestDto);

        assertEquals(requestDto, actualRequest);
        verify(itemRequestRepository).save(request);
    }

    @Test
    void findAllUserRequests() {
        requestDto.setItems(List.of(ItemMapper.toItemDto(item)));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId))
                .thenReturn(List.of(request));
        when(itemRepository.findAllByRequestId(request.getId())).thenReturn(List.of(item));

        List<ItemRequestDto> requests = itemRequestService.findAllUserRequests(userId);

        assertEquals(List.of(requestDto), requests);
        verify(itemRequestRepository).findAllByRequesterIdOrderByCreatedDesc(userId);
    }

    @Test
    void findAll() {
        int from = 0;
        int size = 10;
        List<Long> ids = List.of();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findIdsAllOtherUsers(userId)).thenReturn(ids);
        when(itemRequestRepository.findAllByRequesterIdIn(ids, PageRequest.of(from, size)))
                .thenReturn(Page.empty());

        List<ItemRequestDto> requests = itemRequestService.findAll(userId, from, size);

        assertEquals(List.of(), requests);
        verify(itemRequestRepository).findAllByRequesterIdIn(ids, PageRequest.of(from, size));
    }

    @Test
    void findById() {
        requestDto.setItems(List.of(ItemMapper.toItemDto(item)));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(itemRepository.findAllByRequestId(request.getId())).thenReturn(List.of(item));

        ItemRequestDto actualRequest = itemRequestService.findById(userId, request.getId());

        assertEquals(requestDto, actualRequest);
        verify(itemRequestRepository).findById(request.getId());
    }
}