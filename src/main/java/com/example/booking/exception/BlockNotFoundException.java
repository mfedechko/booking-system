package com.example.booking.exception;

/**
 * @author mfedechko
 */
public class BlockNotFoundException extends RuntimeException {

    public BlockNotFoundException(final Long id) {
        super("Block not found: " + id);
    }
}
