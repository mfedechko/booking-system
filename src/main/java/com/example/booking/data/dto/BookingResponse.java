package com.example.booking.data.dto;

import com.example.booking.data.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author mfedechko
 */
@Getter
@Setter
public class BookingResponse {

    private Long id;
    private Long propertyId;
    private String guestName;
    private String guestEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus status;
}
