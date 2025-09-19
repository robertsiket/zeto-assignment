package org.zeto.assignment.services.edf;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zeto.assignment.models.edf.Channel;
import org.zeto.assignment.models.edf.GeneralHeader;
import org.zeto.assignment.models.edf.SignalHeader;

@DisplayName("ParserServiceImpl tests")
@ExtendWith(MockitoExtension.class)
class ParserServiceImplTest {

    @Mock
    GeneralHeaderService generalHeaderService;

    @Mock
    SignalHeaderService signalHeaderService;

    @InjectMocks
    ParserServiceImpl parser;

    @Test
    @DisplayName("parse builds FileInfo for valid EDF using dependent services")
    void parse_valid() throws Exception {
        var generalHeader = GeneralHeader.builder()
                                         .identifier("0IDENT")
                                         .patientName("Alice")
                                         .recordingDate(LocalDateTime.parse("2025-09-01T10:00:00"))
                                         .numDataRecords(2)
                                         .recordDurationSec(5.0)
                                         .numSignals(2)
                                         .build();

        when(generalHeaderService.read(any(InputStream.class))).thenReturn(generalHeader);
        when(signalHeaderService.read(any(InputStream.class), eq(generalHeader))).thenReturn(SignalHeader.builder()
                                                                                                         .numSignals(2)
                                                                                                         .dataChannelNames(of("Fp1", "Fp2"))
                                                                                                         .dataChannelTransducerTypes(of("T1", "T2"))
                                                                                                         .annotationChannelIndex(3)
                                                                                                         .build());

        var edtFile = createTempEdtFile();
        var actualFileInfo = parser.parse(edtFile);

        assertTrue(actualFileInfo.isValid());
        assertEquals(edtFile.getName(), actualFileInfo.getFileName());
        assertEquals("0IDENT", actualFileInfo.getIdentifier());
        assertEquals("Alice", actualFileInfo.getPatientName());
        assertEquals(LocalDateTime.parse("2025-09-01T10:00:00"), actualFileInfo.getRecordingDate());
        assertEquals(2, actualFileInfo.getChannels().size());
        assertEquals(of(new Channel("Fp1", "T1"), new Channel("Fp2", "T2")), actualFileInfo.getChannels());
        assertEquals(10.0, actualFileInfo.getRecordingLengthSeconds());
        assertEquals(3, actualFileInfo.getNumberOfAnnotations());
    }

    @Test
    @DisplayName("parse returns invalid when identifier does not start with '0'")
    void parse_invalidIdentifier() throws Exception {

        when(generalHeaderService.read(any(InputStream.class))).thenReturn(GeneralHeader.builder()
                                                                                        .identifier("1BAD")
                                                                                        .patientName("Bob")
                                                                                        .recordingDate(LocalDateTime.parse("2025-01-01T00:00:00"))
                                                                                        .numDataRecords(1)
                                                                                        .recordDurationSec(1.0)
                                                                                        .numSignals(1)
                                                                                        .build());

        var edtFile = createTempEdtFile();
        var actualFileInfo = parser.parse(edtFile);

        assertFalse(actualFileInfo.isValid());
        assertEquals(edtFile.getName(), actualFileInfo.getFileName());
    }

    @Test
    @DisplayName("parse returns invalid when an exception occurs")
    void parse_exceptionHandling() throws Exception {
        when(generalHeaderService.read(any(InputStream.class))).thenThrow(new RuntimeException("boom"));

        var edtFile = createTempEdtFile();
        var actualFileInfo = parser.parse(edtFile);

        assertFalse(actualFileInfo.isValid());
        assertEquals(edtFile.getName(), actualFileInfo.getFileName());
    }

    private static File createTempEdtFile() throws IOException {
        var tmp = File.createTempFile("edf", ".edf");
        tmp.deleteOnExit();
        return tmp;
    }
}
