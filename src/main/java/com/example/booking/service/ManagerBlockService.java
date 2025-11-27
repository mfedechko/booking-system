package com.example.booking.service;

import com.example.booking.data.EntityMapper;
import com.example.booking.data.dto.BlockRequest;
import com.example.booking.data.dto.BlockResponse;
import com.example.booking.data.entity.Block;
import com.example.booking.data.repository.BlockRepository;
import com.example.booking.data.repository.BookingRepository;
import com.example.booking.exception.BlockNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mfedechko
 */
@Service
public class ManagerBlockService extends BookingService {

    public ManagerBlockService(final BookingRepository bookingRepository,
                               final BlockRepository blockRepository) {
        super(bookingRepository, blockRepository);
    }

    @Transactional
    public BlockResponse create(final BlockRequest request) {

        final var startDate = request.getStartDate();
        final var endDate = request.getEndDate();
        validateDateRange(startDate, endDate);
        checkIfPropertyBooked(request.getPropertyId(), startDate, endDate);

        final var block = new Block();
        block.setPropertyId(request.getPropertyId());
        block.setStartDate(request.getStartDate());
        block.setEndDate(request.getEndDate());
        block.setReason(request.getReason());

        blockRepository.save(block);
        return EntityMapper.buildBlockResponse(block);
    }

    @Transactional
    public BlockResponse update(final Long id,
                                final BlockRequest request) {
        final var startDate = request.getStartDate();
        final var endDate = request.getEndDate();
        validateDateRange(startDate, endDate);

        final var block = blockRepository.findById(id)
                .orElseThrow(() -> new BlockNotFoundException(id));

        block.setStartDate(request.getStartDate());
        block.setEndDate(request.getEndDate());
        block.setReason(request.getReason());
        blockRepository.save(block);

        return EntityMapper.buildBlockResponse(block);
    }

    @Transactional
    public void delete(final Long id) {
        final var block = blockRepository.findById(id)
                .orElseThrow(() -> new BlockNotFoundException(id));
        blockRepository.delete(block);
    }
}
