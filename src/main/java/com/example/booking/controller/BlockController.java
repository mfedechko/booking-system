package com.example.booking.controller;

import com.example.booking.data.dto.BlockRequest;
import com.example.booking.data.dto.BlockResponse;
import com.example.booking.service.BlockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mfedechko
 */
@RestController
@RequestMapping("/api/blocks")
@Tag(name = "Blocks", description = "Operations with blocks")
public class BlockController {

    private final BlockService blockService;

    public BlockController(final BlockService blockService) {
        this.blockService = blockService;
    }

    @Operation(summary = "Create a block")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlockResponse create(@Valid @RequestBody final BlockRequest request) {
        return blockService.create(request);
    }

    @Operation(summary = "Update a block")
    @PutMapping("/{id}")
    public BlockResponse update(@PathVariable final Long id,
                                @Valid @RequestBody final BlockRequest request) {
        return blockService.update(id, request);
    }

    @Operation(summary = "Delete a block")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final Long id) {
        blockService.delete(id);
    }
}
