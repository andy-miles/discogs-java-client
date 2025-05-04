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
import com.amilesend.discogs.csv.type.InventoryRecordType;
import com.amilesend.discogs.csv.validation.ValidationException;
import com.amilesend.discogs.csv.validation.ValueValidator;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryCsvValidatorTest {
    private static final List<InventoryHeader> INVENTORY_HEADERS = List.of(
            InventoryHeader.RELEASE_ID,
            InventoryHeader.PRICE,
            InventoryHeader.MEDIA_CONDITION);
    private static final List<String> PARSED_INVENTORY_HEADERS = List.of(
            InventoryHeader.RELEASE_ID.getHeader(),
            InventoryHeader.PRICE.getHeader(),
            InventoryHeader.MEDIA_CONDITION.getHeader());

    @Mock
    private CSVFormat mockCSVFormat;
    @InjectMocks
    @Spy
    private InventoryCsvValidator validatorUnderTest;

    /////////
    // ctor
    /////////

    @Test
    @SneakyThrows
    public void ctor_withNullFormat_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> new InventoryCsvValidator(null));
    }

    @Test
    @SneakyThrows
    public void ctor_withDefault_shouldReturnValidator() {
        assertNotNull(new InventoryCsvValidator());
    }

    /////////////
    // validate
    /////////////

    @Test
    @SneakyThrows
    public void validate_withValidFileAndRecordType_shouldDoNothing() {
        doNothing().when(validatorUnderTest).validateCsvPath(any(Path.class));
        final CSVParser mockParser = mock(CSVParser.class);
        when(mockParser.getHeaderNames()).thenReturn(PARSED_INVENTORY_HEADERS);
        doReturn(mockParser).when(validatorUnderTest).getCSVParser(any(Path.class));
        doReturn(INVENTORY_HEADERS)
                .when(validatorUnderTest)
                .toInventoryHeaders(anyList(), any(InventoryRecordType.class));
        final List<ValidationException.Descriptor> noErrors = Collections.emptyList();
        doReturn(noErrors).when(validatorUnderTest).scan(any(CSVParser.class), anyList());

        validatorUnderTest.validate(Path.of("SomeFile.csv"), InventoryRecordType.NEW);

        assertAll(
                () -> verify(validatorUnderTest)
                        .toInventoryHeaders(eq(PARSED_INVENTORY_HEADERS), eq(InventoryRecordType.NEW)),
                () -> verify(validatorUnderTest).scan(eq(mockParser), eq(INVENTORY_HEADERS)),
                () -> verify(mockParser).close());
    }

    @Test
    @SneakyThrows
    public void validate_withErrors_shouldThrowException() {
        doNothing().when(validatorUnderTest).validateCsvPath(any(Path.class));
        final CSVParser mockParser = mock(CSVParser.class);
        when(mockParser.getHeaderNames()).thenReturn(PARSED_INVENTORY_HEADERS);
        doReturn(mockParser).when(validatorUnderTest).getCSVParser(any(Path.class));
        doReturn(INVENTORY_HEADERS)
                .when(validatorUnderTest)
                .toInventoryHeaders(anyList(), any(InventoryRecordType.class));
        final List<ValidationException.Descriptor> errors = List.of(ValidationException.Descriptor.builder()
                .message("ErrorValue")
                .header(InventoryHeader.MEDIA_CONDITION)
                .message("MessageValue")
                .row(3)
                .col(3)
                .build());
        doReturn(errors).when(validatorUnderTest).scan(any(CSVParser.class), anyList());

        final ValidationException thrown = assertThrows(ValidationException.class,
                () -> validatorUnderTest.validate(Path.of("SomeFile.csv"), InventoryRecordType.NEW));

        assertEquals(errors, thrown.getErrors());
    }

    @Test
    @SneakyThrows
    public void validate_withIOException_shouldThrowException() {
        doNothing().when(validatorUnderTest).validateCsvPath(any(Path.class));
        doThrow(new IOException("Exception")).when(validatorUnderTest).getCSVParser(any(Path.class));
        assertThrows(IOException.class,
                () -> validatorUnderTest.validate(Path.of("SomeFile.csv"), InventoryRecordType.NEW));
    }

    @Test
    @SneakyThrows
    public void validate_withInvalidInput_shouldThrowExecption() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> validatorUnderTest.validate(null, InventoryRecordType.NEW)),
                () -> assertThrows(NullPointerException.class,
                        () -> validatorUnderTest.validate(Path.of("SomeFile.csv"), null)));
    }

    /////////////////
    // getCSVParser
    /////////////////

    @Test
    @SneakyThrows
    public void getCSVParser_withValidPath_shouldReturnParser() {
        final File mockFile = mock(File.class);
        final Path mockPath = mock(Path.class);
        when(mockPath.toFile()).thenReturn(mockFile);
        final CSVParser expected = mock(CSVParser.class);
        when(mockCSVFormat.parse(any(Reader.class))).thenReturn(expected);

        try (final MockedConstruction<FileReader> fileReaderMockedCtor = mockConstruction(FileReader.class);
             final MockedConstruction<BufferedReader> bReaderMockedCtor = mockConstruction(BufferedReader.class)) {
            final CSVParser actual = validatorUnderTest.getCSVParser(mockPath);

            assertEquals(expected, actual);
        }
    }

    @Test
    @SneakyThrows
    public void getCSVParser_withInvalidPath_shouldThrowException() {
        final File mockFile = mock(File.class);
        when(mockFile.getPath()).thenReturn("SomeFile.csv");
        final Path mockPath = mock(Path.class);
        when(mockPath.toFile()).thenReturn(mockFile);

        assertThrows(IOException.class, () -> validatorUnderTest.getCSVParser(mockPath));
    }

    /////////
    // scan
    /////////

    @Test
    public void scan_withValidParserAndHeaders_shouldReturnEmptyList() {
        final CSVParser mockParser = mockCSVParser(
                mockCSVRecord("R1V1", "R1V2", "R1V3"),
                mockCSVRecord("R2V1", "R2V2", "R2V3"),
                mockCSVRecord("R3V1", "R3V2", "R3V3"));
        final List<InventoryHeader> headers = List.of(
                mockInventoryHeader("Header1", null),
                mockInventoryHeader("Header2", null),
                mockInventoryHeader("Header3", null));

        final List<ValidationException.Descriptor> actual = validatorUnderTest.scan(mockParser, headers);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void scan_withValidationErrors_shouldReturnList() {
        final CSVParser mockParser = mockCSVParser(
                mockCSVRecord("R1V1", "R1V2", "R1V3"),
                mockCSVRecord("R2V1", "R2V2", "R2V3"),
                mockCSVRecord("R3V1", "R3V2", "R3V3"));
        final List<InventoryHeader> headers = List.of(
                mockInventoryHeader("Header1", null),
                mockInventoryHeader("Header2",
                        new ValidationException("Exception", ValidationException.Descriptor.builder()
                                .value("R*V2")
                                .message("ERROR")
                                .col(2)
                                .build())),
                mockInventoryHeader("Header3", null));

        final List<ValidationException.Descriptor> actual = validatorUnderTest.scan(mockParser, headers);

        assertEquals(3, actual.size());
    }

    @SneakyThrows
    private InventoryHeader mockInventoryHeader(final String headerValue, final ValidationException exceptionToThrow) {
        final ValueValidator mockValidator = mock(ValueValidator.class);
        if (Objects.nonNull(exceptionToThrow)) {
            doThrow(exceptionToThrow)
                    .when(mockValidator)
                    .validate(anyString(), any(InventoryHeader.class), anyInt(), anyInt());
        } else {
            doNothing().when(mockValidator)
                    .validate(anyString(), any(InventoryHeader.class), anyInt(), anyInt());
        }

        final InventoryHeader mockInventoryHeader = mock(InventoryHeader.class);
        when(mockInventoryHeader.getValidator()).thenReturn(mockValidator);
        when(mockInventoryHeader.getHeader()).thenReturn(headerValue);

        return mockInventoryHeader;
    }

    private CSVRecord mockCSVRecord(final String... values) {
        final CSVRecord mockRecord = mock(CSVRecord.class);
        mockIterable(mockRecord, values);

        if (values.length == 0) {
            return mockRecord;
        }

        if (values.length == 1) {
            when(mockRecord.get(anyString())).thenReturn(values[0]);
            return mockRecord;
        }

        final String[] valuesMinusTheFirst = Arrays.copyOfRange(values, 1, values.length);
        when(mockRecord.get((anyString()))).thenReturn(values[0], valuesMinusTheFirst);
        return mockRecord;
    }

    private CSVParser mockCSVParser(final CSVRecord... records) {
        final CSVParser mockParser = mock(CSVParser.class);
        mockIterable(mockParser, records);
        return mockParser;
    }

    public static <T> void mockIterable(final Iterable<T> iterable, final T... values) {
        final Iterator<T> mockIterator = mock(Iterator.class);
        lenient().when(iterable.iterator()).thenReturn(mockIterator);

        if (values.length == 0) {
            when(mockIterator.hasNext()).thenReturn(false);
            return;
        }

        if (values.length == 1) {
            when(mockIterator.hasNext()).thenReturn(true, false);
            when(mockIterator.next()).thenReturn(values[0]);
            return;
        }

        final Boolean[] hasNextResponses = new Boolean[values.length];
        for (int i = 0; i < hasNextResponses.length -1 ; i++) {
            hasNextResponses[i] = true;
        }
        hasNextResponses[hasNextResponses.length - 1] = false;
        lenient().when(mockIterator.hasNext()).thenReturn(true, hasNextResponses);
        final T[] valuesMinusTheFirst = Arrays.copyOfRange(values, 1, values.length);
        lenient().when(mockIterator.next()).thenReturn(values[0], valuesMinusTheFirst);
    }

    ///////////////////////
    // toInventoryHeaders
    ///////////////////////

    @Test
    @SneakyThrows
    public void toInventoryHeaders_withValidHeaders_shouldReturnList() {
        final List<String> parsedHeaders = List.of(
                InventoryHeader.RELEASE_ID.getHeader(),
                InventoryHeader.MEDIA_CONDITION.getHeader(),
                InventoryHeader.PRICE.getHeader());
        doNothing().when(validatorUnderTest).validateHeaders(anyList(), any(InventoryRecordType.class));

        final List<InventoryHeader> actual =
                validatorUnderTest.toInventoryHeaders(parsedHeaders, InventoryRecordType.NEW);

        final List<InventoryHeader> expected = List.of(
                InventoryHeader.RELEASE_ID,
                InventoryHeader.MEDIA_CONDITION,
                InventoryHeader.PRICE);
        assertEquals(expected, actual);
    }

    @Test
    @SneakyThrows
    public void toInventoryHeaders_withValidationException_shouldThrowException() {
        doThrow(new ValidationException("Exception"))
                .when(validatorUnderTest)
                .validateHeaders(anyList(), any(InventoryRecordType.class));
        final List<String> parsedHeaders = List.of(
                InventoryHeader.RELEASE_ID.getHeader(),
                InventoryHeader.MEDIA_CONDITION.getHeader(),
                InventoryHeader.PRICE.getHeader());

        assertThrows(ValidationException.class,
                () -> validatorUnderTest.toInventoryHeaders(parsedHeaders, InventoryRecordType.NEW));
    }

    ////////////////////////////
    // validateRequiredHeaders
    ////////////////////////////

    @Test
    @SneakyThrows
    public void validateRequiredHeaders_withValidHeadersForNewRecord_shouldDoNothing() {
        final List<String> parsedHeaders = List.of(
                InventoryHeader.RELEASE_ID.getHeader(),
                InventoryHeader.MEDIA_CONDITION.getHeader(),
                InventoryHeader.PRICE.getHeader());

        validatorUnderTest.validateRequiredHeaders(parsedHeaders, InventoryRecordType.NEW);
    }

    @Test
    @SneakyThrows
    public void validateRequiredHeaders_withValidHeaderForUpdateRecord_shouldDoNothing() {
        final List<String> parsedHeaders = List.of(InventoryHeader.RELEASE_ID.getHeader());

        validatorUnderTest.validateRequiredHeaders(parsedHeaders, InventoryRecordType.UPDATE);
    }

    @Test
    @SneakyThrows
    public void validateRequiredHeaders_withInvalidHeadersForNewRecord_shouldThrowException() {
        final List<String> parsedHeaders = List.of(
                InventoryHeader.RELEASE_ID.getHeader(),
                InventoryHeader.MEDIA_CONDITION.getHeader());

        assertThrows(ValidationException.class,
                () -> validatorUnderTest.validateRequiredHeaders(parsedHeaders, InventoryRecordType.NEW));
    }

    @Test
    @SneakyThrows
    public void validateRequiredHeaders_withInvalidHeadersForUpdateRecord_shouldThrowException() {
        final List<String> parsedHeaders = List.of(
                InventoryHeader.MEDIA_CONDITION.getHeader(),
                InventoryHeader.PRICE.getHeader());

        assertThrows(ValidationException.class,
                () -> validatorUnderTest.validateRequiredHeaders(parsedHeaders, InventoryRecordType.UPDATE));
    }

    ////////////////////
    // validateHeaders
    ////////////////////

    @Test
    @SneakyThrows
    public void validateHeaders_withAllRecognizedHeaders_shouldDoNothing() {
        final List<String> parsedHeaders = List.of(
                InventoryHeader.RELEASE_ID.getHeader(),
                InventoryHeader.MEDIA_CONDITION.getHeader(),
                InventoryHeader.PRICE.getHeader());
        doNothing().when(validatorUnderTest).validateRequiredHeaders(anyList(), any(InventoryRecordType.class));

        validatorUnderTest.validateHeaders(parsedHeaders, InventoryRecordType.NEW);
    }

    @Test
    @SneakyThrows
    public void validateHeaders_withUnrecognizedHeader_shouldThrowException() {
        final List<String> parsedHeaders = List.of(
                InventoryHeader.RELEASE_ID.getHeader(),
                InventoryHeader.MEDIA_CONDITION.getHeader(),
                InventoryHeader.PRICE.getHeader(),
                "unknown_column");
        doNothing().when(validatorUnderTest).validateRequiredHeaders(anyList(), any(InventoryRecordType.class));

        assertThrows(ValidationException.class,
                () -> validatorUnderTest.validateHeaders(parsedHeaders, InventoryRecordType.NEW));
    }

    ////////////////////
    // validateCsvPath
    ////////////////////

    @Test
    @SneakyThrows
    public void validateCsvPath_withValidFile_shouldDoNothing() {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isReadable(any(Path.class))).thenReturn(true);

            validatorUnderTest.validateCsvPath(mock(Path.class));
        }
    }

    @Test
    @SneakyThrows
    public void validateCsvPath_withUnreadableFile_shouldThrowException() {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isReadable(any(Path.class))).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () -> validatorUnderTest.validateCsvPath(mock(Path.class)));
        }
    }

    @Test
    @SneakyThrows
    public void validateCsvPath_withNonExistentFile_shouldThrowException() {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(any(Path.class))).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () -> validatorUnderTest.validateCsvPath(mock(Path.class)));
        }
    }
}
