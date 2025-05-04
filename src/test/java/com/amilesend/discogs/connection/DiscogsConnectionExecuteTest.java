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

import com.amilesend.client.connection.RequestException;
import com.amilesend.client.connection.ResponseParseException;
import com.amilesend.client.parse.parser.GsonParser;
import com.amilesend.discogs.model.database.GetReleaseResponse;
import com.amilesend.discogs.model.inventory.type.UploadInformation;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import static com.amilesend.client.connection.Connection.Headers.CONTENT_ENCODING;
import static com.amilesend.discogs.connection.DiscogsConnection.LOCATION;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DiscogsConnectionExecuteTest extends DiscogsConnectionTestBase {
    /////////////////////////////////
    // Execute (Request, GsonParser)
    /////////////////////////////////

    @Test
    @SneakyThrows
    public void execute_withValidRequestAndParserAndGzipEncodedResponse_shouldReturnResponse() {
        setUpHttpClientMock(setUpResponseWithBody("gzip"));

        final GetReleaseResponse mockGetReleaseResponse = mock(GetReleaseResponse.class);
        final GsonParser<GetReleaseResponse> mockParser = mock(GsonParser.class);
        when(mockParser.parse(any(Gson.class), any(InputStream.class))).thenReturn(mockGetReleaseResponse);

        try (final MockedConstruction<GZIPInputStream> gzipMockCtor = mockConstruction(GZIPInputStream.class)) {
            final GetReleaseResponse actual = connectionUnderTest.execute(mock(Request.class), mockParser);

            assertAll(
                    () -> assertEquals(mockGetReleaseResponse, actual),
                    () -> verify(mockParser).parse(isA(Gson.class), isA(InputStream.class)));
        }
    }

    @Test
    @SneakyThrows
    public void execute_withValidRequestAndParserAndNonGzipEncodedResponse_shouldReturnResponse() {
        setUpHttpClientMock(setUpResponseWithBody(null));

        final GetReleaseResponse mockGetReleaseResponse = mock(GetReleaseResponse.class);
        final GsonParser<GetReleaseResponse> mockParser = mock(GsonParser.class);
        when(mockParser.parse(any(Gson.class), any(InputStream.class))).thenReturn(mockGetReleaseResponse);

        final GetReleaseResponse actual = connectionUnderTest.execute(mock(Request.class), mockParser);

        assertAll(
                () -> assertEquals(mockGetReleaseResponse, actual),
                () -> verify(mockParser).parse(isA(Gson.class), isA(InputStream.class)));
    }

    @Test
    @SneakyThrows
    public void execute_withIOException_shouldThrowException() {
        final IOException expectedCause = new IOException("Exception");
        setUpHttpClientMock(expectedCause);

        final Throwable thrown = assertThrows(RequestException.class,
                () -> connectionUnderTest.execute(mock(Request.class), mock(GsonParser.class)));

        assertEquals(expectedCause, thrown.getCause());
    }

    @Test
    @SneakyThrows
    public void execute_withJsonParseException_shouldThrowException() {
        setUpHttpClientMock(setUpResponseWithBody(null));

        final GsonParser<GetReleaseResponse> mockParser = mock(GsonParser.class);
        when(mockParser.parse(any(Gson.class), any(InputStream.class))).thenThrow(new JsonParseException("Exception"));

        final Throwable thrown = assertThrows(ResponseParseException.class,
                () -> connectionUnderTest.execute(mock(Request.class), mockParser));

        assertInstanceOf(JsonParseException.class, thrown.getCause());
    }

    @Test
    public void execute_withInvalidInput_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> connectionUnderTest.execute(null, mock(GsonParser.class))),
                () -> assertThrows(NullPointerException.class,
                        () -> connectionUnderTest.execute(mock(Request.class), null)));
    }

    //////////////////////
    // Execute (Request)
    //////////////////////

    @Test
    @SneakyThrows
    public void execute_withValidRequest_shouldReturnResponseCode() {
        final Integer expected = Integer.valueOf(SUCCESS_RESPONSE_CODE);
        final Response mockResponse = mock(Response.class);
        when(mockResponse.code()).thenReturn(expected);
        when(mockResponse.isSuccessful()).thenReturn(true);
        setUpHttpClientMock(mockResponse);

        final Response actual = connectionUnderTest.execute(mock(Request.class));

        assertEquals(expected, actual.code());
    }

    @Test
    @SneakyThrows
    public void execute_withNoParserAndIOException_shouldThrowException() {
        final IOException expectedCause = new IOException("Exception");
        setUpHttpClientMock(expectedCause);

        final Throwable thrown =
                assertThrows(RequestException.class, () -> connectionUnderTest.execute(mock(Request.class)));

        assertEquals(expectedCause, thrown.getCause());
    }

    @Test
    @SneakyThrows
    public void execute_withNoParserAndInvalidRequest() {
        assertThrows(NullPointerException.class, () -> connectionUnderTest.execute(null));
    }

    private Response setUpResponseWithBody(final String contentEncoding) {
        final InputStream mockBodyStream = mock(InputStream.class);
        final ResponseBody mockResponseBody = mock(ResponseBody.class);
        when(mockResponseBody.byteStream()).thenReturn(mockBodyStream);
        final Response mockResponse = mock(Response.class);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponse.code()).thenReturn(SUCCESS_RESPONSE_CODE);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.header(eq(CONTENT_ENCODING))).thenReturn(contentEncoding);

        return mockResponse;
    }

    ///////////
    // upload
    ///////////

    @Test
    @SneakyThrows
    public void upload_withValidRequest_shouldReturnUploadInfo() {
        setUpHttpClientMock(setUpUploadResponse("Location Value"));

        final UploadInformation actual = connectionUnderTest.upload(mock(Request.class), "File.csv");

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("Location Value", actual.getLocation()),
                () -> assertEquals("File.csv", actual.getFilename()));
    }

    private Response setUpUploadResponse(final String location) {
        final Response mockResponse = mock(Response.class);
        when(mockResponse.header(eq(LOCATION))).thenReturn(location);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.code()).thenReturn(SUCCESS_RESPONSE_CODE);

        return mockResponse;
    }
}
