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
package com.amilesend.discogs.data;

import com.amilesend.discogs.model.PaginatedResponseBase;
import com.amilesend.discogs.model.collection.AddToFolderResponse;
import com.amilesend.discogs.model.collection.CreateFolderResponse;
import com.amilesend.discogs.model.collection.GetCollectionItemsByFolderResponse;
import com.amilesend.discogs.model.collection.GetCollectionItemsByReleaseResponse;
import com.amilesend.discogs.model.collection.GetCollectionValueResponse;
import com.amilesend.discogs.model.collection.GetCustomFieldsResponse;
import com.amilesend.discogs.model.collection.GetFolderResponse;
import com.amilesend.discogs.model.collection.GetFoldersResponse;
import com.amilesend.discogs.model.collection.RenameFolderResponse;
import com.amilesend.discogs.model.collection.type.CollectionRelease;
import com.amilesend.discogs.model.collection.type.Field;
import com.amilesend.discogs.model.collection.type.Folder;
import com.amilesend.discogs.model.collection.type.Note;
import com.amilesend.discogs.model.type.Artist;
import com.amilesend.discogs.model.type.BasicInformation;
import com.amilesend.discogs.model.type.CatalogEntity;
import com.amilesend.discogs.model.type.Format;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@UtilityClass
public class UserCollectionApiDataHelper {
    ///////////////////////
    // GetFoldersResponse
    ///////////////////////

    public static GetFoldersResponse newGetFoldersResponse() {
        return GetFoldersResponse.builder()
                .folders(List.of(
                        Folder.builder()
                                .id(1231L)
                                .name("Folder1")
                                .resourceUrl("http://someurl/Folder1")
                                .count(2)
                                .build(),
                        Folder.builder()
                                .id(1232L)
                                .name("Folder2")
                                .resourceUrl("http://someurl/Folder2")
                                .count(4)
                                .build(),
                        Folder.builder()
                                .id(1233L)
                                .name("Folder3")
                                .resourceUrl("http://someurl/Folder3")
                                .count(2)
                                .build()))
                .build();
    }

    /////////////////////////
    // CreateFolderResponse
    /////////////////////////

    public static CreateFolderResponse newCreateFolderResponse() {
        return CreateFolderResponse.builder()
                .id(1231L)
                .name("Folder1")
                .resourceUrl("http://someurl/Folder1")
                .count(2)
                .build();
    }

    //////////////////////
    // GetFolderResponse
    //////////////////////

    public static GetFolderResponse newGetFolderResponse() {
        return GetFolderResponse.builder()
                .id(1231L)
                .name("Folder1")
                .resourceUrl("http://someurl/Folder1")
                .count(2)
                .build();
    }

    /////////////////////////
    // RenameFolderResponse
    /////////////////////////

    public static RenameFolderResponse newRenameFolderResponse() {
        return RenameFolderResponse.builder()
                .id(1231L)
                .name("NewFolderName")
                .resourceUrl("http://someurl/1231")
                .count(2)
                .build();
    }

    ////////////////////////////////////////
    // GetCollectionItemsByReleaseResponse
    ////////////////////////////////////////

    public static GetCollectionItemsByReleaseResponse newGetCollectionItemsByReleaseResponse() {
        return GetCollectionItemsByReleaseResponse.builder()
                .releases(List.of(newCollectionRelease(1L), newCollectionRelease(2L)))
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .items(2)
                        .perPage(10)
                        .page(1)
                        .pages(1)
                        .urls(Map.of(
                                "last", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50",
                                "next", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50"))
                        .build())
                .build();
    }

    ///////////////////////////////////////
    // GetCollectionItemsByFolderResponse
    ///////////////////////////////////////

    public static GetCollectionItemsByFolderResponse newGetCollectionItemsByFolderResponse() {
        return GetCollectionItemsByFolderResponse.builder()
                .releases(List.of(newCollectionRelease(1L), newCollectionRelease(2L)))
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .items(2)
                        .perPage(10)
                        .page(1)
                        .pages(1)
                        .urls(Map.of(
                                "last", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50",
                                "next", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50"))
                        .build())
                .build();
    }

    private static CollectionRelease newCollectionRelease(final long id) {
        return CollectionRelease.builder()
                .id(id)
                .resourceUrl("https://someurl/" + id)
                .instanceId(1000L + id)
                .rating(5)
                .basicInformation(BasicInformation.builder()
                        .id(id)
                        .labels(List.of(newLabel(1), newLabel(2)))
                        .formats(List.of(Format.builder()
                                .descriptions(List.of("Description 1", "Description 2"))
                                .name("Format Value")
                                .qty(10)
                                .build()))
                        .thumb("https://someurl/thum.jpg")
                        .coverImage("https://someurl/cover.jpg")
                        .title("Release Title " + id)
                        .artists(List.of(Artist.builder()
                                .id(15L)
                                .resourceUrl("http://someurl/15")
                                .anv("Anv Value")
                                .join("Join value")
                                .name("Artist name")
                                .role("primary")
                                .tracks("Tracks value")
                                .thumbnailUrl("http://someurl/artistthumb.jpg")
                                .build()))
                        .genres(List.of("Alternative Rock", "Rock"))
                        .styles(List.of("Style 1", "Style 2"))
                        .year(2000)
                        .build())
                .folderId(25L)
                .dateAdded(LocalDateTime.of(2001, 12, 1, 7, 0, 0))
                .notes(List.of(Note.builder()
                        .fieldId(13L)
                        .value("Note Value")
                        .build()))
                .build();
    }

    static CatalogEntity newLabel(final int index) {
        final long idValue = 10L + index;
        return CatalogEntity.builder()
                .id(idValue)
                .resourceUrl("https://someurl/" + idValue)
                .name("Record Label " + idValue)
                .entityType("entityType")
                .entityTypeName("entityType name")
                .catalogNumber("Catalog Number " +  idValue)
                .build();
    }

    ////////////////////////
    // AddToFolderResponse
    ////////////////////////

    public static AddToFolderResponse newAddToFolderResponse() {
        return AddToFolderResponse.builder()
                .instanceId(10L)
                .resourceUrl("https://someurl/10")
                .build();
    }

    ////////////////////////////
    // GetCustomFieldsResponse
    ////////////////////////////

    public static GetCustomFieldsResponse newGetCustomFieldsResponse() {
        return GetCustomFieldsResponse.builder()
                .fields(List.of(
                        Field.builder()
                                .name("Field 1")
                                .options(List.of("Option 1", "Option 2"))
                                .id(1L)
                                .position(1)
                                .type("Type 1")
                                ._public(false)
                                .build(),
                        Field.builder()
                                .name("Field 2")
                                .options(List.of("Option 3", "Option 4"))
                                .id(2L)
                                .position(3)
                                .lines(2)
                                .type("textarea")
                                ._public(true)
                                .build()))
                .build();
    }

    ///////////////////////////////
    // GetCollectionValueResponse
    ///////////////////////////////

    public static GetCollectionValueResponse newGetCollectionValueResponse() {
        return GetCollectionValueResponse.builder()
                .minimum("$60.00")
                .median("$80.00")
                .maximum("$100.00")
                .build();
    }

    @UtilityClass
    public static class Responses {
        private static final String FOLDER = "/collection/";

        public static SerializedResource GET_FOLDERS_RESPONSE =
                new SerializedResource(FOLDER + "GetFoldersResponse.json");
        public static SerializedResource CREATE_FOLDER_RESPONSE =
                new SerializedResource(FOLDER + "CreateFolderResponse.json");
        public static SerializedResource GET_FOLDER_RESPONSE =
                new SerializedResource(FOLDER + "GetFolderResponse.json");
        public static SerializedResource RENAME_FOLDER_RESPONSE =
                new SerializedResource(FOLDER + "RenameFolderResponse.json");
        public static SerializedResource GET_COLLECTION_ITEMS_RESPONSE =
                new SerializedResource(FOLDER + "GetCollectionItemsResponse.json");
        public static SerializedResource ADD_TO_FOLDER_RESPONSE =
                new SerializedResource(FOLDER + "AddToFolderResponse.json");
        public static SerializedResource GET_CUSTOM_FIELDS_RESPONSE =
                new SerializedResource(FOLDER + "GetCustomFieldsResponse.json");
        public static SerializedResource GET_COLLECTION_VALUE_RESPONSE =
                new SerializedResource(FOLDER + "GetCollectionValueResponse.json");
    }
}
