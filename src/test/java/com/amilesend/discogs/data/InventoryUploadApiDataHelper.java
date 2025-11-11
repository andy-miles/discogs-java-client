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
package com.amilesend.discogs.data;

import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.model.inventory.GetUploadResponse;
import com.amilesend.discogs.model.inventory.GetUploadsResponse;
import com.amilesend.discogs.model.inventory.type.UploadItem;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;

import static com.amilesend.discogs.data.UserIdentityApiDataHelper.newPagination;

@UtilityClass
public class InventoryUploadApiDataHelper {
    ///////////////////////
    // GetUploadsResponse
    ///////////////////////

    public static GetUploadsResponse newGetUploadsResponse(final DiscogsConnection connection) {
        return GetUploadsResponse.builder()
                .connection(connection)
                .pagination(newPagination())
                .items(List.of(newUploadItem(1), newUploadItem(2)))
                .build();
    }

    private static UploadItem newUploadItem(final int index) {
        final long uploadId = 900L + index;
        return UploadItem.builder()
                .id(uploadId)
                .type("Upload Type " + index)
                .filename("UploadFilename_" + index + ".csv")
                .finishedTs(LocalDateTime.of(2025, 9, 13, 12, 30, 0))
                .createdTs(LocalDateTime.of(2025, 9, 13, 12, 25, 0))
                .results("Results " + index)
                .status("Status " + index)
                .build();
    }

    //////////////////////
    // GetUploadResponse
    //////////////////////

    public static GetUploadResponse newGetUploadResponse() {
        final long uploadId = 900L;
        return GetUploadResponse.builder()
                .id(uploadId)
                .type("Upload Type " + uploadId)
                .filename("UploadFilename_" + uploadId + ".csv")
                .finishedTs(LocalDateTime.of(2025, 9, 13, 12, 30, 0))
                .createdTs(LocalDateTime.of(2025, 9, 13, 12, 25, 0))
                .results("Results " + uploadId)
                .status("Status " + uploadId)
                .build();
    }


    @UtilityClass
    public static class Responses {
        private static final String FOLDER = "/inventory/";

        public static final SerializedResource GET_UPLOADS_RESPONSE =
                new SerializedResource(FOLDER + "GetUploadsResponse.json");
        public static final SerializedResource GET_UPLOAD_RESPONSE =
                new SerializedResource(FOLDER + "GetUploadResponse.json");
    }
}
