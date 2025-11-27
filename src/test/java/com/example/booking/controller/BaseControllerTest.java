package com.example.booking.controller;

import com.example.booking.data.dto.BookingRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mfedechko
 */
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected void createGuestBooking() throws Exception {
        final var request = new BookingRequest(1L, "guest", "guest@mail.com", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 10));

        mockMvc.perform(post("/api/bookings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    protected String readJson(final String path) throws IOException {
        return new String(
                Objects.requireNonNull(
                        this.getClass().getClassLoader().getResourceAsStream("expected-responses/" + path)
                ).readAllBytes()
        );
    }

    protected String jsonPrettyPrint(final String json) throws JsonProcessingException {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        final var jsonObj = objectMapper.readValue(json, Object.class);
        return objectMapper.writeValueAsString(jsonObj);
    }

}
