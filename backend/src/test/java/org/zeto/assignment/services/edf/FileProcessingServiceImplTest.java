package org.zeto.assignment.services.edf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zeto.assignment.models.edf.FileInfo;

@ExtendWith(MockitoExtension.class)
@DisplayName("FileProcessingServiceImpl Tests")
class FileProcessingServiceImplTest {

    @Mock
    ParserService parser;

    @InjectMocks
    FileProcessingServiceImpl fileProcessingService;

    @Test
    @DisplayName("scanAndProcessFilesOnStartup parses all .edf files from classpath edf directory")
    void scanAndProcessFilesOnStartup_parsesAll() throws Exception {

        when(parser.parse(any(File.class))).thenAnswer(invocation -> {
            var f = (File) invocation.getArgument(0);
            return FileInfo.builder().fileName(f.getName()).valid(true).build();
        });

        fileProcessingService.scanAndProcessFilesOnStartup();

        var files = fileProcessingService.getProcessedFiles();

        assertNotNull(files);
        assertEquals(5, files.size(), "There should be 5 EDF files in resources/edf");

        var actualNames = files.stream().map(FileInfo::getFileName).collect(Collectors.toSet());
        var expectedNames = Set.of(
                "ZE-970-003-287.edf",
                "ZE-970-007-593.edf",
                "ZE-970-009-297.edf",
                "ZE-970-011-242.edf",
                "invalid.edf"
        );

        assertEquals(expectedNames, actualNames);

        verify(parser, times(5)).parse(any(File.class));
    }
}
