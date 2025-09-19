package org.zeto.assignment.services.edf;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BaseServiceTest {

    private final BaseService baseService = new BaseService() {
    };

    @Test
    @DisplayName("readBytes reads exact number of bytes or throws on EOF")
    void readBytes_exactOrThrow() throws Exception {
        var data = new byte[]{1, 2, 3, 4, 5};
        var is = new ByteArrayInputStream(data);
        var out = baseService.readBytes(is, 5);
        assertArrayEquals(data, out);

        var shortIs = new ByteArrayInputStream(new byte[]{9, 9});
        var ex = assertThrows(IOException.class, () -> baseService.readBytes(shortIs, 3));
        assertTrue(ex.getMessage().contains("Unexpected end of file"));
    }

    @Test
    @DisplayName("formatDate converts DD.MM.YY and HH.MM.SS to YYYY-MM-DD HH:MM:SS")
    void formatDate_ok() {
        assertEquals("2019-09-19 12:30:00", baseService.formatDate("19.09.19", "12.30.00"));
        assertEquals("1985-01-02 03:04:05", baseService.formatDate("02.01.85", "03.04.05"));
    }

    @Test
    @DisplayName("formatDate falls back on invalid input")
    void formatDate_fallback() {
        assertEquals("bad invalid", baseService.formatDate("bad", "invalid"));
    }
}
