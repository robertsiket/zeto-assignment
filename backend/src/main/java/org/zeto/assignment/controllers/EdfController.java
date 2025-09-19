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

@RestController
@RequestMapping("/api/edf-files")
@CrossOrigin(origins = "http://localhost:4200")
public class EdfController {

    private final FileProcessingService edfProcessingService;

    public EdfController(FileProcessingService edfProcessingService) {
        this.edfProcessingService = edfProcessingService;
    }

    @GetMapping
    public List<FileInfo> getAllEdfFiles() {
        return edfProcessingService.getProcessedFiles()
                                   .stream()
                                   .sorted(Comparator.comparing(FileInfo::getRecordingDate, Comparator.nullsLast(Comparator.naturalOrder())))
                                   .collect(Collectors.toList());
    }
}