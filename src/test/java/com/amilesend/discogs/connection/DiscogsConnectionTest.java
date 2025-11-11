/*
 * discogs-java-client - A Java client to access the Discogs API
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

import okhttp3.Headers;
import okhttp3.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.amilesend.client.connection.Connection.Headers.ACCEPT;
import static com.amilesend.client.connection.Connection.Headers.ACCEPT_ENCODING;
import static com.amilesend.client.connection.Connection.Headers.USER_AGENT;
import static com.amilesend.discogs.connection.DiscogsConnection.JSON_CONTENT_TYPE;
import static com.amilesend.discogs.connection.DiscogsConnection.TEXT_CSV_TYPE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiscogsConnectionTest extends DiscogsConnectionTestBase {
    /////////
    // ctor
    /////////

    @Test
    public void ctor_withInvalidInput_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> DiscogsConnection.builder()
                                .httpClient(null)
                                .gsonFactory(mockGsonFactory)
                                .authManager(mockAuthManager)
                                .baseUrl("http://baseurl")
                                .userAgent("ConnectionTest/1.0")
                                .build()),
                () -> assertThrows(NullPointerException.class,
                        () -> DiscogsConnection.builder()
                                .httpClient(mockHttpClient)
                                .gsonFactory(null)
                                .authManager(mockAuthManager)
                                .baseUrl("http://baseurl")
                                .userAgent("ConnectionTest/1.0")
                                .build()),
                () -> assertThrows(NullPointerException.class,
                        () -> DiscogsConnection.builder()
                                .httpClient(mockHttpClient)
                                .gsonFactory(mockGsonFactory)
                                .authManager(mockAuthManager)
                                .userAgent("ConnectionTest/1.0")
                                .build()
                                .getBaseUrl()),
                () -> assertThrows(NullPointerException.class,
                        () -> DiscogsConnection.builder()
                                .httpClient(mockHttpClient)
                                .gsonFactory(mockGsonFactory)
                                .authManager(mockAuthManager)
                                .baseUrl("http://baseurl")
                                .build()));
    }

    //////////////////////
    // newRequestBuilder
    //////////////////////

    @Test
    public void newRequestBuilder_shouldDefineHeaders() {
        final Request.Builder expected = new Request.Builder();
        when(mockAuthManager.addAuthentication(any(Request.Builder.class))).thenReturn(expected);

        final Request.Builder actual = connectionUnderTest.newRequestBuilder();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verifyHeaders(JSON_CONTENT_TYPE));
    }

    /////////////////////////////////
    // newRequestBuilderForDownload
    /////////////////////////////////

    @Test
    public void newRequestBuilderForDownload_shouldDefineHeaders() {
        final Request.Builder expected = new Request.Builder();
        when(mockAuthManager.addAuthentication(any(Request.Builder.class))).thenReturn(expected);

        final Request.Builder actual = connectionUnderTest.newRequestBuilderForDownload();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> verifyHeaders(TEXT_CSV_TYPE));
    }

    private void verifyHeaders(final String acceptType) {
        final ArgumentCaptor<Request.Builder> requestBuilderCaptor = ArgumentCaptor.forClass(Request.Builder.class);
        verify(mockAuthManager).addAuthentication(requestBuilderCaptor.capture());
        final Headers headers = requestBuilderCaptor.getValue().url("Http://someurl").build().headers();
        assertAll(
                () -> assertEquals(USER_AGENT_VALUE, headers.get(USER_AGENT)),
                () -> assertEquals(acceptType, headers.get(ACCEPT)),
                () -> assertEquals("gzip", headers.get(ACCEPT_ENCODING)));
    }
}
