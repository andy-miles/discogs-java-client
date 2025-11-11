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

import com.amilesend.client.connection.file.TransferProgressCallback;
import com.amilesend.discogs.RequestValidationTestBase;
import com.amilesend.discogs.model.inventory.DownloadInventoryExportRequest;
import com.amilesend.discogs.model.inventory.GetExportRequest;
import com.amilesend.discogs.model.inventory.GetExportsRequest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class InventoryExportApiRequestValidationTest extends RequestValidationTestBase {
    //////////////////////
    // GetExportsRequest
    //////////////////////

    @Test
    public void getExportsRequest_withValidRequest_shouldPopulateQueryParameters() {
        GetExportsRequest.builder()
                .page(1)
                .perPage(10)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("page", "1"),
                () -> validateQueryParameter("per_page", "10"));
    }

    /////////////////////
    // GetExportRequest
    /////////////////////

    @Test
    public void getExportRequest_withInvalidRequest_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetExportRequest.builder()
                        .exportId(0L)
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    ///////////////////////////////////
    // DownloadInventoryExportRequest
    ///////////////////////////////////

    @Test
    public void downloadInventoryExportRequest_withInvalidExportId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> DownloadInventoryExportRequest.builder()
                        .exportId(0L)
                        .folderPath(mock(Path.class))
                        .callback(mock(TransferProgressCallback.class))
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    @Test
    public void downloadInventoryExportRequest_withNonExistentFolderPath_shouldThrowException() {
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(any(Path.class))).thenReturn(false);

            assertThrows(IllegalArgumentException.class,
                    () -> DownloadInventoryExportRequest.builder()
                            .exportId(1L)
                            .folderPath(mock(Path.class))
                            .callback(mock(TransferProgressCallback.class))
                            .build()
                            .populateQueryParameters(mockHttpUrlBuilder));
        }
    }

    @Test
    public void downloadInventoryExportRequest_withDirectory_shouldThrowException() {
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isDirectory(any(Path.class))).thenReturn(true);

            assertThrows(IllegalArgumentException.class,
                    () -> DownloadInventoryExportRequest.builder()
                            .exportId(1L)
                            .folderPath(mock(Path.class))
                            .callback(mock(TransferProgressCallback.class))
                            .build()
                            .populateQueryParameters(mockHttpUrlBuilder));
        }
    }

    @Test
    public void downloadInventoryExportRequest_withNonWritableFilePath_shouldThrowException() {
        try (MockedStatic<Files> filesMockedStatic = mockStatic(Files.class)) {
            filesMockedStatic.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            filesMockedStatic.when(() -> Files.isDirectory(any(Path.class))).thenReturn(false);
            filesMockedStatic.when(() -> Files.isWritable(any(Path.class))).thenReturn(false);

            assertThrows(IllegalArgumentException.class,
                    () -> DownloadInventoryExportRequest.builder()
                            .exportId(1L)
                            .folderPath(mock(Path.class))
                            .callback(mock(TransferProgressCallback.class))
                            .build()
                            .populateQueryParameters(mockHttpUrlBuilder));
        }
    }
}
