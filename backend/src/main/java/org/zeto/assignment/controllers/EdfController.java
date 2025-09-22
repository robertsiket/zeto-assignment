package org.zeto.assignment.controllers;

import java.util.Comparator;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zeto.assignment.models.edf.FileInfo;
import org.zeto.assignment.services.edf.FileProcessingService;

@Tag(name = "EDF Files", description = "Operations for EDF file metadata")
@RestController
@RequestMapping("/api/edf-files")
@CrossOrigin(origins = "http://localhost:4200")
public class EdfController {

    private final FileProcessingService edfProcessingService;

    public EdfController(FileProcessingService edfProcessingService) {
        this.edfProcessingService = edfProcessingService;
    }

    /**
     * Retrieves a list of metadata for processed EDF files, sorted by their recording date.
     * <p>
     * The method fetches all processed EDF file metadata, ensuring the results
     * are ordered by the recording date. Files with a null recording date
     * will be placed at the end of the sorted list.
     *
     * @return A list of {@link FileInfo} objects containing metadata for each processed EDF file.
     */
    @Operation(
            summary = "List EDF files",
            description = "Returns a list of processed EDF file metadata sorted by recording date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List retrieved",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FileInfo.class))))
    })
    @GetMapping
    public List<FileInfo> getAllEdfFiles() {
        return edfProcessingService.getProcessedFiles()
                                   .stream()
                                   .sorted(Comparator.comparing(FileInfo::getRecordingDate, Comparator.nullsLast(Comparator.naturalOrder())))
                                   .toList();
    }
}