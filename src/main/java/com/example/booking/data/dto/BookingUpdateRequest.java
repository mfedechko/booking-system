package com.example.booking.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * @author mfedechko
 */
@Getter
@AllArgsConstructor
public class BookingUpdateRequest {

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
