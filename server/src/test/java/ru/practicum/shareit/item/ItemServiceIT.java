package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.RequestDataUtils;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserDataUtils;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemServiceIT {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private BookingServiceImpl bookingService;
    @Autowired
    private ItemRequestServiceImpl itemRequestService;
    @Autowired
    private UserServiceImpl userService;
    private UserDto user1;
    private UserDto user2;
    private ItemDto item1;
    private ItemDto item2;
    private ItemDto item3;
    private ItemRequestDto request;
    private CommentDto comment;
    private BookingDto bookingDto;
    private BookingDtoResponse booking;

    @BeforeEach
    public void beforeEach() {
        user1 = userService.save(UserDataUtils.getUserDto1());
        user2 = userService.save(UserDataUtils.getUserDto2());

        request = itemRequestService.save(user2.getId(), RequestDataUtils.getItemRequestDto());
        item1 = itemService.save(ItemDataUtils.getItemDto1(null), user1.getId());
        item2 = itemService.save(ItemDataUtils.getItemDto2(request.getId()), user1.getId());
        item3 = itemService.save(ItemDataUtils.getItemDto3(null), user2.getId());

        bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusSeconds(1L))
                .end(LocalDateTime.now().plusSeconds(2L))
                .build();

        booking = bookingService.save(user2.getId(), bookingDto);
        bookingService.approve(user1.getId(), booking.getId(), true);

        comment = CommentDto.builder()
                .text("comment")
                .build();
    }

    @Test
    void findAllUserItems() {
        int from = 0;
        int size = 10;

        List<ItemDtoGetResponse> items = itemService.findAllUserItems(user1.getId(), from, size);

        assertEquals(2, items.size());
        assertEquals(item1.getName(), items.get(0).getName());
        assertEquals(item1.getDescription(), items.get(0).getDescription());
    }

    @Test
    void findById() {
        ItemDtoGetResponse actualItem = itemService.findById(user1.getId(), item2.getId());

        assertEquals(item2.getName(), actualItem.getName());
        assertEquals(item2.getDescription(), actualItem.getDescription());
    }

    @Test
    void update() {
        ItemDto itemToUpdate = ItemDto.builder()
                .description("newDescription")
                .build();

        ItemDto updatedItem = itemService.update(user2.getId(), item3.getId(), itemToUpdate);

        assertEquals(itemToUpdate.getDescription(), updatedItem.getDescription());
    }

    @Test
    void save() {
        ItemDto actualItem = itemService.save(item1, user1.getId());

        assertEquals(item1, actualItem);
    }

    @Test
    void search() {
        String text = "name2";
        int from = 0;
        int size = 10;

        List<ItemDto> items = itemService.search(1L, text, from, size);

        assertEquals(1, items.size());
        assertEquals(text, items.get(0).getName());
    }

    @Test
    void addComment() throws InterruptedException {
        Thread.sleep(3000);
        CommentDto commentDto = itemService.addComment(user2.getId(), item1.getId(), comment);
        assertEquals(comment.getText(), commentDto.getText());
    }
}