package org.zeto.assignment.models.edf;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

/**
 * Represents the general header section of an EDF (European Data Format) file.
 * The general header contains metadata about the recording and its structure.
 * <p>
 * Fields in this class include:
 * - The number of data records in the file.
 * - The number of signals present in the recording.
 * - The duration of a single data record in seconds.
 * - A unique identifier associated with the file.
 * - The name of the patient associated with the recording.
 * - The date and time when the recording was made.
 * <p>
 * Provides a utility method to calculate the total recording length in seconds.
 */
@Getter
@Builder
public class GeneralHeader {
    private final int numDataRecords;
    private final int numSignals;
    private final double recordDurationSec;
    private final String identifier;
    private final String patientName;
    private final LocalDateTime recordingDate;

    public double getRecordingLengthSeconds() {
        return numDataRecords * recordDurationSec;
    }
}
