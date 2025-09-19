package org.zeto.assignment.models.edf;

import java.util.List;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Getter;

/**
 * DTO representing parsed values from EDF per-signal headers.
 */
@Getter
@Builder
public class SignalHeader {
    private final int numSignals;
    private final List<String> labels;
    private final List<String> transducerTypes;
    private final List<Integer> samplesPerRecord;
    private final List<String> dataChannelNames;
    private final List<String> dataChannelTransducerTypes;
    private final int annotationChannelIndex;

    public List<Channel> getChannels() {
        return IntStream.range(0, dataChannelNames.size())
                        .mapToObj(i -> new Channel(dataChannelNames.get(i), dataChannelTransducerTypes.get(i)))
                        .toList();
    }
}
