package com.example.booking.exception;

/**
 * @author mfedechko
 */
public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(final Long id) {
        super("Booking not found: " + id);
    }
}
