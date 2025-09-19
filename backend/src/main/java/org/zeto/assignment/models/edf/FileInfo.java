package org.zeto.assignment.models.edf;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * A class to hold the structured information extracted from an EDF file.
 */
@Getter
@Builder
@ToString
public class FileInfo {
    private final String fileName;
    private final boolean valid;
    private final String identifier;
    private final String recordingDate;
    private final String patientName;
    private final int numberOfChannels;
    private final List<String> channelNames;
    private final List<String> channelTransducerTypes;
    private final double recordingLengthSeconds;
    private final int numberOfAnnotations;
}
