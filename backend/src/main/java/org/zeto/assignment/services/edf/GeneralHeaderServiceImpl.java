package org.zeto.assignment.services.edf;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.zeto.assignment.models.edf.GeneralHeader;

/**
 * Implementation of the {@link GeneralHeaderService} interface responsible for reading
 * and parsing the general header (first 256 bytes) of an EDF file. This service extracts
 * key metadata required to describe the EDF file.
 * <p>
 * The general header includes details such as:
 * - Identifier version of the EDF file format.
 * - Patient information and name.
 * - Recording date and time.
 * - Number of data records in the file.
 * - Duration of each record in seconds.
 * - Number of signals in the data.
 * <p>
 * This class relies on specific fixed offsets and lengths to read relevant data
 * from the EDF header. Data is read as ASCII characters from byte arrays and
 * converted to their respective types (e.g., integer, double, or string).
 */
@Service
public class GeneralHeaderServiceImpl implements GeneralHeaderService {

    private static final int GH_NUM_DATA_RECORDS_LEN = 8;
    private static final int GH_NUM_DATA_RECORDS_OFFSET = 236;
    private static final int GH_NUM_SIGNALS_LEN = 4;
    private static final int GH_NUM_SIGNALS_OFFSET = 252;
    private static final int GH_PATIENT_INFO_LEN = 80;
    private static final int GH_PATIENT_INFO_OFFSET = 8;
    private static final int GH_RECORD_DURATION_LEN = 8;
    private static final int GH_RECORD_DURATION_OFFSET = 244;
    private static final int GH_START_DATE_LEN = 8;
    private static final int GH_START_DATE_OFFSET = 168;
    private static final int GH_START_TIME_LEN = 8;
    private static final int GH_START_TIME_OFFSET = 176;
    private static final int GH_VERSION_LEN = 8;
    private static final int GH_VERSION_OFFSET = 0;
    private static final int HEADER_GENERAL_BYTES = 256;

    /**
     * Reads and parses the general header information from an InputStream representing an EDF file.
     * Extracts metadata such as identifier, patient name, recording date, number of data records,
     * record duration, and number of signals from the first 256 bytes of the file.
     *
     * @param is The InputStream to read the EDF general header data from.
     * @return A GeneralHeader object containing the parsed metadata from the EDF file.
     * @throws IOException If an I/O error occurs or the InputStream does not contain sufficient data.
     */
    @Override
    public GeneralHeader read(InputStream is) throws IOException {
        var generalHeader = readBytes(is, HEADER_GENERAL_BYTES);

        return GeneralHeader.builder()
                            .identifier(readAscii(generalHeader, GH_VERSION_OFFSET, GH_VERSION_LEN))
                            .patientName(readAscii(generalHeader, GH_PATIENT_INFO_OFFSET, GH_PATIENT_INFO_LEN))
                            .recordingDate(readRecordingDate(generalHeader))
                            .numDataRecords(parseInt(readAscii(generalHeader, GH_NUM_DATA_RECORDS_OFFSET, GH_NUM_DATA_RECORDS_LEN)))
                            .recordDurationSec(parseDouble(readAscii(generalHeader, GH_RECORD_DURATION_OFFSET, GH_RECORD_DURATION_LEN)))
                            .numSignals(parseInt(readAscii(generalHeader, GH_NUM_SIGNALS_OFFSET, GH_NUM_SIGNALS_LEN)))
                            .build();
    }

    private String readRecordingDate(byte[] generalHeader) {
        return formatDate(readAscii(generalHeader, GH_START_DATE_OFFSET, GH_START_DATE_LEN),
                          readAscii(generalHeader, GH_START_TIME_OFFSET, GH_START_TIME_LEN));
    }

    private static String readAscii(byte[] source, int offset, int length) {
        return new String(source, offset, length, StandardCharsets.US_ASCII).trim();
    }
}
