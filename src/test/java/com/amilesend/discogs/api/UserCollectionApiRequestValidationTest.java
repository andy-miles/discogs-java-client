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

import com.amilesend.client.util.StringUtils;
import com.amilesend.discogs.RequestValidationTestBase;
import com.amilesend.discogs.model.collection.AddToFolderRequest;
import com.amilesend.discogs.model.collection.ChangeReleaseRatingRequest;
import com.amilesend.discogs.model.collection.CreateFolderRequest;
import com.amilesend.discogs.model.collection.DeleteFolderRequest;
import com.amilesend.discogs.model.collection.DeleteInstanceRequest;
import com.amilesend.discogs.model.collection.EditInstanceFieldRequest;
import com.amilesend.discogs.model.collection.GetCollectionItemsByFolderRequest;
import com.amilesend.discogs.model.collection.GetCollectionItemsByReleaseRequest;
import com.amilesend.discogs.model.collection.GetCollectionValueRequest;
import com.amilesend.discogs.model.collection.GetCustomFieldsRequest;
import com.amilesend.discogs.model.collection.GetFolderRequest;
import com.amilesend.discogs.model.collection.GetFoldersRequest;
import com.amilesend.discogs.model.collection.MoveReleaseRequest;
import com.amilesend.discogs.model.collection.RenameFolderRequest;
import com.amilesend.discogs.model.type.SortOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserCollectionApiRequestValidationTest extends RequestValidationTestBase {
    //////////////////////
    // GetFoldersRequest
    //////////////////////

    @Test
    public void getFoldersRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetFoldersRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetFoldersRequest.builder()
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ////////////////////////
    // CreateFolderRequest
    ////////////////////////

    @Test
    public void createFolderRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> CreateFolderRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> CreateFolderRequest.builder()
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    /////////////////////
    // GetFolderRequest
    /////////////////////

    @Test
    public void getFolderRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetFolderRequest.builder()
                                .folderId(10L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetFolderRequest.builder()
                                .username(StringUtils.EMPTY)
                                .folderId(10L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetFolderRequest.builder()
                                .username("username")
                                .folderId(-1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ////////////////////////
    // RenameFolderRequest
    ////////////////////////

    @Test
    public void renameFolderRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> RenameFolderRequest.builder()
                                .folderId(10L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> RenameFolderRequest.builder()
                                .username(StringUtils.EMPTY)
                                .folderId(10L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> RenameFolderRequest.builder()
                                .username("username")
                                .folderId(-1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ////////////////////////
    // DeleteFolderRequest
    ////////////////////////

    @Test
    public void deleteFolderRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> DeleteFolderRequest.builder()
                                .folderId(10L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> DeleteFolderRequest.builder()
                                .username(StringUtils.EMPTY)
                                .folderId(10L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> DeleteFolderRequest.builder()
                                .username("username")
                                .folderId(-1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ///////////////////////////////////////
    // GetCollectionItemsByReleaseRequest
    ///////////////////////////////////////

    @Test
    public void getCollectionItemsByReleaseRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetCollectionItemsByReleaseRequest.builder()
                                .releaseId(10L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetCollectionItemsByReleaseRequest.builder()
                                .username(StringUtils.EMPTY)
                                .releaseId(10L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetCollectionItemsByReleaseRequest.builder()
                                .username("username")
                                .releaseId(-1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    //////////////////////////////////////
    // GetCollectionItemsByFolderRequest
    //////////////////////////////////////

    @Test
    public void getCollectionItemsByFolderRequest_withValidRequest_shouldPopulateQueryParameters() {
        GetCollectionItemsByFolderRequest.builder()
                .username("Username")
                .folderId(1234L)
                .sort(GetCollectionItemsByFolderRequest.Sort.TITLE)
                .sortOrder(SortOrder.ASC)
                .page(1)
                .perPage(5)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("sort", "title"),
                () -> validateQueryParameter("sort_order", "asc"),
                () -> validateQueryParameter("page", "1"),
                () -> validateQueryParameter("per_page", "5"));
    }

    @Test
    public void getCollectionItemsByFolderRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetCollectionItemsByFolderRequest.builder()
                                .folderId(10L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetCollectionItemsByFolderRequest.builder()
                                .username(StringUtils.EMPTY)
                                .folderId(10L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetCollectionItemsByFolderRequest.builder()
                                .username("username")
                                .folderId(-1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ///////////////////////
    // AddToFolderRequest
    ///////////////////////

    @Test
    public void addToFolderRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> AddToFolderRequest.builder()
                                .folderId(10L)
                                .releaseId(1234L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> AddToFolderRequest.builder()
                                .username(StringUtils.EMPTY)
                                .folderId(10L)
                                .releaseId(1234L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> AddToFolderRequest.builder()
                                .username("username")
                                .folderId(-1L)
                                .releaseId(1234L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> AddToFolderRequest.builder()
                                .username("username")
                                .folderId(10L)
                                .releaseId(-1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ///////////////////////////////
    // ChangeReleaseRatingRequest
    ///////////////////////////////

    @Test
    public void changeReleaseRatingRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> ChangeReleaseRatingRequest.builder()
                                .folderId(10L)
                                .releaseId(1234L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> ChangeReleaseRatingRequest.builder()
                                .username(StringUtils.EMPTY)
                                .folderId(10L)
                                .releaseId(1234L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> ChangeReleaseRatingRequest.builder()
                                .username("username")
                                .folderId(-1L)
                                .releaseId(1234L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> ChangeReleaseRatingRequest.builder()
                                .username("username")
                                .folderId(10L)
                                .releaseId(-1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ///////////////////////
    // MoveReleaseRequest
    ///////////////////////

    @Test
    public void moveReleaseRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> MoveReleaseRequest.builder()
                                .folderId(10L)
                                .releaseId(1234L)
                                .instanceId(9876L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> MoveReleaseRequest.builder()
                                .username(StringUtils.EMPTY)
                                .folderId(10L)
                                .releaseId(1234L)
                                .instanceId(9876L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> MoveReleaseRequest.builder()
                                .username("username")
                                .folderId(-1L)
                                .releaseId(1234L)
                                .instanceId(9876L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> MoveReleaseRequest.builder()
                                .username("username")
                                .folderId(10L)
                                .releaseId(-1L)
                                .instanceId(9876L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> MoveReleaseRequest.builder()
                                .username("username")
                                .folderId(10L)
                                .releaseId(1234L)
                                .instanceId(-1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    //////////////////////////
    // DeleteInstanceRequest
    //////////////////////////

    @Test
    public void deleteInstanceRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> DeleteInstanceRequest.builder()
                                .folderId(10L)
                                .releaseId(1234L)
                                .instanceId(9876L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> DeleteInstanceRequest.builder()
                                .username(StringUtils.EMPTY)
                                .folderId(10L)
                                .releaseId(1234L)
                                .instanceId(9876L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> DeleteInstanceRequest.builder()
                                .username("username")
                                .folderId(-1L)
                                .releaseId(1234L)
                                .instanceId(9876L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> DeleteInstanceRequest.builder()
                                .username("username")
                                .folderId(10L)
                                .releaseId(-1L)
                                .instanceId(9876L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> DeleteInstanceRequest.builder()
                                .username("username")
                                .folderId(10L)
                                .releaseId(1234L)
                                .instanceId(-1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ///////////////////////////
    // GetCustomFieldsRequest
    ///////////////////////////

    @Test
    public void getCustomFieldsRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetCustomFieldsRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetCustomFieldsRequest.builder()
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    /////////////////////////////
    // EditInstanceFieldRequest
    /////////////////////////////

    @Test
    public void editInstanceFieldRequest_withValidRequest_shouldPopulateQueryParameters() {
        EditInstanceFieldRequest.builder()
                .username("Username")
                .folderId(10L)
                .releaseId(1234L)
                .instanceId(9876L)
                .fieldId(3L)
                .value("Value")
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        validateQueryParameter("value", "Value");
    }

    @Test
    public void editNotesRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> EditInstanceFieldRequest.builder()
                                .folderId(10L)
                                .releaseId(1234L)
                                .instanceId(9876L)
                                .fieldId(3L)
                                .value("Value")
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> EditInstanceFieldRequest.builder()
                                .username(StringUtils.EMPTY)
                                .folderId(10L)
                                .releaseId(1234L)
                                .instanceId(9876L)
                                .fieldId(3L)
                                .value("Value")
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> EditInstanceFieldRequest.builder()
                                .username("username")
                                .folderId(-1L)
                                .releaseId(1234L)
                                .instanceId(9876L)
                                .fieldId(3L)
                                .value("Value")
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> EditInstanceFieldRequest.builder()
                                .username("username")
                                .folderId(10L)
                                .releaseId(-1L)
                                .instanceId(9876L)
                                .fieldId(3L)
                                .value("Value")
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> EditInstanceFieldRequest.builder()
                                .username("username")
                                .folderId(10L)
                                .releaseId(1234L)
                                .instanceId(-1L)
                                .fieldId(3L)
                                .value("Value")
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> EditInstanceFieldRequest.builder()
                                .username("username")
                                .folderId(10L)
                                .releaseId(1234L)
                                .instanceId(9876L)
                                .fieldId(-1L)
                                .value("Value")
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    //////////////////////////////
    // GetCollectionValueRequest
    //////////////////////////////

    @Test
    public void getCollectionValueRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetCollectionValueRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetCollectionValueRequest.builder()
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }
}
