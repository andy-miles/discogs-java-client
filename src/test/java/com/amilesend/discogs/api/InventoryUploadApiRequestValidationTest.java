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

import com.amilesend.discogs.RequestValidationTestBase;
import com.amilesend.discogs.model.inventory.AddInventoryRequest;
import com.amilesend.discogs.model.inventory.GetUploadRequest;
import com.amilesend.discogs.model.inventory.GetUploadsRequest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class InventoryUploadApiRequestValidationTest extends RequestValidationTestBase {
    /////////////////////////
    // InventoryRequestBase
    /////////////////////////

    @Test
    public void addInventoryRequest_withNonRegularFile_shouldThrowException() {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.isRegularFile(any(Path.class))).thenReturn(false);

            assertThrows(IllegalArgumentException.class,
                    () -> AddInventoryRequest.builder()
                            .inventoryCsvFile(mock(Path.class))
                            .transferProgressCallback(new InventoryExportApiFunctionalTest.NoOpTransferProgressCallback())
                            .build()
                            .populateQueryParameters(mockHttpUrlBuilder));
        }
    }

    @Test
    public void addInventoryRequest_withNonReadableFile_shouldThrowException() {
        try (final MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.isRegularFile(any(Path.class))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isReadable(any(Path.class))).thenReturn(false);

            assertThrows(IllegalArgumentException.class,
                    () -> AddInventoryRequest.builder()
                            .inventoryCsvFile(mock(Path.class))
                            .transferProgressCallback(new InventoryExportApiFunctionalTest.NoOpTransferProgressCallback())
                            .build()
                            .populateQueryParameters(mockHttpUrlBuilder));
        }
    }

    //////////////////////
    // GetUploadsRequest
    //////////////////////

    @Test
    public void getUploadsRequest_withValidRequest_shouldPopulateQueryParameters() {
        GetUploadsRequest.builder()
                .page(1)
                .perPage(5)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("page", "1"),
                () -> validateQueryParameter("per_page", "5"));
    }

    /////////////////////
    // GetUploadRequest
    /////////////////////

    @Test
    public void getUploadRequest_withInvalidRequest_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetUploadRequest.builder()
                        .uploadId(0)
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }
}
