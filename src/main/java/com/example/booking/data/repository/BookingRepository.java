package com.example.booking.data.repository;

import com.example.booking.data.BookingStatus;
import com.example.booking.data.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select b from Booking b
            where b.propertyId = :propertyId
              and b.status <> :canceledStatus
              and b.startDate < :endDate
              and b.endDate > :startDate
            """)
    List<Booking> findOverlappingBookings(Long propertyId,
                                          BookingStatus canceledStatus,
                                          LocalDate startDate,
                                          LocalDate endDate);

    @Query("""
            select b from Booking b
            where b.propertyId = :propertyId
              and b.status <> :canceledStatus
              and b.id <> :excludeId
              and b.startDate < :endDate
              and b.endDate > :startDate
            """)
    List<Booking> findOverlappingBookingsExcluding(Long propertyId,
                                                   BookingStatus canceledStatus,
                                                   LocalDate startDate,
                                                   LocalDate endDate,
                                                   Long excludeId);

}
