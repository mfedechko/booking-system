package com.example.booking.exception;

/**
 * @author mfedechko
 */
public class WrongguestException extends RuntimeException {

    public WrongguestException(final String message) {
        super(message);
    }
}
