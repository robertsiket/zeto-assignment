package org.zeto.assignment.services.edf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GeneralHeaderServiceImpl tests")
class GeneralHeaderServiceImplTest {

    private final GeneralHeaderServiceImpl generalHeaderService = new GeneralHeaderServiceImpl();

    @Test
    @DisplayName("read parses all general header fields from 256 bytes")
    void read_parsesFields() throws Exception {
        var header = new byte[256];
        Arrays.fill(header, (byte) ' ');

        putAscii(header, 0, 8, "0VER");
        putAscii(header, 8, 80, "John Doe");
        putAscii(header, 168, 8, "19.09.25");
        putAscii(header, 176, 8, "12.30.00");
        putAscii(header, 236, 8, "2");
        putAscii(header, 244, 8, "10.0");
        putAscii(header, 252, 4, "3");

        var generalHeader = generalHeaderService.read(new ByteArrayInputStream(header));

        assertEquals("0VER", generalHeader.getIdentifier());
        assertEquals("John Doe", generalHeader.getPatientName());
        assertEquals(LocalDateTime.parse("2025-09-19T12:30:00"), generalHeader.getRecordingDate());
        assertEquals(2, generalHeader.getNumDataRecords());
        assertEquals(10.0, generalHeader.getRecordDurationSec());
        assertEquals(3, generalHeader.getNumSignals());
    }

    @Test
    @DisplayName("read throws when fewer than 256 bytes available")
    void read_throwsOnShortStream() {
        var header = new byte[200];
        var ex = assertThrows(IOException.class, () -> generalHeaderService.read(new ByteArrayInputStream(header)));
        assertTrue(ex.getMessage().contains("Unexpected end of file"));
    }

    private static void putAscii(byte[] arr, int offset, int len, String value) {
        var bytes = value.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(bytes, 0, arr, offset, Math.min(bytes.length, len));
    }
}
