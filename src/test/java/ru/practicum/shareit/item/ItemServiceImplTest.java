package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingDataUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.UserDataUtils;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ItemServiceImpl itemService;

    private final User user = UserDataUtils.getUser1WithId();
    private final Item item = ItemDataUtils.getItemWithId();
    private final ItemDtoGetResponse itemDtoGetResponse = ItemDataUtils.getItemResponseWithId();
    private final Comment comment = Comment.builder()
            .id(1L)
            .author(user)
            .text("comment")
            .item(item)
            .build();
    private final Booking booking = BookingDataUtils.getBookingWithId(user, item);
    private final long userId = user.getId();

    @Test
    void findAllUserItems() {
        int from = 0;
        int size = 10;
        when(itemRepository.findAllByOwnerId(userId, PageRequest.of(from, size))).thenReturn(Page.empty());

        List<ItemDtoGetResponse> items = itemService.findAllUserItems(userId, from, size);

        verify(itemRepository).findAllByOwnerId(userId, PageRequest.of(from, size));
    }

    @Test
    void findById() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(item.getId())).thenReturn(List.of());

        ItemDtoGetResponse actualItem = itemService.findById(userId, item.getId());

        assertEquals(item.getName(), actualItem.getName());
        verify(itemRepository).findById(item.getId());
    }

    @Test
    void updateDescription() {
        long id = item.getId();
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        ItemDto itemToUpdate = ItemDto.builder()
                .description("newDescription")
                .build();

        Item expectedItem = Item.builder()
                .id(1L)
                .name("name")
                .description("newDescription")
                .available(true)
                .owner(user)
                .build();

        when(itemRepository.save(expectedItem)).thenReturn(expectedItem);
        ItemDto updatedItem = itemService.update(userId, id, itemToUpdate);

        assertEquals(itemToUpdate.getDescription(), updatedItem.getDescription());
        verify(itemRepository).save(expectedItem);
    }

    @Test
    void updateName() {
        long id = item.getId();
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        ItemDto itemToUpdate = ItemDto.builder()
                .name("newName")
                .build();

        Item expectedItem = Item.builder()
                .id(1L)
                .name("newName")
                .description("description")
                .available(true)
                .owner(user)
                .build();

        when(itemRepository.save(expectedItem)).thenReturn(expectedItem);
        ItemDto updatedItem = itemService.update(userId, id, itemToUpdate);

        assertEquals(itemToUpdate.getName(), updatedItem.getName());
        verify(itemRepository).save(expectedItem);
    }

    @Test
    void updateAvailable() {
        long id = item.getId();
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        ItemDto itemToUpdate = ItemDto.builder()
                .available(false)
                .build();

        Item expectedItem = Item.builder()
                .id(1L)
                .name("newName")
                .description("description")
                .available(false)
                .owner(user)
                .build();

        when(itemRepository.save(any(Item.class))).thenReturn(expectedItem);
        ItemDto updatedItem = itemService.update(userId, id, itemToUpdate);

        assertEquals(itemToUpdate.getAvailable(), updatedItem.getAvailable());
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void updateIfUpdaterIsNotOwner() {
        ItemDto itemToUpdate = ItemDto.builder()
                .available(false)
                .build();
        assertThrows(NotFoundException.class, () ->
                itemService.update(3L, item.getId(), itemToUpdate));
    }

    @Test
    void save() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto itemDto = ItemMapper.toItemDto(item);
        ItemDto actualItem = itemService.save(itemDto, userId);

        assertEquals(itemDto, actualItem);
        verify(itemRepository).save(item);
    }

    @Test
    void search() {
        String text = "text";
        int from = 0;
        int size = 10;

        when(itemRepository.search(text, PageRequest.of(from, size))).thenReturn(new PageImpl<>(List.of(item)));

        List<ItemDto> items = itemService.search(text, from, size);
        assertEquals(ItemMapper.toItemDto(item), items.get(0));
        verify(itemRepository).search(text, PageRequest.of(from, size));
    }

    @Test
    void searchWhenTextIsEmpty() {
        String text = "";
        int from = 0;
        int size = 10;

        List<ItemDto> items = itemService.search(text, from, size);
        assertThat(items).isEmpty();
        verify(itemRepository, never()).search(text, PageRequest.of(from, size));
    }

    @Test
    void addComment() {
        CommentDto expectedComment = CommentMapper.toCommentDto(comment);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByUserBookings(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto commentDto = itemService.addComment(user.getId(), item.getId(), CommentMapper.toCommentDto(comment));
        assertEquals(expectedComment, commentDto);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void addCommentIfNotBooking() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () ->
                itemService.addComment(user.getId(), item.getId(), CommentMapper.toCommentDto(comment)));
        verify(commentRepository, never()).save(comment);
    }
}