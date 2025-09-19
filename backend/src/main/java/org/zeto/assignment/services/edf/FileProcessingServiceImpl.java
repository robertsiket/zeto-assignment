package org.zeto.assignment.services.edf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zeto.assignment.models.edf.FileInfo;

/**
 * Service implementation for processing EDF (European Data Format) files found
 * on the classpath at application startup. This service scans a specified directory,
 * identifies EDF files, parses them using a {@link ParserService}, and maintains
 * a list of {@link FileInfo} objects for all successfully processed files.
 */
@Service
@Slf4j
public class FileProcessingServiceImpl implements FileProcessingService {
    private static final String LOG_INFO_SCANNING_EDF_DIRECTORY = "Scanning EDF directory: {}";
    private static final String LOG_INFO_EDF_FILE_PROCESSED = "Successfully processed EDF file: {}";
    private static final String EDF_FILE_EXTENSION = ".edf";
    private static final String EDF_FILES_DIRECTORY = "edf";
    private final List<FileInfo> processedFiles = new CopyOnWriteArrayList<>();
    private final ParserService parserService;

    public FileProcessingServiceImpl(ParserService parserService) {
        this.parserService = parserService;
    }

    /**
     * Scans the configured EDF directory path for EDF files and processes them.
     * This method is automatically invoked after dependency injection is complete due to
     * the {@code @PostConstruct} annotation. It filters for regular files ending with ".edf"
     * (case-insensitive) and parses each one, storing the resulting {@link FileInfo}.
     *
     * @throws IOException If an I/O error occurs during directory traversal or file access.
     */
    @PostConstruct
    public void scanAndProcessFilesOnStartup() throws IOException {
        log.info(LOG_INFO_SCANNING_EDF_DIRECTORY, getClassPathDirectory());

        try (var paths = Files.walk(Paths.get(getClassPathDirectory()))) {
            paths.filter(Files::isRegularFile)
                 .filter(path -> path.toString().toLowerCase().endsWith(EDF_FILE_EXTENSION))
                 .forEach(path -> {
                     processedFiles.add(parserService.parse(path.toFile()));
                     log.info(LOG_INFO_EDF_FILE_PROCESSED, path.getFileName());
                 });
        }

        processedFiles.sort(Comparator.comparing(FileInfo::getRecordingDate, Comparator.nullsLast(Comparator.naturalOrder())));
    }

    @SneakyThrows
    private String getClassPathDirectory() {
        return Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(EDF_FILES_DIRECTORY)).toURI()).toString();
    }


    /**
     * Retrieves an unmodifiable list of {@link FileInfo} objects for all
     * EDF files that have been scanned and processed by this service.
     *
     * @return An unmodifiable {@link List} of {@link FileInfo} objects,
     * each representing a parsed EDF file.
     */
    @Override
    public List<FileInfo> getProcessedFiles() {
        return Collections.unmodifiableList(processedFiles);
    }
}