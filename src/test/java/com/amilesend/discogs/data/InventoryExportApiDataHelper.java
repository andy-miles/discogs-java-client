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
import com.amilesend.discogs.model.inventory.ExportInventoryResponse;
import com.amilesend.discogs.model.inventory.GetExportResponse;
import com.amilesend.discogs.model.inventory.GetExportsResponse;
import com.amilesend.discogs.model.inventory.type.ExportItem;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;

import static com.amilesend.discogs.data.UserIdentityApiDataHelper.newPagination;

@UtilityClass
public class InventoryExportApiDataHelper {
    ////////////////////////////
    // ExportInventoryResponse
    ////////////////////////////

    public static ExportInventoryResponse newExportInventoryResponse() {
        return ExportInventoryResponse.builder()
                .locationUrl("https://someurl/export/1234")
                .build();
    }

    ///////////////////////
    // GetExportsResponse
    ///////////////////////

    public static GetExportsResponse newGetExportsResponse(final DiscogsConnection connection) {
        return GetExportsResponse.builder()
                .pagination(newPagination())
                .items(List.of(newExportItem(1), newExportItem(2)))
                .connection(connection)
                .build();
    }

    private static ExportItem newExportItem(final int index) {
        final long idValue = 300L + index;
        final String filename = "ExportFileName_" + idValue + ".csv";
        final String urlBase = "https://someurl/" + idValue;
        return ExportItem.builder()
                .id(idValue)
                .filename(filename)
                .downloadUrl(urlBase + "/" + filename)
                .finishedTs(LocalDateTime.of(2025, 6, 12, 13, 0, 0))
                .url(urlBase)
                .createdTs(LocalDateTime.of(2025, 5, 12, 12, 0 , 0))
                .status("Status Value")
                .build();
    }

    //////////////////////
    // GetExportResponse
    //////////////////////

    public static GetExportResponse newGetExportResponse() {
        return GetExportResponse.builder()
                .id(300L)
                .filename("ExportFileName_300.csv")
                .downloadUrl("https://someurl/300/ExportfileName_300.csv")
                .finishedTs(LocalDateTime.of(2025, 6, 12, 13, 0, 0))
                .url("https://someurl/300")
                .createdTs(LocalDateTime.of(2025, 5, 12, 12, 0 , 0))
                .status("Status Value")
                .build();
    }

    @UtilityClass
    public static class Responses {
        private static final String FOLDER = "/inventory/";

        public static final SerializedResource GET_EXPORTS_RESPONSE =
                new SerializedResource(FOLDER + "GetExportsResponse.json");
        public static final SerializedResource GET_EXPORT_RESPONSE =
                new SerializedResource(FOLDER + "GetExportResponse.json");
    }
}
