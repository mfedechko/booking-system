package com.example.booking.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * @author mfedechko
 */
@Getter
public class BookingCancelRequest {

    @NotNull
    @Email
    private String guestEmail;

}
