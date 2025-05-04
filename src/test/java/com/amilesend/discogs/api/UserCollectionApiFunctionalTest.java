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
import com.amilesend.discogs.model.collection.AddToFolderRequest;
import com.amilesend.discogs.model.collection.AddToFolderResponse;
import com.amilesend.discogs.model.collection.ChangeReleaseRatingRequest;
import com.amilesend.discogs.model.collection.CreateFolderRequest;
import com.amilesend.discogs.model.collection.CreateFolderResponse;
import com.amilesend.discogs.model.collection.DeleteFolderRequest;
import com.amilesend.discogs.model.collection.DeleteInstanceRequest;
import com.amilesend.discogs.model.collection.EditInstanceFieldRequest;
import com.amilesend.discogs.model.collection.GetCollectionItemsByFolderRequest;
import com.amilesend.discogs.model.collection.GetCollectionItemsByFolderResponse;
import com.amilesend.discogs.model.collection.GetCollectionItemsByReleaseRequest;
import com.amilesend.discogs.model.collection.GetCollectionItemsByReleaseResponse;
import com.amilesend.discogs.model.collection.GetCollectionValueRequest;
import com.amilesend.discogs.model.collection.GetCollectionValueResponse;
import com.amilesend.discogs.model.collection.GetCustomFieldsRequest;
import com.amilesend.discogs.model.collection.GetCustomFieldsResponse;
import com.amilesend.discogs.model.collection.GetFolderRequest;
import com.amilesend.discogs.model.collection.GetFolderResponse;
import com.amilesend.discogs.model.collection.GetFoldersRequest;
import com.amilesend.discogs.model.collection.GetFoldersResponse;
import com.amilesend.discogs.model.collection.MoveReleaseRequest;
import com.amilesend.discogs.model.collection.RenameFolderRequest;
import com.amilesend.discogs.model.collection.RenameFolderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.amilesend.discogs.data.UserCollectionApiDataHelper.Responses.ADD_TO_FOLDER_RESPONSE;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.Responses.CREATE_FOLDER_RESPONSE;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.Responses.GET_COLLECTION_ITEMS_RESPONSE;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.Responses.GET_COLLECTION_VALUE_RESPONSE;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.Responses.GET_CUSTOM_FIELDS_RESPONSE;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.Responses.GET_FOLDERS_RESPONSE;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.Responses.GET_FOLDER_RESPONSE;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.Responses.RENAME_FOLDER_RESPONSE;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.newAddToFolderResponse;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.newCreateFolderResponse;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.newGetCollectionItemsByFolderResponse;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.newGetCollectionItemsByReleaseResponse;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.newGetCollectionValueResponse;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.newGetCustomFieldsResponse;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.newGetFolderResponse;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.newGetFoldersResponse;
import static com.amilesend.discogs.data.UserCollectionApiDataHelper.newRenameFolderResponse;
import static com.amilesend.discogs.data.UserCollectionApiDataValidator.validateFolder;
import static com.amilesend.discogs.data.UserCollectionApiDataValidator.validateGetCollectionItemsByFolderResponse;
import static com.amilesend.discogs.data.UserCollectionApiDataValidator.validateGetCollectionItemsByReleaseResponse;
import static com.amilesend.discogs.data.UserCollectionApiDataValidator.validateGetFoldersResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserCollectionApiFunctionalTest extends FunctionalTestBase {
    private UserCollectionApi apiUnderTest;

    @BeforeEach
    public void setUpClient() {
        apiUnderTest = getClient().getUserCollectionApi();
    }

    ///////////////
    // getFolders
    ///////////////

    @Test
    public void getFolders_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_FOLDERS_RESPONSE);

        final GetFoldersResponse actual = apiUnderTest.getFolders(
                GetFoldersRequest.builder()
                        .username("Username")
                        .build());

        final GetFoldersResponse expected = newGetFoldersResponse();
        validateGetFoldersResponse(expected, actual);
    }

    @Test
    public void getFolders_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getFolders(
                GetFoldersRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getFolders_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getFolders(
                GetFoldersRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getFolders_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getFolders(null));
    }

    /////////////////
    // createFolder
    /////////////////

    @Test
    public void createFolder_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, CREATE_FOLDER_RESPONSE);

        final CreateFolderResponse actual = apiUnderTest.createFolder(
                CreateFolderRequest.builder()
                        .username("Username")
                        .name("Folder1")
                        .build());

        final CreateFolderResponse expected = newCreateFolderResponse();
        validateFolder(expected, actual);
    }

    @Test
    public void createFolder_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.createFolder(
                CreateFolderRequest.builder()
                        .username("Username")
                        .name("Folder1")
                        .build()));
    }

    @Test
    public void createFolder_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.createFolder(
                CreateFolderRequest.builder()
                        .username("Username")
                        .name("Folder1")
                        .build()));
    }

    @Test
    public void createFolder_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.createFolder(null));
    }

    //////////////
    // getFolder
    //////////////

    @Test
    public void getFolder_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_FOLDER_RESPONSE);

        final GetFolderResponse actual = apiUnderTest.getFolder(
                GetFolderRequest.builder()
                        .username("Username")
                        .folderId(1231L)
                        .build());

        final GetFolderResponse expected = newGetFolderResponse();
        validateFolder(expected, actual);
    }

    @Test
    public void getFolder_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getFolder(
                GetFolderRequest.builder()
                        .username("Username")
                        .folderId(1231L)
                        .build()));
    }

    @Test
    public void getFolder_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getFolder(
                GetFolderRequest.builder()
                        .username("Username")
                        .folderId(1231L)
                        .build()));
    }

    @Test
    public void getFolder_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getFolder(null));
    }

    /////////////////
    // renameFolder
    /////////////////

    @Test
    public void renameFolder_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, RENAME_FOLDER_RESPONSE);

        final RenameFolderResponse actual = apiUnderTest.renameFolder(
                RenameFolderRequest.builder()
                        .username("Username")
                        .folderId(1231)
                        .name("NewFolderName")
                        .build());

        final RenameFolderResponse expected = newRenameFolderResponse();
        validateFolder(expected, actual);
    }

    @Test
    public void renameFolder_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.renameFolder(
                RenameFolderRequest.builder()
                        .username("Username")
                        .folderId(1231)
                        .name("NewFolderName")
                        .build()));
    }

    @Test
    public void renameFolder_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.renameFolder(
                RenameFolderRequest.builder()
                        .username("Username")
                        .folderId(1231)
                        .name("NewFolderName")
                        .build()));
    }

    @Test
    public void renameFolder_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.renameFolder(null));
    }

    /////////////////
    // deleteFolder
    /////////////////

    @Test
    public void deleteFolder_withValidRequest_shouldSucceed() {
        setUpMockResponse(SUCCESS_STATUS_CODE);

        apiUnderTest.deleteFolder(
                DeleteFolderRequest.builder()
                        .username("Username")
                        .folderId(1231L)
                        .build());
    }

    @Test
    public void deleteFolder_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.deleteFolder(
                DeleteFolderRequest.builder()
                        .username("Username")
                        .folderId(1231L)
                        .build()));
    }

    @Test
    public void deleteFolder_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.deleteFolder(
                DeleteFolderRequest.builder()
                        .username("Username")
                        .folderId(1231L)
                        .build()));
    }

    @Test
    public void deleteFolder_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.deleteFolder(null));
    }

    ///////////////////////
    // getCollectionItems
    ///////////////////////

    // By release

    @Test
    public void getCollectionItems_withValidRequestByRelease_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_COLLECTION_ITEMS_RESPONSE);

        final GetCollectionItemsByReleaseResponse actual = apiUnderTest.getCollectionItems(
                GetCollectionItemsByReleaseRequest.builder()
                        .username("Username")
                        .releaseId(10L)
                        .build());

        final GetCollectionItemsByReleaseResponse expected = newGetCollectionItemsByReleaseResponse();
        validateGetCollectionItemsByReleaseResponse(expected, actual);
    }

    @Test
    public void getCollectionItems_withRequestByReleaseAndRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getCollectionItems(
                GetCollectionItemsByReleaseRequest.builder()
                        .username("Username")
                        .releaseId(10L)
                        .build()));
    }

    @Test
    public void getCollectionItems_withRequestByReleaseAndResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getCollectionItems(
                GetCollectionItemsByReleaseRequest.builder()
                        .username("Username")
                        .releaseId(10L)
                        .build()));
    }

    @Test
    public void getCollectionItems_withRequestByReleaseAndNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class,
                () -> apiUnderTest.getCollectionItems((GetCollectionItemsByReleaseRequest) null));
    }

    // By folder

    @Test
    public void getCollectionItems_withValidRequestByFolder_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_COLLECTION_ITEMS_RESPONSE);

        final GetCollectionItemsByFolderResponse actual = apiUnderTest.getCollectionItems(
                GetCollectionItemsByFolderRequest.builder()
                        .username("Username")
                        .folderId(10L)
                        .build());

        final GetCollectionItemsByFolderResponse expected = newGetCollectionItemsByFolderResponse();
        validateGetCollectionItemsByFolderResponse(expected, actual);
    }

    @Test
    public void getCollectionItems_withValidRequestByFolderAndRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getCollectionItems(
                GetCollectionItemsByFolderRequest.builder()
                        .username("Username")
                        .folderId(10L)
                        .build()));
    }

    @Test
    public void getCollectionItems_withValidRequestByFolderAndResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getCollectionItems(
                GetCollectionItemsByFolderRequest.builder()
                        .username("Username")
                        .folderId(10L)
                        .build()));
    }

    @Test
    public void getCollectionItems_withValidRequestByFolderAndNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class,
                () -> apiUnderTest.getCollectionItems((GetCollectionItemsByFolderRequest) null));
    }

    ////////////////
    // addToFolder
    ////////////////

    @Test
    public void addToFolder_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, ADD_TO_FOLDER_RESPONSE);

        final AddToFolderResponse actual = apiUnderTest.addToFolder(
                AddToFolderRequest.builder()
                        .folderId(15L)
                        .username("Username")
                        .releaseId(12L)
                        .build());

        final AddToFolderResponse expected = newAddToFolderResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void addToFolder_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.addToFolder(
                AddToFolderRequest.builder()
                        .folderId(15L)
                        .username("Username")
                        .releaseId(12L)
                        .build()));
    }

    @Test
    public void addToFolder_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.addToFolder(
                AddToFolderRequest.builder()
                        .folderId(15L)
                        .username("Username")
                        .releaseId(12L)
                        .build()));
    }

    @Test
    public void addToFolder_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.addToFolder(null));
    }

    ////////////////////////
    // changeReleaseRating
    ////////////////////////

    @Test
    public void changeReleaseRating_withValidRequest_shouldSucceed() {
        setUpMockResponse(SUCCESS_STATUS_CODE);

        apiUnderTest.changeReleaseRating(ChangeReleaseRatingRequest.builder()
                .rating(8.0D)
                .folderId(15L)
                .releaseId(12L)
                .instanceId(10L)
                .username("Username")
                .build());
    }

    @Test
    public void changeReleaseRating_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.changeReleaseRating(
                ChangeReleaseRatingRequest.builder()
                        .rating(8.0D)
                        .folderId(15L)
                        .releaseId(12L)
                        .instanceId(10L)
                        .username("Username")
                        .build()));
    }

    @Test
    public void changeReleaseRating_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.changeReleaseRating(
                ChangeReleaseRatingRequest.builder()
                        .rating(8.0D)
                        .folderId(15L)
                        .releaseId(12L)
                        .instanceId(10L)
                        .username("Username")
                        .build()));
    }

    @Test
    public void changeReleaseRating_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.changeReleaseRating(null));
    }

    ////////////////
    // moveRelease
    ////////////////

    @Test
    public void moveRelease_withValidRequest_shouldSucceed() {
        setUpMockResponse(SUCCESS_STATUS_CODE);

        apiUnderTest.moveRelease(MoveReleaseRequest.builder()
                .moveToFolderId(20L)
                .folderId(15L)
                .releaseId(12L)
                .instanceId(10L)
                .username("Username")
                .build());
    }

    @Test
    public void moveRelease_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.moveRelease(MoveReleaseRequest.builder()
                .moveToFolderId(20L)
                .folderId(15L)
                .releaseId(12L)
                .instanceId(10L)
                .username("Username")
                .build()));
    }

    @Test
    public void moveRelease_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.moveRelease(MoveReleaseRequest.builder()
                .moveToFolderId(20L)
                .folderId(15L)
                .releaseId(12L)
                .instanceId(10L)
                .username("Username")
                .build()));
    }

    @Test
    public void moveRelease_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.moveRelease(null));
    }

    ///////////////////
    // deleteInstance
    ///////////////////

    @Test
    public void deleteInstance_withValidRequest_shouldSucceed() {
        setUpMockResponse(SUCCESS_STATUS_CODE);

        apiUnderTest.deleteInstance(DeleteInstanceRequest.builder()
                .folderId(15L)
                .releaseId(12L)
                .instanceId(10L)
                .username("Username")
                .build());
    }

    @Test
    public void deleteInstance_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.deleteInstance(DeleteInstanceRequest.builder()
                .folderId(15L)
                .releaseId(12L)
                .instanceId(10L)
                .username("Username")
                .build()));
    }

    @Test
    public void deleteInstance_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.deleteInstance(DeleteInstanceRequest.builder()
                .folderId(15L)
                .releaseId(12L)
                .instanceId(10L)
                .username("Username")
                .build()));
    }

    @Test
    public void deleteInstance_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.deleteInstance(null));
    }

    ////////////////////
    // getCustomFields
    ////////////////////

    @Test
    public void getCustomFields_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_CUSTOM_FIELDS_RESPONSE);

        final GetCustomFieldsResponse actual = apiUnderTest.getCustomFields(
                GetCustomFieldsRequest.builder()
                        .username("Username")
                        .build());

        final GetCustomFieldsResponse expected = newGetCustomFieldsResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void getCustomFields_withValidRequestByFolderAndRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getCustomFields(
                GetCustomFieldsRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getCustomFields_withValidRequestByFolderAndResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getCustomFields(
                GetCustomFieldsRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getCustomFields_withValidRequestByFolderAndNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getCustomFields(null));
    }

    //////////////////////
    // editInstanceField
    //////////////////////

    @Test
    public void editInstanceField_withValidRequest_shouldSucceed() {
        setUpMockResponse(SUCCESS_STATUS_CODE);

        apiUnderTest.editInstanceField(EditInstanceFieldRequest.builder()
                .fieldId(1L)
                .value("Value")
                .username("Username")
                .folderId(10L)
                .instanceId(5L)
                .releaseId(12L)
                .build());
    }

    @Test
    public void editInstanceField_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () ->apiUnderTest.editInstanceField(EditInstanceFieldRequest.builder()
                .fieldId(1L)
                .value("Value")
                .username("Username")
                .folderId(10L)
                .instanceId(5L)
                .releaseId(12L)
                .build()));
    }

    @Test
    public void editInstanceField_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.editInstanceField(EditInstanceFieldRequest.builder()
                .fieldId(1L)
                .value("Value")
                .username("Username")
                .folderId(10L)
                .instanceId(5L)
                .releaseId(12L)
                .build()));
    }

    @Test
    public void editInstanceField_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.editInstanceField(null));
    }

    ///////////////////////
    // getCollectionValue
    ///////////////////////

    @Test
    public void getCollectionValue_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_COLLECTION_VALUE_RESPONSE);

        final GetCollectionValueResponse actual = apiUnderTest.getCollectionValue(
                GetCollectionValueRequest.builder()
                        .username("Username")
                        .build());

        final GetCollectionValueResponse expected = newGetCollectionValueResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void getCollectionValue_withValidRequestByFolderAndRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getCollectionValue(
                GetCollectionValueRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getCollectionValue_withValidRequestByFolderAndResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getCollectionValue(
                GetCollectionValueRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getCollectionValue_withValidRequestByFolderAndNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getCollectionValue(null));
    }
}
