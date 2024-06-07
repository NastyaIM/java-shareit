package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Checks;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private ItemRequestRepository itemRequestRepository;

    @Override
    public List<ItemDtoGetResponse> findAllUserItems(long userId, int from, int size) {
        Checks.CheckPageParams(from, size);

        Page<Item> items = itemRepository.findAllByOwnerId(userId, PageRequest.of(from / size, size));
        return items.getContent().stream()
                .map(item -> {
                    BookingDtoItem nextBooking = getNextBooking(item.getId());
                    BookingDtoItem lastBooking = getLastBooking(item.getId());
                    List<Comment> comments = commentRepository.findByItemId(item.getId());
                    return ItemMapper.toItemDtoBookings(item, lastBooking, nextBooking, comments);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemDtoGetResponse findById(long userId, long id) {
        Item item = checkNotFound(id);

        BookingDtoItem nextBooking = null;
        BookingDtoItem lastBooking = null;
        if (item.getOwner().getId() == userId) {
            nextBooking = getNextBooking(id);
            lastBooking = getLastBooking(id);
        }

        List<Comment> comments = commentRepository.findByItemId(id);

        return ItemMapper.toItemDtoBookings(item, lastBooking, nextBooking, comments);
    }

    @Override
    public ItemDto update(long userId, long id, ItemDto itemDto) {
        Item item = checkNotFound(id);
        if (item.getOwner().getId() != userId) {
            throw new NotFoundException("Редактировать предметы может только владелец");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        Item it = itemRepository.save(item);
        return ItemMapper.toItemDto(it);
    }

    @Override
    public ItemDto save(ItemDto itemDto, long userId) {
        itemDto.setOwner(UserMapper.toUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"))));

        ItemRequestDto requestDto = null;

        if (itemDto.getRequestId() != null) {
            ItemRequest request = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException(""));
            requestDto = RequestMapper.toRequestDto(request);
        }

        Item item = itemRepository.save(ItemMapper.toItem(itemDto, requestDto));

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> search(String text, int from, int size) {
        if (text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        Checks.CheckPageParams(from, size);

        return itemRepository.search(text, PageRequest.of(from, size)).getContent()
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long userId, long id, CommentDto comment) {
        Item item = checkNotFound(id);
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<Booking> bookings = bookingRepository.findAllByUserBookings(userId, item.getId(), LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new ValidationException("Пользователь еще не бронировал этот предмет");
        }
        comment.setItem(ItemMapper.toItemDto(item));
        comment.setAuthorName(author.getName());
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(comment, author)));
    }

    private Item checkNotFound(long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Предмет с таким id не существует"));
    }

    private BookingDtoItem getLastBooking(long id) {
        List<Booking> bookings = bookingRepository.findByItemIdAndStartIsBeforeOrderByStartDesc(id, LocalDateTime.now());
        return checkBookings(bookings);
    }

    private BookingDtoItem getNextBooking(long id) {
        List<Booking> bookings = bookingRepository.findByItemIdAndStartIsAfterOrderByStartAsc(id, LocalDateTime.now());
        return checkBookings(bookings);
    }

    private BookingDtoItem checkBookings(List<Booking> bookings) {
        if (!bookings.isEmpty()) {
            BookingDtoItem bookingDtoItem = BookingMapper.toBookingDtoItem(bookings.get(0));
            if (bookingDtoItem.getStatus() == BookingStatus.REJECTED) {
                return null;
            }
            return bookingDtoItem;
        }
        return null;
    }
}