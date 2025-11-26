package com.example.booking.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @author mfedechko
 */
@Getter
public class BookingRequest {

    @NotNull
    private Long propertyId;

    @NotNull
    private String guestName;

    @NotNull
    @Email
    private String guestEmail;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private LocalDate endDate;

}
