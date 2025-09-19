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
    private List<Channel> channels;
    private LocalDateTime recordingDate;
    private String fileName;
    private String identifier;
    private String patientName;
    private boolean valid;
    private double recordingLengthSeconds;
    private int numberOfAnnotations;
}
