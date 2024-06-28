package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDataUtils;

public class ItemDataUtils {
    public static Item getItem1() {
        return Item.builder()
                .name("name1")
                .description("description1")
                .available(true)
                .build();
    }

    public static Item getItem2() {
        return Item.builder()
                .name("name2")
                .description("description2")
                .available(true)
                .build();
    }

    public static Item getItem3() {
        return Item.builder()
                .name("name3")
                .description("description3")
                .available(true)
                .build();
    }

    public static ItemDto getItemDto1(Long requestId) {
        return ItemDto.builder()
                .name("name1")
                .description("description1")
                .available(true)
                .requestId(requestId)
                .build();
    }

    public static ItemDto getItemDto2(Long requestId) {
        return ItemDto.builder()
                .name("name2")
                .description("description2")
                .available(true)
                .requestId(requestId)
                .build();
    }

    public static ItemDto getItemDto3(Long requestId) {
        return ItemDto.builder()
                .name("name3")
                .description("description3")
                .available(true)
                .requestId(requestId)
                .build();
    }


    public static Item getItemWithId() {
        return Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(UserDataUtils.getUser1WithId())
                .build();
    }

    public static ItemDto getItemDtoWithId() {
        return ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    public static ItemDtoGetResponse getItemResponseWithId() {
        return ItemDtoGetResponse.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
    }
}