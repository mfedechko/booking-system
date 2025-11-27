package com.example.booking.exception;

/**
 * @author mfedechko
 */
public class WrongGuestException extends RuntimeException {

    public WrongGuestException(final String message) {
        super(message);
    }
}
