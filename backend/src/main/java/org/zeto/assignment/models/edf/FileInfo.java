package org.zeto.assignment.models.edf;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents metadata about an EDF file, including its associated channels, recording details, and validation status.
 * <p>
 * The class provides information about:
 * - Channels involved in the recording, represented as a list of {@code Channel} objects.
 * - Date and time when the recording was made.
 * - File name and a unique identifier for the EDF file.
 * - Patient name associated with the recording.
 * - Validation status of the file.
 * - Total length of the recording, in seconds.
 * - Number of annotations present in the file.
 * <p>
 * This class follows a builder pattern for instantiation, and it includes necessary annotations for
 * generating boilerplate code like getters and {@code toString} method automatically.
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
