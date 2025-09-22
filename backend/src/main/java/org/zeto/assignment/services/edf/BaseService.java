package org.zeto.assignment.services.edf;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

/**
 * Provides common utility methods for services involved in processing EDF files.
 * This interface defines default methods that can be reused by various EDF-related services
 * for tasks such as reading specific amounts of data from an input stream and formatting date/time strings.
 */
public interface BaseService {
    String UNEXPECTED_END_OF_FILE_MESSAGE = "Unexpected end of file. Expected %d bytes, but got %d";

    /**
     * A {@link DateTimeFormatter} specifically configured for parsing and formatting date
     * and time strings in the European Data Format (EDF) context.
     * <p>
     * The formatter uses the pattern "dd.MM. YY HH.mm.ss", where:
     * - "dd.MM." captures the day and month in two-digit form.
     * - "YY" corresponds to a reduced-value year with a 100-year window starting from 1985.
     * Years in the range 85-99 map to 1985-1999, while years 00-84 map to 2000-2084.
     * - "HH.mm.ss" represents the time in hours, minutes, and seconds.
     * <p>
     * This formatter can be used to construct date/time objects, such as {@link LocalDateTime},
     * when processing EDF files to standardize and interpret recording timestamps.
     * It is particularly suited for EDF file general headers that include recording start times.
     */
    DateTimeFormatter EDF_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("dd.MM.")
            .appendValueReduced(ChronoField.YEAR_OF_ERA, 2, 2, LocalDate.of(1985, 1, 1))
            .appendPattern(" HH.mm.ss")
            .toFormatter();

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
        var buffer = is.readNBytes(numBytes);
        if (buffer.length != numBytes) {
            throw new IOException(UNEXPECTED_END_OF_FILE_MESSAGE.formatted(numBytes, buffer.length));
        }
        return buffer;
    }

    /**
     * Parses date and time strings into a {@link LocalDateTime} object.
     * It parses the date from "DD.MM.YY" format and time from "HH.MM.SS" format.
     * For the year, it uses a 100-year window starting from 1985, so years 85-99 are interpreted as 1985-1999,
     * and years 00-84 are interpreted as 2000-2084.
     *
     * @param date The date string, in "DD.MM.YY" format (e.g., "19.09.19").
     * @param time The time string, in "HH.MM.SS" format (e.g., "12.30.00").
     * @return A {@link LocalDateTime} object representing the parsed date and time.
     * @throws IllegalArgumentException if the date and time strings are in an invalid format.
     */
    default LocalDateTime formatDate(String date, String time) {
        try {
            return LocalDateTime.parse(date + " " + time, EDF_DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + date + " " + time, e);
        }
    }
}
