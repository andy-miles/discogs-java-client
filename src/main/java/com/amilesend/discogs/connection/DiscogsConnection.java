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

import com.amilesend.client.connection.Connection;
import com.amilesend.client.connection.ConnectionException;
import com.amilesend.client.connection.RequestException;
import com.amilesend.client.connection.auth.AuthManager;
import com.amilesend.client.connection.file.TransferFileWriter;
import com.amilesend.client.connection.file.TransferProgressCallback;
import com.amilesend.client.parse.parser.GsonParser;
import com.amilesend.client.util.Validate;
import com.amilesend.client.util.VisibleForTesting;
import com.amilesend.discogs.connection.auth.AuthVerifier;
import com.amilesend.discogs.connection.auth.NoOpAuthVerifier;
import com.amilesend.discogs.model.inventory.type.DownloadInformation;
import com.amilesend.discogs.model.inventory.type.UploadInformation;
import com.amilesend.discogs.parse.GsonFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static com.amilesend.client.connection.Connection.Headers.ACCEPT;
import static com.amilesend.client.connection.Connection.Headers.ACCEPT_ENCODING;
import static com.amilesend.client.connection.Connection.Headers.USER_AGENT;

/**
 * Wraps a {@link OkHttpClient} that manages authentication refresh and parsing responses to corresponding POJO types.
 *
 * @see AuthManager
 */
@SuperBuilder
@Getter
@Slf4j
public class DiscogsConnection extends Connection<GsonFactory> {
    public static final String DEFAULT_BASE_URL = "https://api.discogs.com";
    public static final String TEXT_CSV_TYPE = "text/csv; charset=utf-8";
    public static final String LOCATION = "Location";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_LENGTH = "Content-Length";

    /** The authorization verifier used to check if API calls require authentication prior to invoking the API. */
    @Builder.Default
    private final AuthVerifier authVerifier = new NoOpAuthVerifier();

    /**
     * Creates a new {@link Request.Builder} with pre-configured headers for a download request that expects a
     * CSV-formatted response.
     *
     * @return the request builder
     */
    public Request.Builder newRequestBuilderForDownload() {
        final Request.Builder requestBuilder = new Request.Builder()
                .addHeader(USER_AGENT, getUserAgent())
                .addHeader(ACCEPT, TEXT_CSV_TYPE);
        if (isGzipContentEncodingEnabled()) {
            requestBuilder.addHeader(ACCEPT_ENCODING, GZIP_ENCODING);
        }

        return getAuthManager().addAuthentication(requestBuilder);
    }

    @Override
    public <T> T execute(@NonNull final Request request, @NonNull final GsonParser<T> parser)
            throws ConnectionException {
        authVerifier.checkIfAuthenticated(getAuthManager());
        return super.execute(request, parser);
    }

    /**
     * Uploads the contents for a given {@code request}.
     *
     * @param request the request
     * @param filename the name of the file that is uploaded
     * @return the upload information
     * @throws ConnectionException if an error occurred while uploading the content for the request
     * @see UploadInformation
     */
    public UploadInformation upload(@NonNull final Request request, final String filename) {
        try (final Response response = execute(request)) {
            return UploadInformation.builder()
                    .filename(filename)
                    .location(response.header(LOCATION))
                    .build();
        }
    }

    @Override
    public Response execute(@NonNull final Request request) throws ConnectionException {
        authVerifier.checkIfAuthenticated(getAuthManager());
        return super.execute(request);
    }

    /**
     * Downloads the contents for the given {@code request} to the specified {@code folderPath}.
     *
     * @param request the request
     * @param folderPath the path of the folder to download the content to
     * @param callback the {@link TransferProgressCallback} call to invoke to report download transfer progress
     * @return the download information
     * @throws ConnectionException if an error occurred while downloading the content for the request
     * @see DownloadInformation
     */
    public DownloadInformation download(
            @NonNull final Request request,
            @NonNull final Path folderPath,
            @NonNull final TransferProgressCallback callback) throws ConnectionException {
        authVerifier.checkIfAuthenticated(getAuthManager());

        try (final Response response = getHttpClient().newCall(request).execute()) {
            final String fileName = parseFileNameFromContentDisposition(response.header(CONTENT_DISPOSITION));
            final long sizeBytes = Optional.of(response)
                            .map(r -> r.header(CONTENT_LENGTH))
                            .map(Long::parseLong)
                            .orElseThrow(() ->
                                    new IllegalStateException("Response does not define a CONTENT_LENGTH header"));
            final Path downloadPath = checkFolderAndGetDestinationPath(folderPath, fileName);

            final long downloadBytes = processDownloadResponse(response, downloadPath, sizeBytes, callback);
            return DownloadInformation.builder()
                    .fileName(fileName)
                    .sizeBytes(sizeBytes)
                    .downloadPath(downloadPath)
                    .downloadedBytes(downloadBytes)
                    .build();
        } catch (final ConnectionException ex) {
            // Response failed validation, notify the callback
            callback.onFailure(ex);
            throw ex;
        } catch (final Exception ex) {
            // The underlying TransferFileWriter will record an onFailure to the callback.
            throw new RequestException("Unable to execute request: " + ex.getMessage(), ex);
        }
    }

    @VisibleForTesting
    String parseFileNameFromContentDisposition(final String headerValue) {
        Validate.notBlank(headerValue, "Content-Disposition header value must not be blank");
        final String validationErrorMsg = "Content-Disposition header value contains unexpected format: " + headerValue;
        Validate.isTrue(headerValue.startsWith("attachment; filename="), validationErrorMsg);

        final String[] tokens = headerValue.split("=");
        Validate.isTrue(tokens.length == 2, validationErrorMsg);

        return tokens[1];
    }

    @VisibleForTesting
    Path checkFolderAndGetDestinationPath(final Path folderPath, final String name) throws IOException {
        final Path normalizedFolderPath = folderPath.toAbsolutePath().normalize();
        if (Files.exists(normalizedFolderPath) && !Files.isDirectory(normalizedFolderPath)) {
            throw new IllegalArgumentException(normalizedFolderPath + " must not already exist as a file");
        }

        Files.createDirectories(normalizedFolderPath);
        return normalizedFolderPath.resolve(name);
    }

    @VisibleForTesting
    long processDownloadResponse(
            final Response response,
            final Path downloadPath,
            final long sizeBytes,
            final TransferProgressCallback callback) throws IOException {
        validateResponseCode(response);

        final long totalBytes = TransferFileWriter.builder()
                .output(downloadPath)
                .callback(callback)
                .build()
                .write(response.body().source(), sizeBytes);

        if (log.isDebugEnabled()) {
            log.debug("Downloaded [{}] bytes to [{}]", totalBytes, downloadPath);
        }
        return totalBytes;
    }
}
