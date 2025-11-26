package com.example.booking.data.repository;

import com.example.booking.data.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * @author mfedechko
 */
public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("""
            select bl from Block bl
            where bl.propertyId = :propertyId
              and bl.startDate < :endDate
              and bl.endDate > :startDate
            """)
    List<Block> findOverlappingBlocks(Long propertyId,
                                      LocalDate startDate,
                                      LocalDate endDate);
}
