package com.example.booking.data;

import com.example.booking.data.dto.BlockResponse;
import com.example.booking.data.dto.BookingResponse;
import com.example.booking.data.entity.Block;
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

    public static BlockResponse buildBlockResponse(final Block block) {
        final var response = new BlockResponse();
        response.setId(block.getId());
        response.setPropertyId(block.getPropertyId());
        response.setStartDate(block.getStartDate());
        response.setEndDate(block.getEndDate());
        response.setReason(block.getReason());
        return response;
    }


}
