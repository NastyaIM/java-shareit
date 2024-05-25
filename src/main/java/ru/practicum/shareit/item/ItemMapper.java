package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                UserMapper.toUserDto(item.getOwner())
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
                UserMapper.toUserDto(item.getOwner()),
                CommentMapper.toCommentsDto(comments)

        );
    }

    public static Item toItem(ItemDto item) {
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                UserMapper.toUser(item.getOwner())
        );
    }
}