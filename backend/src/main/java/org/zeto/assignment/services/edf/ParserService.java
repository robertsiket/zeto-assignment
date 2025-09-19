package org.zeto.assignment.services.edf;

import java.io.File;

import org.zeto.assignment.models.edf.FileInfo;

/**
 * Service for parsing EDF/EDF+ files to extract structured metadata and details.
 * <p>
 * This service provides functionality for processing and validating EDF/EDF+ files,
 * extracting details from the general header and signal headers, and returning the
 * parsed information in the form of a {@link FileInfo} object. It uses associated
 * services as necessary for header parsing and logging errors or invalid states.
 */
public interface ParserService extends BaseService {
    FileInfo parse(File edfFile);
}
