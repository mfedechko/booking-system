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
import com.example.booking.exception.WrongguestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.example.booking.util.DateUtils.validateDateRange;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BlockRepository blockRepository;

    public BookingService(final BookingRepository bookingRepository,
                          final BlockRepository blockRepository) {
        this.bookingRepository = bookingRepository;
        this.blockRepository = blockRepository;
    }

    @Transactional
    public BookingResponse create(final BookingRequest request) {

        final var startDate = request.getStartDate();
        final var endDate = request.getEndDate();
        validateDateRange(startDate, endDate);

        final var bookedProperties = getAllBookedProperties(startDate, endDate);

        final var propertyId = request.getPropertyId();
        checkIfPropertyBooked(bookedProperties, propertyId);

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

        final var booking = bookingRepository.findById(propertyId)
                .orElseThrow(() -> new BookingNotFoundException(propertyId));

        final var bookedProperties = getAllBookedProperties(startDate, endDate);
        checkIfPropertyBooked(bookedProperties, propertyId);

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

        if (booking.getGuestEmail().equals(guestEmail)) {
            throw new WrongguestException("Booking is created by another user");
        }

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
    public void delete(final Long id) {
        final var booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        bookingRepository.delete(booking);
    }


    private List<Long> getAllBookedProperties(final LocalDate startDate,
                                              final LocalDate endDate) {
        final var bookedProperties = bookingRepository.findBookedProperties(BookingStatus.ACTIVE, startDate, endDate);
        final var bookedBlocks = blockRepository.findBookedBlocks(startDate, endDate);
        bookedProperties.addAll(bookedBlocks);
        return bookedProperties;
    }

    private static void checkIfPropertyBooked(final List<Long> bookedProperties,
                                              final Long propertyId) {
        if (bookedProperties.contains(propertyId)) {
            throw new PropertyBookedException(String.format("Booking already exists for property id %s", propertyId));
        }
    }


}
