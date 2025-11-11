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
package com.amilesend.discogs.model.inventory.type;

import com.amilesend.client.connection.file.TransferProgressCallback;
import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.BodyBasedRequest;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import okhttp3.HttpUrl;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Base class for inventory upload-related requests.
 *
 * @see BodyBasedRequest
 */
@SuperBuilder
@Data
public abstract class InventoryRequestBase implements BodyBasedRequest {
    /** The inventory CSV file with the inventory items to add. */
    @NonNull
    private final Path inventoryCsvFile;
    /**
     * The callback used to be notified of transfer progress.
     *
     * @see TransferProgressCallback
     */
    private final TransferProgressCallback transferProgressCallback;

    @Override
    public HttpUrl.Builder populateQueryParameters(final HttpUrl.Builder urlBuilder) {
        Validate.isTrue(Files.isRegularFile(inventoryCsvFile), "inventoryCsvFile must be a regular file");
        Validate.isTrue(Files.isReadable(inventoryCsvFile), "inventoryCsvFile must be readable");
        return urlBuilder;
    }
}
