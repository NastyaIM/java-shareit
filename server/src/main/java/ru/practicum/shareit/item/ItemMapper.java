package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner() != null ? UserMapper.toUserDto(item.getOwner()) : null,
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ItemDtoGetResponse toItemDtoBookings(Item item,
                                                       BookingDtoItem lastBooking, BookingDtoItem nextBooking,
                                                       List<Comment> comments) {
        return new ItemDtoGetResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                item.getOwner() != null ? UserMapper.toUserDto(item.getOwner()) : null,
                CommentMapper.toCommentsDto(comments)

        );
    }

    public static Item toItem(ItemDto item, ItemRequestDto request) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner() != null ? UserMapper.toUser(item.getOwner()) : null,
                request != null ? RequestMapper.toRequest(request) : null
        );
    }
}