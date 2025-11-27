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
           select b.propertyId  from Block b
           where b.endDate >= :startDate 
           and b.startDate <= :endDate 
           """)
    List<Long> findBookedBlocks(LocalDate startDate,
                               LocalDate endDate);
}
