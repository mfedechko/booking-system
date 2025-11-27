package com.example.booking.data.repository;

import com.example.booking.data.BookingStatus;
import com.example.booking.data.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Query("""
               select b.propertyId from Booking b
               where b.endDate >= :startDate 
               and b.startDate <= :endDate 
               and b.status = :status
           """)
    List<Long> findBookedProperties(BookingStatus status,
                                    LocalDate startDate,
                                    LocalDate endDate);

}
