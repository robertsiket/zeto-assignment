package org.zeto.assignment.services.edf;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides common utility methods for services involved in processing EDF files.
 * This interface defines default methods that can be reused by various EDF-related services
 * for tasks such as reading specific amounts of data from an input stream and formatting date/time strings.
 */
public interface BaseService {
    String UNEXPECTED_END_OF_FILE_MESSAGE = "Unexpected end of file. Expected %d bytes, but got %d";

    /**
     * Reads a specified number of bytes from an {@link InputStream}, ensuring that the exact amount is read.
     * This method continues reading until {@code numBytes} have been accumulated or the end of the stream is reached.
     * If the end of the stream is reached before {@code numBytes} can be read, an {@link IOException} is thrown.
     *
     * @param is       The {@link InputStream} to read bytes from.
     * @param numBytes The exact number of bytes to read.
     * @return A byte array containing the {@code numBytes} read from the stream.
     * @throws IOException If an I/O error occurs or the stream ends unexpectedly before all {@code numBytes} are read.
     */
    default byte[] readBytes(InputStream is, int numBytes) throws IOException {
        var buffer = new byte[numBytes];
        var offset = 0;
        while (offset < numBytes) {
            var n = is.read(buffer, offset, numBytes - offset);
            if (n == -1) break;
            offset += n;
        }
        if (offset != numBytes) {
            throw new IOException(UNEXPECTED_END_OF_FILE_MESSAGE.formatted(numBytes, offset));
        }
        return buffer;
    }

    /**
     * Formats a date and time string pair into a standard "YYYY-MM-DD HH:MM:SS" format.
     * It attempts to parse the date from "DD.MM.YY" format and time from "HH.MM.SS" format.
     * For the year, it applies a heuristic: if the two-digit year is greater than 84, it's prefixed with "19";
     * otherwise, it's prefixed with "20".
     * If any parsing error occurs, the original date and time strings are concatenated and returned.
     *
     * @param date The date string, expected in "DD.MM.YY" format (e.g., "19.09.25").
     * @param time The time string, expected in "HH.MM.SS" format (e.g., "12.30.00").
     * @return A formatted date-time string in "YYYY-MM-DD HH:MM:SS" format,
     * or the concatenated original date and time strings if formatting fails.
     */
    default LocalDateTime formatDate(String date, String time) {
        try {
            var dateParts = date.split("\\.");
            var year = Integer.parseInt(dateParts[2]) > 84 ? "19" + dateParts[2] : "20" + dateParts[2];
            var formattedDateTime = String.format("%s-%s-%s %s", year, dateParts[1], dateParts[0], time.replace('.', ':'));
            return LocalDateTime.parse(formattedDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + date + " " + time, e);
        }
    }
}
