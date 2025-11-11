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

import com.amilesend.client.connection.RequestException;
import com.amilesend.client.connection.file.ProgressReportingRequestBody;
import com.amilesend.client.connection.file.TransferProgressCallback;
import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.model.Api;
import com.amilesend.discogs.model.AuthenticationRequired;
import com.amilesend.discogs.model.inventory.AddInventoryRequest;
import com.amilesend.discogs.model.inventory.AddInventoryResponse;
import com.amilesend.discogs.model.inventory.ChangeInventoryRequest;
import com.amilesend.discogs.model.inventory.ChangeInventoryResponse;
import com.amilesend.discogs.model.inventory.DeleteInventoryRequest;
import com.amilesend.discogs.model.inventory.DeleteInventoryResponse;
import com.amilesend.discogs.model.inventory.GetUploadRequest;
import com.amilesend.discogs.model.inventory.GetUploadResponse;
import com.amilesend.discogs.model.inventory.GetUploadsRequest;
import com.amilesend.discogs.model.inventory.GetUploadsResponse;
import com.amilesend.discogs.model.inventory.type.UploadInformation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.io.IOException;
import java.nio.file.Path;

import static com.amilesend.client.connection.Connection.Headers.CONTENT_TYPE;
import static com.amilesend.client.connection.file.TransferFileUtil.fetchMimeTypeFromFile;

/**
 * The Discogs Inventory Upload API.
 * <br/>
 * <a href="https://www.discogs.com/developers#page:inventory-upload">API Documentation</a>
 *
 * @see ApiBase
 */
@Api
@Slf4j
public class InventoryUploadApi extends ApiBase {
    private static final String UPLOAD_FILE_FIELD_NAME = "upload";
    private static final String INVENTORY_UPLOAD_SUB_PATH = "/inventory/upload";

    /**
     * Creates a new {@code InventoryUploadApi} object.
     *
     * @param connection the underlying client connection
     */
    public InventoryUploadApi(final DiscogsConnection connection) {
        super(connection);
    }

    /**
     * Adds items from a CSV file to a user's inventory.
     *
     * @param request the request
     * @return the response
     * @see AddInventoryRequest
     * @see AddInventoryResponse
     */
    @AuthenticationRequired
    public AddInventoryResponse addInventory(@NonNull final AddInventoryRequest request) {
        final HttpUrl httpUrl = buildHttpUrl(INVENTORY_UPLOAD_SUB_PATH + "/add", request);

        final UploadInformation uploadInfo =
                uploadInternal(httpUrl, request.getInventoryCsvFile(), request.getTransferProgressCallback());

        return AddInventoryResponse.builder()
                .filename(uploadInfo.getFilename())
                .location(uploadInfo.getLocation())
                .build();
    }

    /**
     * Change items from a CSV file within a user's inventory.
     *
     * @param request the request
     * @return the response
     * @see ChangeInventoryRequest
     * @see ChangeInventoryResponse
     */
    @AuthenticationRequired
    public ChangeInventoryResponse changeInventory(@NonNull final ChangeInventoryRequest request) {
        final HttpUrl httpUrl = buildHttpUrl(INVENTORY_UPLOAD_SUB_PATH + "/change", request);

        final UploadInformation uploadInfo =
                uploadInternal(httpUrl, request.getInventoryCsvFile(), request.getTransferProgressCallback());

        return ChangeInventoryResponse.builder()
                .filename(uploadInfo.getFilename())
                .location(uploadInfo.getLocation())
                .build();
    }

    /**
     * Delete items from a user's inventory for the given CSV-formatted list of releases.
     *
     * @param request the request
     * @return the response
     * @see DeleteInventoryRequest
     * @see DeleteInventoryResponse
     */
    @AuthenticationRequired
    public DeleteInventoryResponse deleteInventory(@NonNull final DeleteInventoryRequest request) {
        final HttpUrl httpUrl = buildHttpUrl(INVENTORY_UPLOAD_SUB_PATH + "/delete", request);

        final UploadInformation uploadInfo =
                uploadInternal(httpUrl, request.getInventoryCsvFile(), request.getTransferProgressCallback());

        return DeleteInventoryResponse.builder()
                .filename(uploadInfo.getFilename())
                .location(uploadInfo.getLocation())
                .build();
    }

    private UploadInformation uploadInternal(
            final HttpUrl httpUrl,
            final Path filePath,
            final TransferProgressCallback callback) {
        try {
            final ProgressReportingRequestBody requestBody = ProgressReportingRequestBody.multiPartBuilder()
                    .fieldName(UPLOAD_FILE_FIELD_NAME)
                    .contentType(fetchMimeTypeFromFile(filePath))
                    .callback(callback)
                    .file(filePath)
                    .build();

            final DiscogsConnection connection = getConnection();
            final Request httpRequest = connection.newRequestBuilder()
                    .url(httpUrl)
                    .addHeader(CONTENT_TYPE, requestBody.contentType().toString())
                    .post(requestBody)
                    .build();
            final UploadInformation uploadInfo =
                    connection.upload(httpRequest, filePath.getFileName().toString());
            return AddInventoryResponse.builder()
                    .filename(uploadInfo.getFilename())
                    .location(uploadInfo.getLocation())
                    .build();
        } catch (final IOException ex) {
            throw new RequestException("Error reading file to upload", ex);
        }
    }

    /**
     * Gets the paginated list of uploads. Note: authentication is required.
     *
     * @param request the request
     * @return the response
     * @see GetUploadsRequest
     * @see GetUploadsResponse
     */
    @AuthenticationRequired
    public GetUploadsResponse getUploads(@NonNull final GetUploadsRequest request) {
        return executeGet(INVENTORY_UPLOAD_SUB_PATH, request, GetUploadsResponse.class);
    }

    /**
     * Gets information for a specific upload. Note: authentication is required.
     *
     * @param request the request
     * @return the response
     * @see GetUploadRequest
     * @see GetUploadResponse
     */
    @AuthenticationRequired
    public GetUploadResponse getUpload(@NonNull final GetUploadRequest request) {
        final String subPath = new StringBuilder(INVENTORY_UPLOAD_SUB_PATH)
                .append("/")
                .append(request.getUploadId())
                .toString();
        return executeGet(subPath, request, GetUploadResponse.class);
    }
}
