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
package com.amilesend.discogs.connection;

import com.amilesend.client.connection.ConnectionException;
import com.amilesend.client.connection.RequestException;
import com.amilesend.client.connection.file.TransferFileWriter;
import com.amilesend.client.connection.file.TransferProgressCallback;
import com.amilesend.client.util.StringUtils;
import com.amilesend.discogs.model.inventory.type.DownloadInformation;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.amilesend.discogs.connection.DiscogsConnection.CONTENT_DISPOSITION;
import static com.amilesend.discogs.connection.DiscogsConnection.CONTENT_LENGTH;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DiscogsConnectionDownloadTest extends DiscogsConnectionTestBase {
    ////////////////////////////////////
    // download
    ////////////////////////////////////

    @SneakyThrows
    @Test
    public void download_withValidRequestAndPath_shouldProcessDownloadResponse() {
        doReturn("FileNameValue")
                .when(connectionUnderTest)
                .parseFileNameFromContentDisposition(anyString());
        doReturn(BYTES_TRANSFERRED)
                .when(connectionUnderTest)
                .processDownloadResponse(
                        any(Response.class),
                        any(Path.class),
                        anyLong(),
                        any(TransferProgressCallback.class));
        final Path mockDownloadPath = mock(Path.class);
        doReturn(mockDownloadPath)
                .when(connectionUnderTest)
                .checkFolderAndGetDestinationPath(any(Path.class), anyString());
        final Response mockResponse = newMockedResponse(SUCCESS_RESPONSE_CODE);
        setupResponseHeadersForDownload(mockResponse);
        setUpHttpClientMock(mockResponse);

        final DownloadInformation actual = connectionUnderTest.download(
                mock(Request.class),
                mock(Path.class),
                mock(TransferProgressCallback.class));

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(mockDownloadPath, actual.getDownloadPath()),
                () -> assertEquals(BYTES_TRANSFERRED, actual.getDownloadedBytes()),
                () -> assertEquals("FileNameValue", actual.getFileName()),
                () -> assertEquals(BYTES_TRANSFERRED, actual.getSizeBytes()),
                () -> verify(connectionUnderTest).processDownloadResponse(
                        eq(mockResponse),
                        eq(mockDownloadPath),
                        eq(BYTES_TRANSFERRED),
                        isA(TransferProgressCallback.class)));
    }

    private Response setupResponseHeadersForDownload(final Response mockResponse) {
        when(mockResponse.header(eq(CONTENT_DISPOSITION))).thenReturn("attachment; filename=FileNameValue");
        when(mockResponse.header(eq(CONTENT_LENGTH))).thenReturn(String.valueOf(BYTES_TRANSFERRED));
        return mockResponse;
    }

    @SneakyThrows
    @Test
    public void download_withIOExceptionGettingDownloadPath_shouldThrowException() {
        doReturn("FileNameValue")
                .when(connectionUnderTest)
                .parseFileNameFromContentDisposition(anyString());
        doThrow(new IOException("Exception"))
                .when(connectionUnderTest)
                .checkFolderAndGetDestinationPath(any(Path.class), anyString());
        final TransferProgressCallback mockCallback = mock(TransferProgressCallback.class);
        final Response mockResponse = newMockedResponse(SUCCESS_RESPONSE_CODE);
        setupResponseHeadersForDownload(mockResponse);
        setUpHttpClientMock(mockResponse);

        final Throwable thrown = assertThrows(RequestException.class,
                () -> connectionUnderTest.download(mock(Request.class), mock(Path.class), mockCallback));

        assertAll(
                () -> assertInstanceOf(IOException.class, thrown.getCause()),
                () -> verify(mockCallback, never()).onFailure(any(Throwable.class)));
    }

    @SneakyThrows
    @Test
    public void download_withIOExceptionDuringProcess_shouldThrowException() {
        doReturn("FileNameValue")
                .when(connectionUnderTest)
                .parseFileNameFromContentDisposition(anyString());
        doThrow(new IOException("Exception"))
                .when(connectionUnderTest)
                .processDownloadResponse(
                        any(Response.class),
                        any(Path.class),
                        anyLong(),
                        any(TransferProgressCallback.class));
        final Path mockDownloadPath = mock(Path.class);
        doReturn(mockDownloadPath)
                .when(connectionUnderTest)
                .checkFolderAndGetDestinationPath(any(Path.class), anyString());
        final Response mockResponse = newMockedResponse(SUCCESS_RESPONSE_CODE);
        setupResponseHeadersForDownload(mockResponse);
        setUpHttpClientMock(mockResponse);
        final TransferProgressCallback mockCallback = mock(TransferProgressCallback.class);

        final Throwable thrown = assertThrows(RequestException.class,
                () -> connectionUnderTest.download(mock(Request.class), mock(Path.class), mockCallback));

        assertAll(
                () -> assertInstanceOf(IOException.class, thrown.getCause()),
                () -> verify(mockCallback, never()).onFailure(any(Throwable.class)));
    }

    @SneakyThrows
    @Test
    public void download_withConnectionException_shouldThrowException() {
        doReturn("FileNameValue")
                .when(connectionUnderTest)
                .parseFileNameFromContentDisposition(anyString());
        doThrow(new ConnectionException("Exception"))
                .when(connectionUnderTest)
                .processDownloadResponse(
                        any(Response.class),
                        any(Path.class),
                        anyLong(),
                        any(TransferProgressCallback.class));
        final Path mockDownloadPath = mock(Path.class);
        doReturn(mockDownloadPath)
                .when(connectionUnderTest)
                .checkFolderAndGetDestinationPath(any(Path.class), anyString());
        final Response mockResponse = newMockedResponse(SUCCESS_RESPONSE_CODE);
        setupResponseHeadersForDownload(mockResponse);
        setUpHttpClientMock(mockResponse);
        final TransferProgressCallback mockCallback = mock(TransferProgressCallback.class);

        assertThrows(ConnectionException.class,
                () -> connectionUnderTest.download(mock(Request.class), mock(Path.class), mockCallback));

        verify(mockCallback).onFailure(any(Throwable.class));
    }

    @SneakyThrows
    @Test
    public void download_withInvalidParameters_shouldThrowException() {
        final Request mockRequest = mock(Request.class);
        final Path mockPath = mock(Path.class);
        final TransferProgressCallback mockCallback = mock(TransferProgressCallback.class);
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> connectionUnderTest.download(
                                null, // Null Request
                                mockPath,
                                mockCallback)),
                () -> assertThrows(NullPointerException.class,
                        () -> connectionUnderTest.download(
                                mockRequest,
                                null, // Null folderPath
                                mockCallback)),
                () -> assertThrows(NullPointerException.class,
                        () -> connectionUnderTest.download(
                                mockRequest,
                                mockPath,
                                null))); // Null callback
    }

    ////////////////////////////////////////
    // parseFileNameFromContentDisposition
    ////////////////////////////////////////

    @Test
    public void parseFileNameFromContentDisposition_withValidHeaderValue_shouldReturnFilename() {
        final String actual = connectionUnderTest.parseFileNameFromContentDisposition(
                "attachment; filename=FileNameValue");
        assertEquals("FileNameValue", actual);
    }

    @Test
    public void parseFileNameFromContentDisposition_withMalformedHeaderValue_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> connectionUnderTest.parseFileNameFromContentDisposition("attachment; filename")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> connectionUnderTest.parseFileNameFromContentDisposition("attachment; filename=FilenameValue;OtherKey=Value")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> connectionUnderTest.parseFileNameFromContentDisposition("filename=FilenameValue")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> connectionUnderTest.parseFileNameFromContentDisposition(StringUtils.EMPTY)),
                () -> assertThrows(NullPointerException.class,
                        () -> connectionUnderTest.parseFileNameFromContentDisposition(null)));
    }

    /////////////////////////////////////
    // checkFolderAndGetDestinationPath
    /////////////////////////////////////

    @Test
    @SneakyThrows
    public void checkFolderAndGetDestinationPath_withValidPath_shouldReturnPath() {
        final Path expected = mock(Path.class);

        final Path mockFolderPath = mock(Path.class);
        when(mockFolderPath.toAbsolutePath()).thenReturn(mockFolderPath);
        when(mockFolderPath.normalize()).thenReturn(mockFolderPath);
        when(mockFolderPath.resolve(anyString())).thenReturn(expected);

        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(eq(mockFolderPath))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isDirectory(eq(mockFolderPath))).thenReturn(true);

            final Path actual = connectionUnderTest.checkFolderAndGetDestinationPath(mockFolderPath, "Filename");

            assertEquals(expected, actual);
        }
    }

    @Test
    @SneakyThrows
    public void checkFolderAndGetDestinationPath_withValidNonExistingPath_shouldReturnPath() {
        final Path expected = mock(Path.class);

        final Path mockFolderPath = mock(Path.class);
        when(mockFolderPath.toAbsolutePath()).thenReturn(mockFolderPath);
        when(mockFolderPath.normalize()).thenReturn(mockFolderPath);
        when(mockFolderPath.resolve(anyString())).thenReturn(expected);

        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(eq(mockFolderPath))).thenReturn(false);

            final Path actual = connectionUnderTest.checkFolderAndGetDestinationPath(mockFolderPath, "Filename");

            assertEquals(expected, actual);
        }
    }

    @Test
    @SneakyThrows
    public void checkFolderAndGetDestinationPath_withIOException_shouldThrowException() {
        final Path mockFolderPath = mock(Path.class);
        when(mockFolderPath.toAbsolutePath()).thenReturn(mockFolderPath);
        when(mockFolderPath.normalize()).thenReturn(mockFolderPath);

        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(eq(mockFolderPath))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isDirectory(eq(mockFolderPath))).thenReturn(true);
            filesMockedStatic.when(() -> Files.createDirectories(any(Path.class)))
                    .thenThrow(new IOException("Exception"));

            assertThrows(IOException.class,
                    () -> connectionUnderTest.checkFolderAndGetDestinationPath(mockFolderPath, "Filename"));
        }
    }

    @Test
    @SneakyThrows
    public void checkFolderAndGetDestinationPath_withFilePath_shouldThrowException() {
        final Path mockFolderPath = mock(Path.class);
        when(mockFolderPath.toAbsolutePath()).thenReturn(mockFolderPath);
        when(mockFolderPath.normalize()).thenReturn(mockFolderPath);

        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(eq(mockFolderPath))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isDirectory(eq(mockFolderPath))).thenReturn(false);

            assertThrows(IllegalArgumentException.class,
                    () -> connectionUnderTest.checkFolderAndGetDestinationPath(mockFolderPath, "Filename"));
        }
    }

    ////////////////////////////
    // processDownloadResponse
    ////////////////////////////

    @Test
    @SneakyThrows
    public void processDownloadResponse_withValidResponse_shouldWriteBytes() {
        final TransferFileWriter mockWriter = mock(TransferFileWriter.class);
        when(mockWriter.write(any(BufferedSource.class), anyLong())).thenReturn(BYTES_TRANSFERRED);

        final TransferFileWriter.TransferFileWriterBuilder mockBuilder =
                mock(TransferFileWriter.TransferFileWriterBuilder.class);
        when(mockBuilder.output(any(Path.class))).thenReturn(mockBuilder);
        when(mockBuilder.callback(any(TransferProgressCallback.class))).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(mockWriter);

        final BufferedSource mockSource = mock(BufferedSource.class);
        final ResponseBody mockResponseBody = mock(ResponseBody.class);
        when(mockResponseBody.source()).thenReturn(mockSource);
        final Response mockResponse = mock(Response.class);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponse.code()).thenReturn(SUCCESS_RESPONSE_CODE);
        when(mockResponse.isSuccessful()).thenReturn(true);

        try (final MockedStatic<TransferFileWriter> writerMockedStatic = mockStatic(TransferFileWriter.class)) {
            writerMockedStatic.when(() -> TransferFileWriter.builder()).thenReturn(mockBuilder);

            final long actual = connectionUnderTest.processDownloadResponse(
                    mockResponse,
                    mock(Path.class),
                    BYTES_TRANSFERRED,
                    mock(TransferProgressCallback.class));

            assertEquals(BYTES_TRANSFERRED, actual);
        }
    }

    @Test
    @SneakyThrows
    public void processDownloadResponse_withIOException_shouldThrowException() {
        final TransferFileWriter mockWriter = mock(TransferFileWriter.class);
        when(mockWriter.write(any(BufferedSource.class), anyLong())).thenThrow(new IOException("Exception"));

        final TransferFileWriter.TransferFileWriterBuilder mockBuilder =
                mock(TransferFileWriter.TransferFileWriterBuilder.class);
        when(mockBuilder.output(any(Path.class))).thenReturn(mockBuilder);
        when(mockBuilder.callback(any(TransferProgressCallback.class))).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(mockWriter);

        final BufferedSource mockSource = mock(BufferedSource.class);
        final ResponseBody mockResponseBody = mock(ResponseBody.class);
        when(mockResponseBody.source()).thenReturn(mockSource);
        final Response mockResponse = mock(Response.class);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponse.code()).thenReturn(SUCCESS_RESPONSE_CODE);
        when(mockResponse.isSuccessful()).thenReturn(true);

        try (final MockedStatic<TransferFileWriter> writerMockedStatic = mockStatic(TransferFileWriter.class)) {
            writerMockedStatic.when(() -> TransferFileWriter.builder()).thenReturn(mockBuilder);

            assertThrows(IOException.class, () -> connectionUnderTest.processDownloadResponse(
                    mockResponse,
                    mock(Path.class),
                    BYTES_TRANSFERRED,
                    mock(TransferProgressCallback.class)));
        }
    }
}
