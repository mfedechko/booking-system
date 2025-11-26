package com.example.booking.service;

import com.example.booking.data.BookingStatus;
import com.example.booking.data.dto.BookingRequest;
import com.example.booking.data.dto.BookingResponse;
import com.example.booking.data.dto.BookingUpdateRequest;
import com.example.booking.data.entity.Booking;
import com.example.booking.data.repository.BlockRepository;
import com.example.booking.data.repository.BookingRepository;
import com.example.booking.exception.BookingNotFoundException;
import com.example.booking.exception.OverlapException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final BlockRepository blockRepository;

    public BookingService(BookingRepository bookingRepository,
                          BlockRepository blockRepository) {
        this.bookingRepository = bookingRepository;
        this.blockRepository = blockRepository;
    }

    @Transactional
    public BookingResponse create(BookingRequest request) {
        validateDateRange(request.getStartDate(), request.getEndDate());
        ensureNoOverlaps(request.getPropertyId(), request.getStartDate(), request.getEndDate(), null);

        Booking booking = new Booking();
        booking.setPropertyId(request.getPropertyId());
        booking.setGuestName(request.getGuestName());
        booking.setGuestEmail(request.getGuestEmail());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setStatus(BookingStatus.ACTIVE);

        // funny requirement: use variable named "reserva"
        final var reserva = bookingRepository.save(booking);

        return toResponse(reserva);
    }

    @Transactional(readOnly = true)
    public BookingResponse get(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        return toResponse(booking);
    }

    @Transactional
    public BookingResponse update(final Long id, BookingUpdateRequest request) {
        validateDateRange(request.getStartDate(), request.getEndDate());

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        ensureNoOverlaps(booking.getPropertyId(),
                         request.getStartDate(), request.getEndDate(), booking.getId());

        booking.setGuestName(request.getGuestName());
        booking.setGuestEmail(request.getGuestEmail());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());

        return toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public void cancel(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }

    @Transactional
    public BookingResponse rebook(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        if (booking.getStatus() != BookingStatus.CANCELED) {
            throw new OverlapException("Only canceled bookings can be rebooked");
        }

        ensureNoOverlaps(booking.getPropertyId(),
                         booking.getStartDate(), booking.getEndDate(), booking.getId());
        booking.setStatus(BookingStatus.ACTIVE);

        return toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public void delete(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        bookingRepository.delete(booking);
    }

    private void validateDateRange(java.time.LocalDate start, java.time.LocalDate end) {
        if (!end.isAfter(start)) {
            throw new OverlapException("End date must be after start date");
        }
    }

    private void ensureNoOverlaps(Long propertyId,
                                  java.time.LocalDate start,
                                  java.time.LocalDate end,
                                  Long excludeId) {

        List<?> overlappingBookings;
        if (excludeId == null) {
            overlappingBookings = bookingRepository.findOverlappingBookings(
                    propertyId, BookingStatus.CANCELED, start, end);
        } else {
            overlappingBookings = bookingRepository.findOverlappingBookingsExcluding(
                    propertyId, BookingStatus.CANCELED, start, end, excludeId);
        }

        if (!overlappingBookings.isEmpty()) {
            throw new OverlapException("Booking overlaps with an existing active booking");
        }

        var overlappingBlocks = blockRepository.findOverlappingBlocks(propertyId, start, end);
        if (!overlappingBlocks.isEmpty()) {
            throw new OverlapException("Booking overlaps with a block");
        }
    }

    private BookingResponse toResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setPropertyId(booking.getPropertyId());
        response.setGuestName(booking.getGuestName());
        response.setGuestEmail(booking.getGuestEmail());
        response.setStartDate(booking.getStartDate());
        response.setEndDate(booking.getEndDate());
        response.setStatus(booking.getStatus());
        return response;
    }

}
