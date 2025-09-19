package org.zeto.assignment.services.edf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.zeto.assignment.models.edf.GeneralHeader;
import org.zeto.assignment.models.edf.SignalHeader;

/**
 * Service implementation for processing and interpreting signal headers
 * and related data from an EDF (European Data Format) file. This class provides
 * methods to read and parse signal-specific header information, such as labels,
 * transducer types, and sample data, from an input stream while adhering to
 * EDF+ specifications.
 * <p>
 * The class is designed to handle multiple signals, compute data channel attributes,
 * and parse annotation channel values when available.
 * <p>
 * Responsibilities:
 * - Read signal header fields from the EDF file.
 * - Parse metadata for each signal, including labels, transducer types, and sample counts.
 * - Identify and process the EDF+ annotation channel for annotation tagging and counting.
 */
@Service
public class SignalHeaderServiceImpl implements SignalHeaderService {

    private static final String ANNOTATION_CHANNEL_LABEL = "EDF Annotations";
    private static final byte TAL_TERMINATOR_BYTE = 0x00;
    private static final int BYTES_PER_SAMPLE = 2;
    private static final int SH_DIGITAL_MAX_LENGTH = 8;
    private static final int SH_DIGITAL_MIN_LENGTH = 8;
    private static final int SH_LABEL_LENGTH = 16;
    private static final int SH_PHYSICAL_DIMENSION_LENGTH = 8;
    private static final int SH_PHYSICAL_MAX_LENGTH = 8;
    private static final int SH_PHYSICAL_MIN_LENGTH = 8;
    private static final int SH_PREFILTER_LENGTH = 80;
    private static final int SH_RESERVED_LENGTH = 32;
    private static final int SH_SAMPLES_PER_RECORD_LENGTH = 8;
    private static final int SH_TRANSDUCER_LENGTH = 80;

    /**
     * Reads and parses the signal header information from the given input stream and general header.
     * Extracts signal labels, transducer types, samples per record, and processes data channel
     * attributes as well as the EDF+ annotation channel, if present.
     *
     * @param is            the input stream containing the signal header data
     * @param generalHeader the general header containing metadata for the signal header
     * @return a SignalHeader object containing parsed signal header information
     * @throws IOException if an I/O error occurs while reading from the input stream
     */
    @Override
    public SignalHeader read(InputStream is, GeneralHeader generalHeader) throws IOException {
        var allSignalLabels = readSignalHeaderField(is, generalHeader.getNumSignals(), SH_LABEL_LENGTH);
        var allTransducerTypes = readSignalHeaderField(is, generalHeader.getNumSignals(), SH_TRANSDUCER_LENGTH);

        readSignalHeaderField(is, generalHeader.getNumSignals(), SH_PHYSICAL_DIMENSION_LENGTH);
        readSignalHeaderField(is, generalHeader.getNumSignals(), SH_PHYSICAL_MIN_LENGTH);
        readSignalHeaderField(is, generalHeader.getNumSignals(), SH_PHYSICAL_MAX_LENGTH);
        readSignalHeaderField(is, generalHeader.getNumSignals(), SH_DIGITAL_MIN_LENGTH);
        readSignalHeaderField(is, generalHeader.getNumSignals(), SH_DIGITAL_MAX_LENGTH);
        readSignalHeaderField(is, generalHeader.getNumSignals(), SH_PREFILTER_LENGTH);

        var samplesPerRecord = readSignalSamplesPerRecord(is, generalHeader.getNumSignals());

        readBytes(is, generalHeader.getNumSignals() * SH_RESERVED_LENGTH);

        var annotationChannelIndex = -1;
        var dataChannelNames = new ArrayList<String>();
        var dataChannelTransducerTypes = new ArrayList<String>();

        for (var i = 0; i < generalHeader.getNumSignals(); i++) {
            var label = allSignalLabels.get(i);

            if (ANNOTATION_CHANNEL_LABEL.equals(label)) {
                annotationChannelIndex = i;
            } else {
                dataChannelNames.add(label);
                dataChannelTransducerTypes.add(allTransducerTypes.get(i));
            }
        }

        return SignalHeader.builder()
                           .numSignals(generalHeader.getNumSignals())
                           .labels(allSignalLabels)
                           .transducerTypes(allTransducerTypes)
                           .samplesPerRecord(samplesPerRecord)
                           .dataChannelNames(dataChannelNames)
                           .dataChannelTransducerTypes(dataChannelTransducerTypes)
                           .annotationChannelIndex(countAnnotations(is, generalHeader.getNumDataRecords(), samplesPerRecord, annotationChannelIndex))
                           .build();
    }

    private static String readAscii(byte[] source, int offset, int length) {
        return new String(source, offset, length, StandardCharsets.US_ASCII).trim();
    }

    private List<String> readSignalHeaderField(InputStream is, int numSignals, int fieldLength) throws IOException {
        var fieldBytes = readBytes(is, numSignals * fieldLength);
        var values = new ArrayList<String>(numSignals);

        for (var i = 0; i < numSignals; i++) {
            values.add(readAscii(fieldBytes, i * fieldLength, fieldLength));
        }

        return values;
    }

    private List<Integer> readSignalSamplesPerRecord(InputStream is, int numSignals) throws IOException {
        var stringValues = readSignalHeaderField(is, numSignals, SH_SAMPLES_PER_RECORD_LENGTH);
        var intValues = new ArrayList<Integer>(numSignals);

        for (var s : stringValues) {
            intValues.add(Integer.parseInt(s.trim()));
        }

        return intValues;
    }

    /**
     * Count annotations across all data records by scanning the EDF+ annotation channel.
     * <p>
     * Approach:
     * - Compute the total number of samples per record and the offset of the annotation channel.
     * - For each record, skip preceding channel samples, read the annotation bytes, and count
     * NUL (0x00) terminators which separate TAL entries. Then skip remaining samples.
     * - This is a lightweight heuristic suitable for a simple count; it does not fully parse TALs.
     */
    private int countAnnotations(InputStream is, int numDataRecords, List<Integer> samplesPerRecord, int annotationChannelIndex) throws IOException {
        if (annotationChannelIndex == -1) {
            return 0;
        }

        var annotationCount = 0;
        var totalSamplesInRecord = samplesPerRecord.stream().mapToInt(Integer::intValue).sum();
        var samplesBeforeAnnotation = 0;

        for (var i = 0; i < annotationChannelIndex; i++) {
            samplesBeforeAnnotation += samplesPerRecord.get(i);
        }

        for (var record = 0; record < numDataRecords; record++) {
            readBytes(is, samplesBeforeAnnotation * BYTES_PER_SAMPLE);

            var annotationSamples = samplesPerRecord.get(annotationChannelIndex);
            var annotationBytes = readBytes(is, annotationSamples * BYTES_PER_SAMPLE);

            for (var annotation : annotationBytes) {
                if (annotation == TAL_TERMINATOR_BYTE) annotationCount++;
            }

            var samplesAfterAnnotation = totalSamplesInRecord - samplesBeforeAnnotation - annotationSamples;

            readBytes(is, samplesAfterAnnotation * BYTES_PER_SAMPLE);
        }

        return annotationCount;
    }
}
