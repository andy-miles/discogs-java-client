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
package com.amilesend.discogs.api;

import com.amilesend.client.connection.RequestException;
import com.amilesend.client.connection.ResponseException;
import com.amilesend.discogs.FunctionalTestBase;
import com.amilesend.discogs.model.inventory.DownloadInventoryExportRequest;
import com.amilesend.discogs.model.inventory.DownloadInventoryExportResponse;
import com.amilesend.discogs.model.inventory.ExportInventoryResponse;
import com.amilesend.discogs.model.inventory.GetExportRequest;
import com.amilesend.discogs.model.inventory.GetExportResponse;
import com.amilesend.discogs.model.inventory.GetExportsRequest;
import com.amilesend.discogs.model.inventory.GetExportsResponse;
import lombok.SneakyThrows;
import mockwebserver3.MockResponse;
import okio.Buffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.amilesend.discogs.data.InventoryExportApiDataHelper.Responses.GET_EXPORTS_RESPONSE;
import static com.amilesend.discogs.data.InventoryExportApiDataHelper.Responses.GET_EXPORT_RESPONSE;
import static com.amilesend.discogs.data.InventoryExportApiDataHelper.newExportInventoryResponse;
import static com.amilesend.discogs.data.InventoryExportApiDataHelper.newGetExportResponse;
import static com.amilesend.discogs.data.InventoryExportApiDataHelper.newGetExportsResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InventoryExportApiFunctionalTest extends FunctionalTestBase {
    private InventoryExportApi apiUnderTest;

    @BeforeEach
    public void setUpClient() {
        apiUnderTest = getClient().getInventoryExportApi();
    }

    ////////////////////
    // exportInventory
    ////////////////////

    @Test
    public void exportInventory_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, Map.of("Location", "https://someurl/export/1234"));

        final ExportInventoryResponse actual = apiUnderTest.exportInventory();

        final ExportInventoryResponse expected = newExportInventoryResponse();
        assertEquals(expected, actual);
    }


    @Test
    public void exportInventory_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);
        assertThrows(RequestException.class, () -> apiUnderTest.exportInventory());
    }

    @Test
    public void exportInventory_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);
        assertThrows(ResponseException.class, () -> apiUnderTest.exportInventory());
    }

    ///////////////
    // getExports
    ///////////////

    @Test
    public void getExports_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_EXPORTS_RESPONSE);

        final GetExportsResponse actual = apiUnderTest.getExports(
                GetExportsRequest.builder()
                        .build());

        final GetExportsResponse expected = newGetExportsResponse(getConnection());
        assertEquals(expected, actual);
    }

    @Test
    public void getExports_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getExports(
                GetExportsRequest.builder()
                        .build()));
    }

    @Test
    public void getExports_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getExports(
                GetExportsRequest.builder()
                        .build()));
    }

    @Test
    public void getExports_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getExports(null));
    }

    //////////////
    // getExport
    //////////////

    @Test
    public void getExport_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_EXPORT_RESPONSE);

        final GetExportResponse actual = apiUnderTest.getExport(
                GetExportRequest.builder()
                        .exportId(300L)
                        .build());

        final GetExportResponse expected = newGetExportResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void getExport_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getExport(
                GetExportRequest.builder()
                        .exportId(300L)
                        .build()));
    }

    @Test
    public void getExport_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getExport(
                GetExportRequest.builder()
                        .exportId(300L)
                        .build()));
    }

    @Test
    public void getExport_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getExport(null));
    }

    ///////////////////
    // downloadExport
    ///////////////////

    @Test
    @SneakyThrows
    public void downloadExport_withValidRequest_shouldDownloadAndReturnResponse(@TempDir final Path tempDir) {
        final String filename = "TestExport.csv";
        final String expectedContent = "TestFileContent";
        final byte[] contentBytes = expectedContent.getBytes(StandardCharsets.UTF_8);
        final int contentLength = contentBytes.length;
        setUpMockResponse(SUCCESS_STATUS_CODE, filename, contentBytes);

        final DownloadInventoryExportResponse actual = apiUnderTest.downloadExport(
                DownloadInventoryExportRequest.builder()
                        .exportId(300L)
                        .folderPath(tempDir)
                        .callback(new NoOpTransferProgressCallback())
                        .build());

        // Validate downloaded content
        final Path downloadedFile = tempDir.resolve(actual.getDownloadPath());
        final String contents = Files.readString(downloadedFile);
        assertEquals(expectedContent, contents);

        // Validate response
        final DownloadInventoryExportResponse expected = DownloadInventoryExportResponse.builder()
                .fileName(filename)
                .sizeBytes(contentLength)
                .downloadedBytes(contentLength)
                .downloadPath(tempDir.resolve(filename))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void downloadExport_withRequestException_shouldThrowException(@TempDir final Path tempDir) {
        setUpMockResponse(USER_ERROR_CODE, "TestExport.csv", "TestFileContent".getBytes(StandardCharsets.UTF_8));

        assertThrows(RequestException.class, () -> apiUnderTest.downloadExport(
                DownloadInventoryExportRequest.builder()
                        .exportId(300L)
                        .folderPath(tempDir)
                        .callback(new NoOpTransferProgressCallback())
                        .build()));
    }

    @Test
    public void downloadExport_withResponseException_shouldThrowException(@TempDir final Path tempDir) {
        setUpMockResponse(SERVICE_ERROR_CODE, "TestExport.csv", "TestFileContent".getBytes(StandardCharsets.UTF_8));

        assertThrows(ResponseException.class, () -> apiUnderTest.downloadExport(
                DownloadInventoryExportRequest.builder()
                        .exportId(300L)
                        .folderPath(tempDir)
                        .callback(new NoOpTransferProgressCallback())
                        .build()));
    }

    @Test
    public void downloadExport_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.downloadExport(null));
    }

    ///////////////

    @SneakyThrows
    void setUpMockResponse(final int responseCode, final String filename, final byte[] bytesForDownload) {
        getMockWebServer().enqueue(new MockResponse.Builder()
                .code(responseCode)
                .addHeader("Content-Type", "text/csv; charset=utf-8")
                .addHeader("Content-Disposition", "attachment; filename=" + filename)
                .addHeader("Content-Length", bytesForDownload.length)
                .body(new Buffer().write(bytesForDownload))
                .build());
    }
}
