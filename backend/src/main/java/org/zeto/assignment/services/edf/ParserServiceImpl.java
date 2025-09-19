package org.zeto.assignment.services.edf;

import java.io.File;
import java.io.FileInputStream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zeto.assignment.models.edf.FileInfo;

/**
 * Spring-managed service that parses EDF/EDF+ files in pure Java.
 * <p>
 * Responsibilities:
 * - Read the fixed-size general header (256 bytes) and signal headers according to the EDF spec.
 * - Identify data channels vs. the EDF+ annotation channel (label "EDF Annotations").
 * - Compute recording metadata (start date/time, duration, channel names/types).
 * - Optionally count annotations by scanning the annotation channel across data records.
 * <p>
 * Notes and assumptions:
 * - Only basic identifier validation is performed (identifier[0] == '0').
 * - For annotation counting we use a lightweight heuristic by counting NUL terminators in the
 * annotation bytes; a full TAL parser is out of scope for this service.
 */
@Service
@Slf4j
public class ParserServiceImpl implements ParserService {

    private static final char INITIAL_IDENTIFIER_CHAR = '0';
    private final GeneralHeaderService generalHeaderService;
    private final SignalHeaderService signalHeaderService;

    public ParserServiceImpl(GeneralHeaderService generalHeaderService, SignalHeaderService signalHeaderService) {
        this.generalHeaderService = generalHeaderService;
        this.signalHeaderService = signalHeaderService;
    }

    /**
     * Parses the provided EDF/EDF+ file to extract structured metadata.
     * <p>
     * This method processes the general header and signal headers of the EDF file,
     * validates its identifier, and constructs a {@link FileInfo} object containing
     * details such as file name, recording details, channel information, and annotations.
     * In case of errors, it returns a {@link FileInfo} object with invalid state.
     *
     * @param file The EDF/EDF+ file to parse.
     * @return A {@link FileInfo} object representing the parsed structure of the EDF file.
     */
    @Override
    public FileInfo parse(File file) {
        try (var fis = new FileInputStream(file)) {
            var generalHeader = generalHeaderService.read(fis);

            if (isNotCorrectIdentifier(generalHeader.getIdentifier())) {
                return createInvalidEdfFileInfo(file);
            }

            var signalHeader = signalHeaderService.read(fis, generalHeader);

            return FileInfo.builder()
                           .fileName(file.getName())
                           .valid(true)
                           .identifier(generalHeader.getIdentifier())
                           .recordingDate(generalHeader.getRecordingDate())
                           .patientName(generalHeader.getPatientName())
                           .channels(signalHeader.getChannels())
                           .recordingLengthSeconds(generalHeader.getRecordingLengthSeconds())
                           .numberOfAnnotations(signalHeader.getAnnotationChannelIndex())
                           .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return createInvalidEdfFileInfo(file);
        }
    }

    private static boolean isNotCorrectIdentifier(String identifier) {
        return identifier.isEmpty() || identifier.charAt(0) != INITIAL_IDENTIFIER_CHAR;
    }

    private static FileInfo createInvalidEdfFileInfo(File edfFile) {
        return FileInfo.builder()
                       .fileName(edfFile.getName())
                       .valid(false)
                       .build();
    }
}
