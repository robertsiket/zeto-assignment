package org.zeto.assignment.services.edf;

import java.io.IOException;
import java.io.InputStream;

import org.zeto.assignment.models.edf.GeneralHeader;

/**
 * Service responsible for reading and parsing the EDF general header (first 256 bytes).
 */
public interface GeneralHeaderService extends BaseService {
    /**
     * Reads and parses the general header (first 256 bytes) of an EDF file from the given input stream.
     * This header contains crucial metadata about the EDF file, such as the number of data records,
     * number of signals, record duration, patient information, and recording date.
     *
     * @param is The {@link InputStream} from which to read the EDF general header. The stream is expected
     *           to be positioned at the beginning of the general header.
     * @return A {@link GeneralHeader} object containing the parsed information from the EDF general header.
     * @throws IOException If an I/O error occurs while reading from the input stream, or if the
     *                     stream ends unexpectedly before all header bytes can be read.
     */
    GeneralHeader read(InputStream is) throws IOException;
}
