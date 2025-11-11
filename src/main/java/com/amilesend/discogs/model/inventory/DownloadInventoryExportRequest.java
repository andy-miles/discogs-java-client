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
package com.amilesend.discogs.model.inventory;

import com.amilesend.client.connection.file.LogProgressCallback;
import com.amilesend.client.connection.file.TransferProgressCallback;
import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.PathParameter;
import com.amilesend.discogs.model.QueryParameterBasedRequest;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import okhttp3.HttpUrl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * The request to download an inventory export CSV file to the filesystem.
 *
 * @see QueryParameterBasedRequest
 */
@Data
public class DownloadInventoryExportRequest implements QueryParameterBasedRequest {
    /** The inventory export identifier (required). */
    @PathParameter
    private final long exportId;
    /** The folder to download the CSV to (required). */
    private final Path folderPath;
    /**
     * The callback for transfer progress (optional). Note: If {@code null}, then the default
     * {@link LogProgressCallback} will be used to log transfer progress.
     *
     * @see TransferProgressCallback
     * @see LogProgressCallback
     */
    private final TransferProgressCallback callback;

    @Builder
    private DownloadInventoryExportRequest(
            final long exportId,
            @NonNull final Path folderPath,
            final TransferProgressCallback callback) {
        this.exportId = exportId;
        this.folderPath = folderPath;
        this.callback = Optional.ofNullable(callback)
                .orElseGet(() -> LogProgressCallback.builder()
                        .prefix(new StringBuilder("[Discogs ->")
                                .append(folderPath)
                                .append("]")
                                .toString())
                        .transferType(LogProgressCallback.TransferType.DOWNLOAD)
                        .build());
    }

    @Override
    public HttpUrl.Builder populateQueryParameters(final HttpUrl.Builder urlBuilder) {
        Validate.isTrue(exportId > 0L, "exportId must be > 0");
        Validate.isTrue(Files.exists(folderPath), "folderPath must exist");
        Validate.isTrue(Files.isDirectory(folderPath), "folderPath must be a directory");
        Validate.isTrue(Files.isWritable(folderPath), "folderPath must be writable");
        return urlBuilder;
    }
}
