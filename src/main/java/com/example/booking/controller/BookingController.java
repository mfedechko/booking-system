package com.example.booking.controller;

import com.example.booking.data.dto.BookingCancelRequest;
import com.example.booking.data.dto.BookingRequest;
import com.example.booking.data.dto.BookingResponse;
import com.example.booking.data.dto.BookingUpdateRequest;
import com.example.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BookingResponse> create(@Valid @RequestBody final BookingRequest request) {
        final var response = bookingService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get booking by id")
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> get(@PathVariable final Long id) {
        final var bookingResponse = bookingService.get(id);
        return ResponseEntity.ok(bookingResponse);
    }

    @Operation(summary = "Update booking dates and guest details")
    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> update(@PathVariable final Long id,
                                                  @Valid @RequestBody final BookingUpdateRequest request) {
        final var response = bookingService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancel a booking")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancel(@PathVariable final Long id,
                                                  @Valid @RequestBody final BookingCancelRequest request) {
        final var response = bookingService.cancel(id, request.getGuestEmail());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Rebook a canceled booking")
    @PostMapping("/{id}/rebook")
    public ResponseEntity<BookingResponse> rebook(@PathVariable final Long id) {
        final var rebook = bookingService.rebook(id);
        return ResponseEntity.ok(rebook);
    }

    @Operation(summary = "Delete a booking from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        bookingService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
