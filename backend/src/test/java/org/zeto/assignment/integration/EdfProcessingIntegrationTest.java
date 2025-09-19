package org.zeto.assignment.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.zeto.assignment.models.edf.FileInfo;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration test: processed EDF files order and status")
class EdfProcessingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/edf-files returns files sorted by recordingDate with invalid last and statuses correct")
    void processedFiles_orderAndStatus() throws Exception {
        var mvcResult = mockMvc.perform(get("/api/edf-files").accept(MediaType.APPLICATION_JSON))
                               .andExpect(status().isOk())
                               .andReturn();

        var content = mvcResult.getResponse().getContentAsString();

        List<FileInfo> fileInfoList = getFileInfoList(content);

        assertEquals(5, fileInfoList.size(), "Expected 5 EDF fileInfoList to be processed from classpath resources");

        assertLastInvalidFile(fileInfoList);
        assertValidFiles(fileInfoList);
        assertSorting(fileInfoList);
    }

    private static void assertSorting(List<FileInfo> fileInfoList) {
        List<FileInfo> sorted = new ArrayList<>(fileInfoList);
        sorted.sort(Comparator.comparing(FileInfo::getRecordingDate, Comparator.nullsLast(Comparator.naturalOrder())));
        assertEquals(sorted, fileInfoList, "Files should be sorted by recordingDate ascending, nulls last");
    }

    private static void assertValidFiles(List<FileInfo> fileInfoList) {
        for (int i = 0; i < fileInfoList.size() - 1; i++) {
            assertEquals(Boolean.TRUE, fileInfoList.get(i).isValid(), "All valid fileInfo should be marked valid");
            assertNotNull(fileInfoList.get(i).getRecordingDate(), "All valid fileInfo should have a recordingDate");
            assertNotNull(fileInfoList.get(i).getIdentifier(), "All valid fileInfo should have an identifier");
            assertNotNull(fileInfoList.get(i).getChannelNames(), "All valid fileInfo should have channelNames");
            assertNotNull(fileInfoList.get(i).getChannelTransducerTypes(), "All valid fileInfo should have channelTransducerTypes");
            assertTrue(fileInfoList.get(i).getNumberOfChannels() > 0, "All valid fileInfo should have numberOfChannels");
            assertTrue(fileInfoList.get(i).getNumberOfAnnotations() >= 0, "All valid fileInfo should have a non-negative numberOfAnnotations");
            assertTrue(fileInfoList.get(i).getRecordingLengthSeconds() > 0, "All valid fileInfo should have recordingLengthSeconds");
        }
    }

    private static void assertLastInvalidFile(List<FileInfo> fileInfoList) {
        var lastFile = fileInfoList.getLast();
        assertEquals("invalid.edf", lastFile.getFileName());
        assertFalse(lastFile.isValid());
        assertNull(lastFile.getRecordingDate(), "Invalid file should not have a recordingDate");
    }

    private static List<FileInfo> getFileInfoList(String content) throws JsonProcessingException {
        return new ObjectMapper()
                .findAndRegisterModules()
                .readValue(content, new TypeReference<>() {
                });
    }
}
