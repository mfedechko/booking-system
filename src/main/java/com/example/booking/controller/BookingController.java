package com.example.booking.controller;

import com.example.booking.data.dto.BookingRequest;
import com.example.booking.data.dto.BookingResponse;
import com.example.booking.data.dto.BookingUpdateRequest;
import com.example.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mfedechko
 */
@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "Operations with bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Create a booking")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@Valid @RequestBody final BookingRequest request) {
        return bookingService.create(request);
    }

    @Operation(summary = "Get booking by id")
    @GetMapping("/{id}")
    public BookingResponse get(@PathVariable final Long id) {
        return bookingService.get(id);
    }

    @Operation(summary = "Update booking dates and guest details")
    @PutMapping("/{id}")
    public BookingResponse update(@PathVariable final Long id,
                                  @Valid @RequestBody final BookingUpdateRequest request) {
        return bookingService.update(id, request);
    }

    @Operation(summary = "Cancel a booking")
    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable final Long id) {
        bookingService.cancel(id);
    }

    @Operation(summary = "Rebook a canceled booking")
    @PostMapping("/{id}/rebook")
    public BookingResponse rebook(@PathVariable final Long id) {
        return bookingService.rebook(id);
    }

    @Operation(summary = "Delete a booking from the system")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        bookingService.delete(id);
    }
}
