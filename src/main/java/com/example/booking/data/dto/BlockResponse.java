package com.example.booking.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author mfedechko
 */
@Getter
@Setter
public class BlockResponse {

    private Long id;
    private Long propertyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}
