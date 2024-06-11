package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.PathConstants;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    private final BookingDto bookingDto = new BookingDto(1L,
            LocalDateTime.of(2024, Month.AUGUST, 15, 14, 14, 14),
            LocalDateTime.of(2024, Month.AUGUST, 18, 14, 14, 14),
            null, null, BookingStatus.WAITING);
    private final BookingDtoResponse bookingDtoResponse = BookingMapper
            .toBookingDtoResponse(BookingMapper.toBooking(bookingDto, null));
    private final long userId = 1L;

    @SneakyThrows
    @Test
    void save() {
        when(bookingService.save(userId, bookingDto)).thenReturn(bookingDtoResponse);

        mockMvc.perform(post(PathConstants.BOOKINGS)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoResponse.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDtoResponse.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())));

        verify(bookingService).save(userId, bookingDto);
    }

    @SneakyThrows
    @Test
    void approve() {
        long id = bookingDto.getId();
        boolean approved = true;
        bookingDtoResponse.setStatus(BookingStatus.APPROVED);

        when(bookingService.approve(userId, id, approved)).thenReturn(bookingDtoResponse);

        mockMvc.perform(patch(PathConstants.BOOKINGS + PathConstants.BY_ID, id)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", String.valueOf(approved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())));

        verify(bookingService).approve(userId, id, approved);
    }

    @SneakyThrows
    @Test
    void findById() {
        long id = bookingDto.getId();

        when(bookingService.findById(userId, id)).thenReturn(bookingDtoResponse);

        mockMvc.perform(get(PathConstants.BOOKINGS + PathConstants.BY_ID, id)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoResponse.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDtoResponse.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())));

        verify(bookingService).findById(userId, id);
    }

    @SneakyThrows
    @Test
    void findAll() {
        String state = "ALL";
        int from = 0;
        int size = 10;
        when(bookingService.findAll(userId, state, from, size)).thenReturn(List.of(bookingDtoResponse));

        mockMvc.perform(get(PathConstants.BOOKINGS)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDtoResponse.getStart().toString())))
                .andExpect(jsonPath("$[0].end", is(bookingDtoResponse.getEnd().toString())))
                .andExpect(jsonPath("$[0].status", is(bookingDtoResponse.getStatus().toString())));

        verify(bookingService).findAll(userId, state, from, size);
    }

    @SneakyThrows
    @Test
    void findAllOwner() {
        String state = "ALL";
        int from = 0;
        int size = 10;
        when(bookingService.findAllOwner(userId, state, from, size)).thenReturn(List.of());

        mockMvc.perform(get(PathConstants.BOOKINGS + PathConstants.BOOKINGS_OWNER)
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());

        verify(bookingService).findAllOwner(userId, state, from, size);
    }
}