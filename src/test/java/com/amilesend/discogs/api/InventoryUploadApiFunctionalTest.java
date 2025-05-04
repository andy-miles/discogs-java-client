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
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static com.amilesend.discogs.data.InventoryUploadApiDataHelper.Responses.GET_UPLOADS_RESPONSE;
import static com.amilesend.discogs.data.InventoryUploadApiDataHelper.Responses.GET_UPLOAD_RESPONSE;
import static com.amilesend.discogs.data.InventoryUploadApiDataHelper.newGetUploadResponse;
import static com.amilesend.discogs.data.InventoryUploadApiDataHelper.newGetUploadsResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InventoryUploadApiFunctionalTest extends FunctionalTestBase {
    private InventoryUploadApi apiUnderTest;

    @BeforeEach
    public void setUpClient() {
        apiUnderTest = getClient().getInventoryUploadApi();
    }

    /////////////////
    // addInventory
    /////////////////

    @Test
    public void addInventory_withValidRequest_shouldUploadAndReturnResponse(@TempDir final Path tempDir) {
        setUpMockResponse(SUCCESS_STATUS_CODE, Map.of("Location", "https://someurl/import/1234"));

        final Path fileToUpload = createFile(tempDir);
        final AddInventoryResponse actual = apiUnderTest.addInventory(
                AddInventoryRequest.builder()
                        .inventoryCsvFile(fileToUpload)
                        .transferProgressCallback(new NoOpTransferProgressCallback())
                        .build());

        final AddInventoryResponse expected = AddInventoryResponse.builder()
                .filename(fileToUpload.getFileName().toString())
                .location("https://someurl/import/1234")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void addInventory_withRequestException_shouldThrowException(@TempDir final Path tempDir) {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.addInventory(
                AddInventoryRequest.builder()
                        .inventoryCsvFile(createFile(tempDir))
                        .transferProgressCallback(new NoOpTransferProgressCallback())
                        .build()));
    }

    @Test
    public void addInventory_withResponseException_shouldThrowException(@TempDir final Path tempDir) {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.addInventory(
                AddInventoryRequest.builder()
                        .inventoryCsvFile(createFile(tempDir))
                        .transferProgressCallback(new NoOpTransferProgressCallback())
                        .build()));
    }

    @Test
    public void addInventory_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.addInventory(null));
    }

    ////////////////////
    // changeInventory
    ////////////////////

    @Test
    public void changeInventory_withValidRequest_shouldUploadAndReturnResponse(@TempDir final Path tempDir) {
        setUpMockResponse(SUCCESS_STATUS_CODE, Map.of("Location", "https://someurl/import/1234"));

        final Path fileToUpload = createFile(tempDir);
        final ChangeInventoryResponse actual = apiUnderTest.changeInventory(
                ChangeInventoryRequest.builder()
                        .inventoryCsvFile(fileToUpload)
                        .transferProgressCallback(new NoOpTransferProgressCallback())
                        .build());

        final ChangeInventoryResponse expected = ChangeInventoryResponse.builder()
                .filename(fileToUpload.getFileName().toString())
                .location("https://someurl/import/1234")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void changeInventory_withRequestException_shouldThrowException(@TempDir final Path tempDir) {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.changeInventory(
                ChangeInventoryRequest.builder()
                        .inventoryCsvFile(createFile(tempDir))
                        .transferProgressCallback(new NoOpTransferProgressCallback())
                        .build()));
    }

    @Test
    public void changeInventory_withResponseException_shouldThrowException(@TempDir final Path tempDir) {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.changeInventory(
                ChangeInventoryRequest.builder()
                        .inventoryCsvFile(createFile(tempDir))
                        .transferProgressCallback(new NoOpTransferProgressCallback())
                        .build()));
    }

    @Test
    public void changeInventory_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.changeInventory(null));
    }

    ////////////////////
    // deleteInventory
    ////////////////////

    @Test
    public void deleteInventory_withValidRequest_shouldUploadAndReturnResponse(@TempDir final Path tempDir) {
        setUpMockResponse(SUCCESS_STATUS_CODE, Map.of("Location", "https://someurl/import/1234"));

        final Path fileToUpload = createFile(tempDir);
        final DeleteInventoryResponse actual = apiUnderTest.deleteInventory(
                DeleteInventoryRequest.builder()
                        .inventoryCsvFile(fileToUpload)
                        .transferProgressCallback(new NoOpTransferProgressCallback())
                        .build());

        final DeleteInventoryResponse expected = DeleteInventoryResponse.builder()
                .filename(fileToUpload.getFileName().toString())
                .location("https://someurl/import/1234")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void deleteInventory_withRequestException_shouldThrowException(@TempDir final Path tempDir) {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.deleteInventory(
                DeleteInventoryRequest.builder()
                        .inventoryCsvFile(createFile(tempDir))
                        .transferProgressCallback(new NoOpTransferProgressCallback())
                        .build()));
    }

    @Test
    public void deleteInventory_withResponseException_shouldThrowException(@TempDir final Path tempDir) {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.deleteInventory(
                DeleteInventoryRequest.builder()
                        .inventoryCsvFile(createFile(tempDir))
                        .transferProgressCallback(new NoOpTransferProgressCallback())
                        .build()));
    }

    @Test
    public void deleteInventory_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.deleteInventory(null));
    }

    ///////////////
    // getUploads
    ///////////////

    @Test
    public void getUploads_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_UPLOADS_RESPONSE);

        final GetUploadsResponse actual = apiUnderTest.getUploads(
                GetUploadsRequest.builder()
                        .build());

        final GetUploadsResponse expected = newGetUploadsResponse(getConnection());
        assertEquals(expected, actual);
    }

    @Test
    public void getUploads_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getUploads(
                GetUploadsRequest.builder()
                        .build()));
    }

    @Test
    public void getUploads_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getUploads(
                GetUploadsRequest.builder()
                        .build()));
    }

    @Test
    public void getUploads_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getUploads(null));
    }

    //////////////
    // getUpload
    //////////////

    @Test
    public void getUpload_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_UPLOAD_RESPONSE);

        final GetUploadResponse actual = apiUnderTest.getUpload(
                GetUploadRequest.builder()
                        .uploadId(1233)
                        .build());

        final GetUploadResponse expected = newGetUploadResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void getUpload_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getUpload(
                GetUploadRequest.builder()
                        .uploadId(1233)
                        .build()));
    }

    @Test
    public void getUpload_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getUpload(
                GetUploadRequest.builder()
                        .uploadId(1233)
                        .build()));
    }

    @Test
    public void getUpload_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getUpload(null));
    }

    //////////////

    @SneakyThrows
    private Path createFile(final Path tempDir) {
        final Path filePathToUpload = tempDir.resolve("testFileToUpload.txt");
        Files.write(filePathToUpload, "TestFileContents".getBytes(StandardCharsets.UTF_8));
        return filePathToUpload;
    }
}
