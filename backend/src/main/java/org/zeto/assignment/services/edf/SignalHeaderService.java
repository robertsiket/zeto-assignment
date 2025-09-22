package org.zeto.assignment.services.edf;

import java.io.IOException;
import java.io.InputStream;

import org.zeto.assignment.models.edf.GeneralHeader;
import org.zeto.assignment.models.edf.SignalHeader;

/**
 * A service interface for handling signal header data in EDF (European Data Format) files.
 * This service enables reading and parsing signal-specific metadata from the EDF file,
 * which is essential for correctly interpreting the signal data stored in the file.
 * <p>
 * The signal header contains attributes such as:
 * - Signal labels
 * - Transducer types
 * - Physical and digital conversion factors
 * - Number of samples per data record
 * - Annotation channel indices
 * <p>
 * Extends {@link BaseService} to leverage common utility methods for EDF file processing.
 */
public interface SignalHeaderService extends BaseService {
    /**
     * Reads and parses the EDF signal header data from the given input stream.
     *
     * @param is            The input stream containing the EDF file data, positioned after the general header.
     * @param generalHeader The parsed general header data that provides context, such as the number of signals.
     * @return An instance of {@link SignalHeader} containing the parsed signal-specific header data.
     * @throws IOException If an I/O error occurs while reading from the input stream.
     */
    SignalHeader read(InputStream is, GeneralHeader generalHeader) throws IOException;
}
