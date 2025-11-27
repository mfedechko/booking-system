package com.example.booking.service;

import com.example.booking.data.BookingStatus;
import com.example.booking.data.repository.BlockRepository;
import com.example.booking.data.repository.BookingRepository;
import com.example.booking.exception.DateRangeException;
import com.example.booking.exception.PropertyBookedException;

import java.time.LocalDate;
import java.util.List;

/**
 * @author mfedechko
 */
public class BookingService {

    protected final BookingRepository bookingRepository;
    protected final BlockRepository blockRepository;

    public BookingService(final BookingRepository bookingRepository,
                          final BlockRepository blockRepository) {
        this.bookingRepository = bookingRepository;
        this.blockRepository = blockRepository;
    }

    protected static void validateDateRange(final LocalDate startDate,
                                            final LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new DateRangeException("End date must be after start date");
        }
    }


    protected void checkIfPropertyBooked(final Long propertyId,
                                         final LocalDate startDate,
                                         final LocalDate endDate) {
        final var bookedProperties = getAllBookedProperties(startDate, endDate);
        if (bookedProperties.contains(propertyId)) {
            throw new PropertyBookedException(String.format("Booking already exists for property id %s", propertyId));
        }
    }

    private List<Long> getAllBookedProperties(final LocalDate startDate,
                                              final LocalDate endDate) {
        final var bookedProperties = bookingRepository.findBookedProperties(BookingStatus.ACTIVE, startDate, endDate);
        final var bookedBlocks = blockRepository.findBookedBlocks(startDate, endDate);
        bookedProperties.addAll(bookedBlocks);
        return bookedProperties;
    }
}
