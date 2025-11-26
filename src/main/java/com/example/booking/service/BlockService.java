package com.example.booking.service;

import com.example.booking.data.BookingStatus;
import com.example.booking.data.dto.BlockRequest;
import com.example.booking.data.dto.BlockResponse;
import com.example.booking.data.entity.Block;
import com.example.booking.data.repository.BlockRepository;
import com.example.booking.data.repository.BookingRepository;
import com.example.booking.exception.BlockNotFoundException;
import com.example.booking.exception.PropertyBookedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mfedechko
 */
@Service
public class BlockService {
    private final BlockRepository blockRepository;
    private final BookingRepository bookingRepository;

    public BlockService(BlockRepository blockRepository,
                        BookingRepository bookingRepository) {
        this.blockRepository = blockRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public BlockResponse create(BlockRequest request) {
        validateDateRange(request.getStartDate(), request.getEndDate());
        ensureNoOverlaps(request.getPropertyId(), request.getStartDate(), request.getEndDate(), null);

        Block block = new Block();
        block.setPropertyId(request.getPropertyId());
        block.setStartDate(request.getStartDate());
        block.setEndDate(request.getEndDate());
        block.setReason(request.getReason());

        return toResponse(blockRepository.save(block));
    }

    @Transactional
    public BlockResponse update(Long id, BlockRequest request) {
        validateDateRange(request.getStartDate(), request.getEndDate());

        Block block = blockRepository.findById(id)
                .orElseThrow(() -> new BlockNotFoundException(id));

        ensureNoOverlaps(block.getPropertyId(), request.getStartDate(), request.getEndDate(), block.getId());

        block.setStartDate(request.getStartDate());
        block.setEndDate(request.getEndDate());
        block.setReason(request.getReason());

        return toResponse(blockRepository.save(block));
    }

    @Transactional
    public void delete(Long id) {
        Block block = blockRepository.findById(id)
                .orElseThrow(() -> new BlockNotFoundException(id));
        blockRepository.delete(block);
    }

    private void validateDateRange(java.time.LocalDate start, java.time.LocalDate end) {
        if (!end.isAfter(start)) {
            throw new PropertyBookedException("End date must be after start date");
        }
    }

    private void ensureNoOverlaps(Long propertyId,
                                  java.time.LocalDate start,
                                  java.time.LocalDate end,
                                  Long excludeId) {
        var overlappingBlocks = blockRepository.findOverlappingBlocks(propertyId, start, end).stream()
                .filter(block -> excludeId == null || !block.getId().equals(excludeId))
                .toList();

        if (!overlappingBlocks.isEmpty()) {
            throw new PropertyBookedException("Block overlaps with another block");
        }

        var overlappingBookings = bookingRepository.findOverlappingBookings(
                propertyId, BookingStatus.CANCELED, start, end);

        if (!overlappingBookings.isEmpty()) {
            throw new PropertyBookedException("Block overlaps with active bookings");
        }
    }

    private BlockResponse toResponse(Block block) {
        BlockResponse response = new BlockResponse();
        response.setId(block.getId());
        response.setPropertyId(block.getPropertyId());
        response.setStartDate(block.getStartDate());
        response.setEndDate(block.getEndDate());
        response.setReason(block.getReason());
        return response;
    }
}
