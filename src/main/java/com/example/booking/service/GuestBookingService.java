package com.example.booking.service;

import com.example.booking.data.BookingStatus;
import com.example.booking.data.EntityMapper;
import com.example.booking.data.dto.BookingRequest;
import com.example.booking.data.dto.BookingResponse;
import com.example.booking.data.dto.BookingUpdateRequest;
import com.example.booking.data.entity.Booking;
import com.example.booking.data.repository.BlockRepository;
import com.example.booking.data.repository.BookingRepository;
import com.example.booking.exception.BookingNotFoundException;
import com.example.booking.exception.PropertyBookedException;
import com.example.booking.exception.WrongGuestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestBookingService extends BookingService {


    public GuestBookingService(final BookingRepository bookingRepository,
                               final BlockRepository blockRepository) {
        super(bookingRepository, blockRepository);
    }

    @Transactional
    public BookingResponse create(final BookingRequest request) {

        final var startDate = request.getStartDate();
        final var endDate = request.getEndDate();
        validateDateRange(startDate, endDate);
        checkIfPropertyBooked(request.getPropertyId(), startDate, endDate);

        final var booking = new Booking();
        booking.setPropertyId(request.getPropertyId());
        booking.setGuestName(request.getGuestName());
        booking.setGuestEmail(request.getGuestEmail());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setStatus(BookingStatus.ACTIVE);

        return EntityMapper.buildBookingResponse(booking);
    }

    @Transactional(readOnly = true)
    public BookingResponse get(final Long id) {
        return bookingRepository.findById(id)
                .map(EntityMapper::buildBookingResponse)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    @Transactional
    public BookingResponse update(final Long propertyId,
                                  final BookingUpdateRequest request) {
        final var startDate = request.getStartDate();
        final var endDate = request.getEndDate();

        validateDateRange(startDate, endDate);
        checkIfPropertyBooked(propertyId, startDate, endDate);

        final var booking = bookingRepository.findById(propertyId)
                .orElseThrow(() -> new BookingNotFoundException(propertyId));

        checkBookingUser(request.getGuestEmail(), booking);

        booking.setGuestName(request.getGuestName());
        booking.setGuestEmail(request.getGuestEmail());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        bookingRepository.save(booking);

        return EntityMapper.buildBookingResponse(booking);
    }

    @Transactional
    public BookingResponse cancel(final Long id, final String guestEmail) {
        final var booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        checkBookingUser(guestEmail, booking);

        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
        return EntityMapper.buildBookingResponse(booking);
    }

    @Transactional
    public BookingResponse rebook(final Long id) {
        final var booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        if (booking.getStatus() != BookingStatus.CANCELED) {
            throw new PropertyBookedException("Only canceled bookings can be rebooked");
        }

        validateDateRange(booking.getStartDate(), booking.getEndDate());

        booking.setStatus(BookingStatus.ACTIVE);
        bookingRepository.save(booking);
        return EntityMapper.buildBookingResponse(booking);
    }

    @Transactional
    public void delete(final Long id, final String email) {
        final var booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        checkBookingUser(email, booking);
        bookingRepository.delete(booking);
    }

    private static void checkBookingUser(final String guestEmail,
                                         final Booking booking) {
        if (!booking.getGuestEmail().equals(guestEmail)) {
            throw new WrongGuestException("Booking is created by another user");
        }
    }
}
