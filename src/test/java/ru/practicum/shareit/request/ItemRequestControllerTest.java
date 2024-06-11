package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserDataUtils;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private final UserDto userDto = UserDataUtils.getUserDtoWithId();
    private final ItemRequestDto request = RequestDataUtils.getItemRequestDtoWithId();
    private final long userId = userDto.getId();

    @SneakyThrows
    @Test
    void save() {
        when(itemRequestService.save(userId, request)).thenReturn(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(request.getId()), Long.class))
                .andExpect(jsonPath("$.description", Matchers.is(request.getDescription())));

        verify(itemRequestService).save(userId, request);
    }

    @SneakyThrows
    @Test
    void findAllUserRequests() {
        when(itemRequestService.findAllUserRequests(userId)).thenReturn(List.of(request));

        mockMvc.perform(get(PathConstants.REQUESTS)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(request.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", Matchers.is(request.getDescription())));

        verify(itemRequestService).findAllUserRequests(userId);
    }

    @SneakyThrows
    @Test
    void findAll() {
        int from = 0;
        int size = 10;
        when(itemRequestService.findAll(userId, from, size)).thenReturn(List.of());

        mockMvc.perform(get(PathConstants.REQUESTS + PathConstants.ALL)
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(itemRequestService).findAll(userId, from, size);

    }

    @SneakyThrows
    @Test
    void findById() {
        long id = request.getId();
        when(itemRequestService.findById(userId, id)).thenReturn(request);

        mockMvc.perform(get(PathConstants.REQUESTS + PathConstants.BY_ID, id)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(id), Long.class))
                .andExpect(jsonPath("$.description", Matchers.is(request.getDescription())));

        verify(itemRequestService).findById(userId, id);
    }
}