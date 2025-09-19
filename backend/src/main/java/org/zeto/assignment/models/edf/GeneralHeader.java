package org.zeto.assignment.models.edf;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO representing parsed values from the EDF general header (first 256 bytes).
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
