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
package com.amilesend.discogs;

import com.amilesend.client.connection.auth.NoOpAuthManager;
import com.amilesend.client.connection.file.TransferProgressCallback;
import com.amilesend.client.connection.http.OkHttpClientBuilder;
import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.data.SerializedResource;
import com.amilesend.discogs.parse.GsonFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;
import java.util.Objects;

import static com.amilesend.client.connection.Connection.Headers.CONTENT_ENCODING;
import static com.amilesend.client.connection.Connection.Headers.CONTENT_TYPE;

public class FunctionalTestBase {
    public static final int SUCCESS_STATUS_CODE = 200;
    public static final int USER_ERROR_CODE = 404;
    public static final int SERVICE_ERROR_CODE = 503;

    @Getter(AccessLevel.PROTECTED)
    private MockWebServer mockWebServer = new MockWebServer();
    private OkHttpClient httpClient;
    @Getter
    private DiscogsConnection connection;
    @Getter
    private Discogs client;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        httpClient = new OkHttpClientBuilder().isForTest(true).build();
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        setUpDiscogs();
    }

    @SneakyThrows
    @AfterEach
    public void cleanUp() {
        mockWebServer.close();
    }

    protected void setUpMockResponse(final int responseCode) {
        setUpMockResponse(responseCode, (SerializedResource) null);
    }

    @SneakyThrows
    protected void setUpMockResponse(final int responseCode, final SerializedResource responseBodyResource) {
        if (Objects.isNull(responseBodyResource)) {
            mockWebServer.enqueue(new MockResponse.Builder()
                    .code(responseCode)
                    .build());
            return;
        }

        final byte[] responseBodyBytes = responseBodyResource.toGzipCompressedBytes();
        mockWebServer.enqueue(new MockResponse.Builder()
                .code(responseCode)
                .addHeader(CONTENT_TYPE, "application/json; charset=utf-8")
                .addHeader(CONTENT_ENCODING, "gzip")
                .body(new Buffer().write(responseBodyBytes))
                .build());
    }

    @SneakyThrows
    protected void setUpMockResponse(final int responseCode, final Map<String, String> responseHeaders) {
        getMockWebServer().enqueue(new MockResponse.Builder()
                .code(responseCode)
                .headers(Headers.of(responseHeaders))
                .build());
    }

    protected String getMockWebServerUrl() {
        return String.format("http://%s:%d", mockWebServer.getHostName(), mockWebServer.getPort());
    }

    private void setUpDiscogs() {
        connection = DiscogsConnection.builder()
                .baseUrl(getMockWebServerUrl())
                .gsonFactory(new GsonFactory())
                .httpClient(httpClient)
                .userAgent("FunctionalTest/1.0")
                .authManager(new NoOpAuthManager())
                .build();
        client = new Discogs(connection);
    }

    public static class NoOpTransferProgressCallback implements TransferProgressCallback {
        @Override
        public void onUpdate(final long currentBytes, final long totalBytes) {
            // NoOp
        }

        @Override
        public void onFailure(final Throwable cause) {
            // NoOp
        }

        @Override
        public void onComplete(final long bytesTransferred) {
            // NoOp
        }
    }
}
