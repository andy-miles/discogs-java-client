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
package com.amilesend.discogs.api;

import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.model.Api;
import com.amilesend.discogs.model.AuthenticationRequired;
import com.amilesend.discogs.model.inventory.DownloadInventoryExportRequest;
import com.amilesend.discogs.model.inventory.DownloadInventoryExportResponse;
import com.amilesend.discogs.model.inventory.ExportInventoryResponse;
import com.amilesend.discogs.model.inventory.GetExportRequest;
import com.amilesend.discogs.model.inventory.GetExportResponse;
import com.amilesend.discogs.model.inventory.GetExportsRequest;
import com.amilesend.discogs.model.inventory.GetExportsResponse;
import com.amilesend.discogs.model.inventory.type.DownloadInformation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.amilesend.discogs.connection.DiscogsConnection.LOCATION;

/**
 * The Discogs Inventory Export API.
 * <br/>
 * <a href="https://www.discogs.com/developers#page:inventory-export">API Documentation</a>
 *
 * @see ApiBase
 */
@Api
@Slf4j
public class InventoryExportApi extends ApiBase {
    private static final String INVENTORY_EXPORT_SUB_PATH = "/inventory/export";

    /**
     * Creates a new {@code InventoryExportApi} object.
     *
     * @param connection the underlying client connection
     */
    public InventoryExportApi(final DiscogsConnection connection) {
        super(connection);
    }

    /**
     * Issues a request to enqueue the export of the authenticated user's inventory into a CSV file.
     * Note: authentication is required.
     *
     * @return the response
     * @see ExportInventoryResponse
     */
    @AuthenticationRequired
    public ExportInventoryResponse exportInventory() {
        final DiscogsConnection connection = getConnection();
        final Request request = connection.newRequestBuilder()
                .url(HttpUrl.parse(connection.getBaseUrl() + INVENTORY_EXPORT_SUB_PATH))
                .post(RequestBody.create(new byte[]{}))
                .build();

        try (final Response response = connection.execute(request)) {
            return ExportInventoryResponse.builder()
                    .locationUrl(response.header(LOCATION))
                    .build();
        }
    }

    /**
     * Gets the paginated list of recent exports. Note: authentication is required.
     *
     * @param request the request
     * @return the response
     * @see GetExportsRequest
     * @see GetExportsResponse
     */
    @AuthenticationRequired
    public GetExportsResponse getExports(@NonNull final GetExportsRequest request) {
        return executeGet(INVENTORY_EXPORT_SUB_PATH, request, GetExportsResponse.class);
    }

    /**
     * Gets information for a specific export. Note: authentication is required.
     *
     * @param request the request
     * @return the response
     * @see GetExportRequest
     * @see GetExportResponse
     */
    @AuthenticationRequired
    public GetExportResponse getExport(@NonNull final GetExportRequest request) {
        final String subPath = new StringBuilder(INVENTORY_EXPORT_SUB_PATH)
                .append("/")
                .append(request.getExportId())
                .toString();
        return executeGet(subPath, request, GetExportResponse.class);
    }

    /**
     * Downloads an inventory export CSV file. Note: authentication is required.
     *
     * @param request the request
     * @return the response
     * @see DownloadInventoryExportRequest
     * @see DownloadInventoryExportResponse
     */
    @AuthenticationRequired
    public DownloadInventoryExportResponse downloadExport(@NonNull final DownloadInventoryExportRequest request) {
        final DiscogsConnection connection = getConnection();
        final String subPath = new StringBuilder(INVENTORY_EXPORT_SUB_PATH)
                .append("/")
                .append(request.getExportId())
                .append("/download")
                .toString();
        final Request httpRequest = connection.newRequestBuilderForDownload()
                .url(buildHttpUrl(subPath, request))
                .build();
        final DownloadInformation downloadInfo =
                connection.download(httpRequest, request.getFolderPath(), request.getCallback());
        return DownloadInventoryExportResponse.builder()
                .fileName(downloadInfo.getFileName())
                .sizeBytes(downloadInfo.getSizeBytes())
                .downloadPath(downloadInfo.getDownloadPath())
                .downloadedBytes(downloadInfo.getDownloadedBytes())
                .build();
    }
}
