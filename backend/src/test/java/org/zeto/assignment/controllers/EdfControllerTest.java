package org.zeto.assignment.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.zeto.assignment.models.edf.FileInfo;
import org.zeto.assignment.services.edf.FileProcessingService;

@WebMvcTest(EdfController.class)
class EdfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileProcessingService fileProcessingService;

    @Test
    void testGetAllEdfFiles_WhenNoFilesExist_ShouldReturnEmptyList() throws Exception {
        Mockito.when(fileProcessingService.getProcessedFiles()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/edf-files").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testGetAllEdfFiles_WhenFilesExist_ShouldReturnSortedFiles() throws Exception {
        FileInfo file1 = FileInfo.builder().recordingDate(LocalDate.of(2025, 9, 10).atStartOfDay()).build();
        FileInfo file2 = FileInfo.builder().recordingDate(LocalDate.of(2025, 9, 15).atStartOfDay()).build();
        FileInfo file3 = FileInfo.builder().recordingDate(null).build();

        Mockito.when(fileProcessingService.getProcessedFiles()).thenReturn(Arrays.asList(file1, file2, file3));

        mockMvc.perform(get("/api/edf-files").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(3)))
               .andExpect(jsonPath("$[0].recordingDate", is("2025-09-10T00:00:00")))
               .andExpect(jsonPath("$[1].recordingDate", is("2025-09-15T00:00:00")))
               .andExpect(jsonPath("$[2].recordingDate", is((String) null)));
    }
}