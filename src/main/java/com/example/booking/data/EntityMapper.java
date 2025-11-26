package com.example.booking.data;

import com.example.booking.data.dto.BookingResponse;
import com.example.booking.data.entity.Booking;
import lombok.experimental.UtilityClass;

/**
 * @author mfedechko
 */
@UtilityClass
public class EntityMapper {

    public static BookingResponse buildBookingResponse(final Booking bookingEntity) {
        final var response = new BookingResponse();
        response.setId(bookingEntity.getId());
        response.setPropertyId(bookingEntity.getPropertyId());
        response.setGuestName(bookingEntity.getGuestName());
        response.setGuestEmail(bookingEntity.getGuestEmail());
        response.setStartDate(bookingEntity.getStartDate());
        response.setEndDate(bookingEntity.getEndDate());
        response.setStatus(bookingEntity.getStatus());
        return response;
    }


}
