package org.zeto.assignment.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zeto.assignment.models.edf.FileInfo;
import org.zeto.assignment.services.edf.FileProcessingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "EDF Files", description = "Operations for EDF file metadata")
@RestController
@RequestMapping("/api/edf-files")
@CrossOrigin(origins = "http://localhost:4200")
public class EdfController {

    private final FileProcessingService edfProcessingService;

    public EdfController(FileProcessingService edfProcessingService) {
        this.edfProcessingService = edfProcessingService;
    }

    @Operation(
            summary = "List EDF files",
            description = "Returns a list of processed EDF file metadata sorted by recording date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FileInfo.class))))
    })
    @GetMapping
    public List<FileInfo> getAllEdfFiles() {
        return edfProcessingService.getProcessedFiles()
                                   .stream()
                                   .sorted(Comparator.comparing(FileInfo::getRecordingDate, Comparator.nullsLast(Comparator.naturalOrder())))
                                   .collect(Collectors.toList());
    }
}