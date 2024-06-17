package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private final ItemDto itemDto = ItemDataUtils.getItemDtoWithId();
    private final ItemDtoGetResponse itemDtoGetResponse = ItemDataUtils.getItemResponseWithId();

    private final long userId = 1L;

    @SneakyThrows
    @Test
    void findAllUserItems() {
        int from = 0;
        int size = 10;
        when(itemService.findAllUserItems(userId, from, size)).thenReturn(List.of(itemDtoGetResponse));

        mockMvc.perform(get(PathConstants.ITEMS)
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", Matchers.is(itemDtoGetResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", Matchers.is(itemDtoGetResponse.getName())))
                .andExpect(jsonPath("$[0].description", Matchers.is(itemDtoGetResponse.getDescription())));

        verify(itemService).findAllUserItems(userId, from, size);
    }

    @SneakyThrows
    @Test
    void findById() {
        long id = itemDtoGetResponse.getId();

        when(itemService.findById(userId, id)).thenReturn(itemDtoGetResponse);

        mockMvc.perform(get(PathConstants.ITEMS + PathConstants.BY_ID, id)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(id), Long.class))
                .andExpect(jsonPath("$.name", Matchers.is(itemDtoGetResponse.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(itemDtoGetResponse.getDescription())));

        verify(itemService).findById(userId, id);
    }

    @SneakyThrows
    @Test
    void update() {
        long id = itemDto.getId();

        ItemDto itemToUpdate = new ItemDto();
        itemToUpdate.setDescription("newDescription");
        ItemDto updatedItem = new ItemDto(1L, "name",
                "newDescription", true, new UserDto(), null);

        when(itemService.update(userId, id, itemToUpdate)).thenReturn(updatedItem);

        mockMvc.perform(patch(PathConstants.ITEMS + PathConstants.BY_ID, id)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedItem.getName())))
                .andExpect(jsonPath("$.description", is(itemToUpdate.getDescription())));

        verify(itemService).update(userId, id, itemToUpdate);
    }

    @SneakyThrows
    @Test
    void save() {
        when(itemService.save(itemDto, userId)).thenReturn(itemDto);

        mockMvc.perform(post(PathConstants.ITEMS)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));

        verify(itemService).save(itemDto, userId);
    }

    @SneakyThrows
    @Test
    void search() {
        String query = "query";
        int from = 0;
        int size = 10;
        when(itemService.search(1L, query, from, size)).thenReturn(List.of(itemDto));

        mockMvc.perform(get(PathConstants.ITEMS + PathConstants.ITEMS_SEARCH)
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", query)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())));

        verify(itemService).search(1L, query, from, size);
    }

    @SneakyThrows
    @Test
    void addComment() {
        long id = itemDto.getId();
        CommentDto comment = new CommentDto(1L, "comment", itemDto,
                "author", LocalDateTime.now());

        when(itemService.addComment(userId, id, comment)).thenReturn(comment);

        mockMvc.perform(post(PathConstants.ITEMS + PathConstants.BY_ID + PathConstants.ITEMS_COMMENTS, id)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", Matchers.is(comment.getText())));

        verify(itemService).addComment(userId, id, comment);
    }
}