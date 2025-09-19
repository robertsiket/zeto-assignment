package org.zeto.assignment.services.edf;

import java.util.List;

import org.zeto.assignment.models.edf.FileInfo;

/**
 * Service interface for processing EDF files located on the application's classpath.
 * Implementations of this interface are responsible for locating, parsing, and
 * providing structured information about EDF files found in the classpath resources.
 */
public interface FileProcessingService {
    /**
     * Retrieves a list of processed {@link FileInfo} objects for all EDF files
     * found on the classpath. Each {@link FileInfo} object contains parsed metadata
     * and validation status for a single EDF file.
     *
     * @return A {@link List} of {@link FileInfo} objects, representing the
     * structured information of processed EDF files.
     */
    List<FileInfo> getProcessedFiles();
}
