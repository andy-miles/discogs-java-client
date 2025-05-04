/*
 * discogs-java-client - A Java SDK to access the Discogs API
 * Copyright © 2025 Andy Miles (andy.miles@amilesend.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.amilesend.discogs.csv;

import com.amilesend.discogs.csv.type.InventoryHeader;
import com.amilesend.discogs.csv.type.InventoryRecord;
import com.amilesend.discogs.csv.type.InventoryRecordType;
import com.amilesend.discogs.csv.validation.ValidationException;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryCsvWriterTest {
    private static List<InventoryHeader> NEW_MIN_HEADERS =
            List.of(InventoryHeader.RELEASE_ID, InventoryHeader.PRICE, InventoryHeader.MEDIA_CONDITION);

    @Mock
    private Path mockCsvFilePath;
    @Mock
    private CSVPrinter mockPrinter;
    private InventoryCsvWriter writerUnderTest;

    /////////////////
    // ctor/builder
    /////////////////

    @Test
    @SneakyThrows
    public void builder_withAppend_shouldReturnWriter() {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.isRegularFile(any(Path.class))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isWritable(any(Path.class))).thenReturn(true);

            final InventoryCsvWriter actual = InventoryCsvWriter.builder()
                    .csvFile(mockCsvFilePath)
                    .headers(NEW_MIN_HEADERS)
                    .recordType(InventoryRecordType.NEW)
                    .csvFormat(newMockCsvFormat(mockPrinter))
                    .isAppended(true)
                    .build();

            assertAll(
                    () -> filesMockedStatic.verify(() -> Files.isRegularFile(eq(mockCsvFilePath))),
                    () -> filesMockedStatic.verify(() -> Files.isWritable(eq(mockCsvFilePath))),
                    () -> assertEquals(mockPrinter, actual.getCsvPrinter()),
                    () -> assertEquals(InventoryRecordType.NEW, actual.getRecordType()),
                    () -> assertEquals(NEW_MIN_HEADERS, actual.getHeaders()),
                    () -> assertTrue(actual.isAppended()));
        }
    }

    @Test
    @SneakyThrows
    public void builder_withOverwrite_shouldReturnWriter() {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.createFile(any(Path.class))).thenReturn(mockCsvFilePath);

            final InventoryCsvWriter actual = InventoryCsvWriter.builder()
                    .csvFile(mockCsvFilePath)
                    .headers(NEW_MIN_HEADERS)
                    .recordType(InventoryRecordType.NEW)
                    .csvFormat(newMockCsvFormat(mockPrinter))
                    .isAppended(false)
                    .build();

            assertAll(
                    () -> filesMockedStatic.verify(() -> Files.deleteIfExists(eq(mockCsvFilePath))),
                    () -> filesMockedStatic.verify(() -> Files.createDirectories(eq(mockCsvFilePath))),
                    () -> filesMockedStatic.verify(() -> Files.createFile(eq(mockCsvFilePath))),
                    () -> assertEquals(mockPrinter, actual.getCsvPrinter()),
                    () -> assertEquals(InventoryRecordType.NEW, actual.getRecordType()),
                    () -> assertEquals(NEW_MIN_HEADERS, actual.getHeaders()),
                    () -> assertFalse(actual.isAppended()));
        }
    }

    @Test
    public void builder_withInputParameters_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> InventoryCsvWriter.builder()
                                .csvFile(null)
                                .headers(NEW_MIN_HEADERS)
                                .recordType(InventoryRecordType.NEW)
                                .csvFormat(newMockCsvFormat(mockPrinter))
                                .isAppended(true)
                                .build()),
                () -> assertThrows(NullPointerException.class,
                        () -> InventoryCsvWriter.builder()
                                .csvFile(mockCsvFilePath)
                                .headers(null)
                                .recordType(InventoryRecordType.NEW)
                                .csvFormat(newMockCsvFormat(mockPrinter))
                                .isAppended(true)
                                .build()),
                () -> assertThrows(NullPointerException.class,
                        () -> InventoryCsvWriter.builder()
                                .csvFile(mockCsvFilePath)
                                .headers(NEW_MIN_HEADERS)
                                .recordType(null)
                                .csvFormat(newMockCsvFormat(mockPrinter))
                                .isAppended(true)
                                .build()));
    }

    @Test
    public void builder_withMissingRequiredHeaders_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> InventoryCsvWriter.builder()
                                .csvFile(mockCsvFilePath)
                                .headers(List.of(InventoryHeader.RELEASE_ID, InventoryHeader.PRICE))
                                .recordType(InventoryRecordType.NEW)
                                .csvFormat(mock(CSVFormat.class))
                                .isAppended(false)
                                .build()),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> InventoryCsvWriter.builder()
                                .csvFile(mockCsvFilePath)
                                .headers(List.of(InventoryHeader.PRICE))
                                .recordType(InventoryRecordType.UPDATE)
                                .csvFormat(mock(CSVFormat.class))
                                .isAppended(false)
                                .build()));
    }

    @Test
    public void builder_withAppendAndNonRegularFile_shouldThrowException() {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.isRegularFile(any(Path.class))).thenReturn(false);

            assertThrows(IllegalArgumentException.class,
                    () -> InventoryCsvWriter.builder()
                            .csvFile(mockCsvFilePath)
                            .headers(NEW_MIN_HEADERS)
                            .recordType(InventoryRecordType.NEW)
                            .csvFormat(mock(CSVFormat.class))
                            .isAppended(true)
                            .build());
        }
    }

    @Test
    public void builder_withAppendAndNonWritableFile_shouldThrowException() {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.isRegularFile(any(Path.class))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isWritable(any(Path.class))).thenReturn(false);

            assertThrows(IllegalArgumentException.class,
                    () -> InventoryCsvWriter.builder()
                            .csvFile(mockCsvFilePath)
                            .headers(NEW_MIN_HEADERS)
                            .recordType(InventoryRecordType.NEW)
                            .csvFormat(mock(CSVFormat.class))
                            .isAppended(true)
                            .build());
        }
    }

    @Test
    public void build_withOverwriteAndIOException_shouldThrowException() {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.deleteIfExists(any(Path.class))).thenThrow(new IOException("Exception"));

            assertThrows(IOException.class,
                    () -> InventoryCsvWriter.builder()
                            .csvFile(mockCsvFilePath)
                            .headers(NEW_MIN_HEADERS)
                            .recordType(InventoryRecordType.NEW)
                            .csvFormat(mock(CSVFormat.class))
                            .isAppended(false)
                            .build());
        }
    }

    @Test
    public void build_withAppendAndIOExceptionFromPrinter_shouldThrowException() {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.isRegularFile(any(Path.class))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isWritable(any(Path.class))).thenReturn(true);

            assertThrows(IOException.class,
                    () -> InventoryCsvWriter.builder()
                            .csvFile(mockCsvFilePath)
                            .headers(NEW_MIN_HEADERS)
                            .recordType(InventoryRecordType.NEW)
                            .csvFormat(newMockCsvFormat(new IOException("Exception")))
                            .isAppended(true)
                            .build());
        }
    }

    //////////
    // write
    //////////

    @Test
    @SneakyThrows
    public void write_withValidRecord_shouldPrintRecord() {
        writerUnderTest = setUpWriterForAppend(mockCsvFilePath, mockPrinter);
        final List<String> csvRecordValue = List.of("Value1", "Value2", "Value3");
        final InventoryRecord mockRecord = mock(InventoryRecord.class);
        when(mockRecord.toCsvRow(anyList(), any(InventoryRecordType.class))).thenReturn(csvRecordValue);

        writerUnderTest.write(mockRecord);

        assertAll(
                () -> verify(mockRecord).validate(isA(InventoryRecordType.class)),
                () -> verify(mockPrinter).printRecord(eq(csvRecordValue)));
    }

    @Test
    @SneakyThrows
    public void write_withIOException_shouldThrowException() {
        writerUnderTest = setUpWriterForAppend(mockCsvFilePath, mockPrinter);
        final List<String> csvRecordValue = List.of("Value1", "Value2", "Value3");
        final InventoryRecord mockRecord = mock(InventoryRecord.class);
        when(mockRecord.toCsvRow(anyList(), any(InventoryRecordType.class))).thenReturn(csvRecordValue);
        doThrow(new IOException("Exception")).when(mockPrinter).printRecord(eq(csvRecordValue));

        assertThrows(IOException.class, () -> writerUnderTest.write(mockRecord));
    }

    @Test
    @SneakyThrows
    public void write_withValidationException_shouldThrowException() {
        writerUnderTest = setUpWriterForAppend(mockCsvFilePath, mockPrinter);
        final InventoryRecord mockRecord = mock(InventoryRecord.class);
        doThrow(new ValidationException("Exception")).when(mockRecord).validate(any(InventoryRecordType.class));

        assertThrows(ValidationException.class, () -> writerUnderTest.write(mockRecord));
    }

    @Test
    public void write_withNullRecord_shouldThrowException() {
        writerUnderTest = setUpWriterForAppend(mockCsvFilePath, mockPrinter);
        assertThrows(NullPointerException.class, () -> writerUnderTest.write(null));
    }

    //////////
    // close
    //////////

    @Test
    @SneakyThrows
    public void close_shouldClosePrinter() {
        writerUnderTest = setUpWriterForAppend(mockCsvFilePath, mockPrinter);

        writerUnderTest.close();

        verify(mockPrinter).close();
    }

    @Test
    @SneakyThrows
    public void close_withException_shouldThrowException() {
        doThrow(new IOException("Exception")).when(mockPrinter).close();
        writerUnderTest = setUpWriterForAppend(mockCsvFilePath, mockPrinter);

        assertThrows(IOException.class, () -> writerUnderTest.close());
    }

    /////////////
    // toString
    /////////////

    @Test
    public void toString_shouldReturnExpectedStringValue() {
        when(mockCsvFilePath.toString()).thenReturn("FileNameValue.csv");
        writerUnderTest = setUpWriterForAppend(mockCsvFilePath, mockPrinter);

        final String actual = writerUnderTest.toString();

        assertEquals("InventoryCsvWriter [NEW]: FileNameValue.csv", actual);
    }


    @SneakyThrows
    private static InventoryCsvWriter setUpWriterForAppend(final Path mockCsvFilePath, final CSVPrinter mockPrinter) {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.isRegularFile(any(Path.class))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isWritable(any(Path.class))).thenReturn(true);

            return spy(InventoryCsvWriter.builder()
                    .csvFile(mockCsvFilePath)
                    .headers(NEW_MIN_HEADERS)
                    .recordType(InventoryRecordType.NEW)
                    .csvFormat(newMockCsvFormat(mockPrinter))
                    .isAppended(true)
                    .build());
        }
    }

    @SneakyThrows
    private static CSVFormat newMockCsvFormat(final CSVPrinter mockPrinter) {
        final CSVFormat mockFormat = mock(CSVFormat.class);
        final CSVFormat.Builder mockFormatBuilder = mock(CSVFormat.Builder.class);
        when(mockFormatBuilder.setHeader(any(String[].class))).thenReturn(mockFormatBuilder);
        when(mockFormatBuilder.get()).thenReturn(mockFormat);
        when(mockFormat.builder()).thenReturn(mockFormatBuilder);
        when(mockFormat.print(any(Path.class), any(Charset.class))).thenReturn(mockPrinter);
        return mockFormat;
    }

    @SneakyThrows
    private static CSVFormat newMockCsvFormat(final IOException thrownException) {
        final CSVFormat mockFormat = mock(CSVFormat.class);
        final CSVFormat.Builder mockFormatBuilder = mock(CSVFormat.Builder.class);
        when(mockFormatBuilder.setHeader(any(String[].class))).thenReturn(mockFormatBuilder);
        when(mockFormatBuilder.get()).thenReturn(mockFormat);
        when(mockFormat.builder()).thenReturn(mockFormatBuilder);
        when(mockFormat.print(any(Path.class), any(Charset.class))).thenThrow(thrownException);
        return mockFormat;
    }
}
