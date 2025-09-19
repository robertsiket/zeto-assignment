package org.zeto.assignment.models.edf;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * DTO representing parsed values from EDF per-signal headers.
 */
@Getter
@Builder
@Accessors(fluent = true)
public class SignalHeader {
    private final int numSignals;
    private final List<String> labels;
    private final List<String> transducerTypes;
    private final List<Integer> samplesPerRecord;
    private final List<String> dataChannelNames;
    private final List<String> dataChannelTransducerTypes;
    private final int annotationChannelIndex;
}
