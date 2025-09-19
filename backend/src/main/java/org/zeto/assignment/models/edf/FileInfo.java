package org.zeto.assignment.models.edf;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A class to hold the structured information extracted from an EDF file.
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    private String fileName;
    private boolean valid;
    private String identifier;
    private LocalDateTime recordingDate;
    private String patientName;
    private int numberOfChannels;
    private List<String> channelNames;
    private List<String> channelTransducerTypes;
    private double recordingLengthSeconds;
    private int numberOfAnnotations;
}
