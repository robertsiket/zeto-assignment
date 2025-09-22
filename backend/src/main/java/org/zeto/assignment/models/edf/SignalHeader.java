package org.zeto.assignment.models.edf;

import java.util.List;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Getter;

/**
 * Represents the header section for signals in an EDF (European Data Format) file.
 * This header contains metadata related to the signals recorded in the file,
 * providing information necessary for interpreting the signal data.
 * <p>
 * Main functionalities include:
 * - Storing the number of signals in the file.
 * - Maintaining lists of signal attributes such as labels, transducer types, and samples per record.
 * - Defining data channel-specific information, including names and transducer types.
 * - Keeping track of the index of the annotation channel, if present.
 * <p>
 * It also provides a utility method to create a list of {@code Channel} objects,
 * representing the data channels with their associated names and transducer types.
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
