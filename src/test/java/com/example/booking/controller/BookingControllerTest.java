package com.example.booking.controller;

import com.example.booking.data.dto.BookingRequest;
import com.example.booking.data.dto.BookingUpdateRequest;
import com.example.booking.data.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingControllerTest extends BaseControllerTest {

    @Autowired
    private BookingRepository bookingRepository;


    @Test
    void givenNewBookingRequest_whenCallingController_shouldReturnCorrectResult() throws Exception {

        final var request = new BookingRequest(1L, "guest", "quest@mail.com", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10));

        final var result = mockMvc.perform(post("/api/bookings")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        final var responseStr = result.getResponse().getContentAsString();
        final var actual = jsonPrettyPrint(responseStr);

        final var expected = readJson("create-booking.json");
        assertEquals(expected, actual);

    }

    @Test
    void givenGetBookingRequest_whenCallingController_shouldReturnCorrectResult() throws Exception {
        createGuestBooking();
        final var result = mockMvc.perform(get("/api/bookings/1")
                                                   .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        final var responseStr = result.getResponse().getContentAsString();
        final var actual = jsonPrettyPrint(responseStr);

        final var expected = readJson("get-booking.json");
        assertEquals(expected, actual);
    }


    @Test
    void givenUpdateBookingRequest_whenCallingController_shouldReturnCorrectResult() throws Exception {
        createGuestBooking();
        final var updateReq = new BookingUpdateRequest("guest", "guest@mail.com", LocalDate.of(2026, 1, 20), LocalDate.of(2026, 1, 25));
        final var result = mockMvc.perform(put("/api/bookings/1")
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andReturn();
        final var responseStr = result.getResponse().getContentAsString();
        final var actual = jsonPrettyPrint(responseStr);

        final var expected = readJson("update-booking.json");
        assertEquals(expected, actual);
    }

    @Test
    void givenCancelBookingRequest_whenCallingController_shouldReturnCorrectResult() throws Exception {
        createGuestBooking();
        final var result = mockMvc.perform(post("/api/bookings/1/cancel?email=guest@mail.com")
                                                   .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        final var responseStr = result.getResponse().getContentAsString();
        final var actual = jsonPrettyPrint(responseStr);

        final var expected = readJson("cancel-booking.json");
        assertEquals(expected, actual);
    }


    @Test
    void givenRebookBookingRequest_whenCallingController_shouldReturnCorrectResult() throws Exception {
        createGuestBooking();
        mockMvc.perform(post("/api/bookings/1/cancel?email=guest@mail.com")
                                                   .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        final var result = mockMvc.perform(post("/api/bookings/1/rebook?email=guest@mail.com")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        final var responseStr = result.getResponse().getContentAsString();
        final var actual = jsonPrettyPrint(responseStr);

        final var expected = readJson("rebook-booking.json");
        assertEquals(expected, actual);
    }

}