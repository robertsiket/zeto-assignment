package org.zeto.assignment.services.edf;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.zeto.assignment.models.edf.GeneralHeader;
import org.zeto.assignment.models.edf.SignalHeader;

class SignalHeaderServiceImplTest {

    private final SignalHeaderServiceImpl service = new SignalHeaderServiceImpl();

    @Test
    @DisplayName("read parses signal metadata and counts annotations across data records")
    void read_parsesAndCountsAnnotations() throws Exception {
        var numSignals = 3;
        var numDataRecords = 2;

        // Build input bytes: signal header blocks then data records
        var bout = new ByteArrayOutputStream();

        // labels (16 bytes each)
        writePaddedAscii(bout, "Fp1", 16);
        writePaddedAscii(bout, "Fp2", 16);
        writePaddedAscii(bout, "EDF Annotations", 16);

        // transducer types (80 bytes each)
        writePaddedAscii(bout, "Type1", 80);
        writePaddedAscii(bout, "Type2", 80);
        writePaddedAscii(bout, "AnnType", 80);

        // physical dim, min, max, digital min, max, prefilter (ignored values)
        repeatWriteSpaces(bout, numSignals * 8);  // phys dim
        repeatWriteSpaces(bout, numSignals * 8);  // phys min
        repeatWriteSpaces(bout, numSignals * 8);  // phys max
        repeatWriteSpaces(bout, numSignals * 8);  // digital min
        repeatWriteSpaces(bout, numSignals * 8);  // digital max
        repeatWriteSpaces(bout, numSignals * 80); // prefilter

        // samples per record (8 bytes ascii each) -> [2,3,4]
        writePaddedAscii(bout, "2", 8);
        writePaddedAscii(bout, "3", 8);
        writePaddedAscii(bout, "4", 8);

        // reserved (32 bytes each)
        repeatWriteSpaces(bout, numSignals * 32);

        // Data records
        // total samples per record = 2 + 3 + 4 = 9; bytes per sample = 2
        // samples before annotation (channel index 2) = 2 + 3 = 5 -> 10 bytes to skip
        // annotation samples = 4 -> 8 bytes to read and count 0x00
        // We'll make record1 have 2 zeros, record2 have 1 zero
        var before = new byte[10];
        Arrays.fill(before, (byte) 7);
        var ann1 = new byte[] {0x00, 1, 2, 0x00, 3, 4, 5, 6}; // 2 zeros
        var ann2 = new byte[] {9, 8, 7, 6, 5, 4, 3, 0x00};     // 1 zero

        // record 1
        bout.write(before);
        bout.write(ann1);
        // samples after annotation = 0 in this layout
        // record 2
        bout.write(before);
        bout.write(ann2);

        var is = new ByteArrayInputStream(bout.toByteArray());

        var g = GeneralHeader.builder()
                              .numSignals(numSignals)
                              .numDataRecords(numDataRecords)
                              .build();

        SignalHeader sh = service.read(is, g);
        assertEquals(3, sh.getNumSignals());
        assertEquals(List.of("Fp1", "Fp2", "EDF Annotations"), sh.getLabels());
        assertEquals(List.of("Type1", "Type2", "AnnType"), sh.getTransducerTypes());
        assertEquals(List.of(2, 3, 4), sh.getSamplesPerRecord());
        assertEquals(List.of("Fp1", "Fp2"), sh.getDataChannelNames());
        assertEquals(List.of("Type1", "Type2"), sh.getDataChannelTransducerTypes());
        // The implementation stores the annotation count in the annotationChannelIndex field
        assertEquals(3, sh.getAnnotationChannelIndex());
    }

    private static void writePaddedAscii(ByteArrayOutputStream bout, String s, int len) {
        var buf = new byte[len];
        Arrays.fill(buf, (byte) ' ');
        var sb = s.getBytes(StandardCharsets.US_ASCII);
        System.arraycopy(sb, 0, buf, 0, Math.min(sb.length, len));
        bout.writeBytes(buf);
    }

    private static void repeatWriteSpaces(ByteArrayOutputStream bout, int count) {
        var buf = new byte[count];
        Arrays.fill(buf, (byte) ' ');
        bout.writeBytes(buf);
    }
}
