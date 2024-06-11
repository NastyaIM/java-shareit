package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserDataUtils;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRequestServiceIT {
    @Autowired
    private ItemRequestServiceImpl itemRequestService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ItemServiceImpl itemService;
    private UserDto user1;
    private UserDto user2;
    private ItemRequestDto request1;
    private ItemRequestDto request2;

    @BeforeEach
    public void beforeEach() {
        user1 = userService.save(UserDataUtils.getUserDto1());
        user2 = userService.save(UserDataUtils.getUserDto2());
        request1 = itemRequestService.save(user2.getId(), RequestDataUtils.getItemRequestDto1());
        request2 = itemRequestService.save(user1.getId(), RequestDataUtils.getItemRequestDto2());
    }

    @Test
    void findAllUserRequests() {
        List<ItemRequestDto> requests = itemRequestService.findAllUserRequests(user2.getId());

        assertThat(requests).isNotEmpty();
        assertEquals(1, requests.size());
        assertEquals(1L, request1.getId());
        assertEquals(user2, requests.get(0).getRequester());
    }

    @Test
    void findAll() {
        int from = 0;
        int size = 10;

        List<ItemRequestDto> requests = itemRequestService.findAll(2, from, size);

        assertThat(requests).isNotEmpty();
        assertEquals(1, requests.size());
        assertEquals(1L, request1.getId());
        assertEquals(user1, requests.get(0).getRequester());
    }

    @Test
    void findById() {
        ItemRequestDto actualRequest = itemRequestService.findById(1L, 2L);

        assertEquals(request2, actualRequest);
    }
}
