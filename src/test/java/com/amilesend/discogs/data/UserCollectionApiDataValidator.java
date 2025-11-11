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

import com.amilesend.discogs.model.collection.GetCollectionItemsByFolderResponse;
import com.amilesend.discogs.model.collection.GetCollectionItemsByReleaseResponse;
import com.amilesend.discogs.model.collection.GetFoldersResponse;
import com.amilesend.discogs.model.collection.type.CollectionRelease;
import com.amilesend.discogs.model.collection.type.Folder;
import com.amilesend.discogs.model.type.BasicInformation;
import lombok.experimental.UtilityClass;

import java.util.Objects;

import static com.amilesend.discogs.data.DataValidatorHelper.validateListOf;
import static com.amilesend.discogs.data.DataValidatorHelper.validateNamedResource;
import static com.amilesend.discogs.data.DataValidatorHelper.validatePaginatedResponseBase;
import static com.amilesend.discogs.data.DataValidatorHelper.validateResource;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UtilityClass
public class UserCollectionApiDataValidator {
    ///////////////////////
    // GetFoldersResponse
    ///////////////////////

    public static void validateGetFoldersResponse(final GetFoldersResponse expected, final GetFoldersResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        validateListOf(expected.getFolders(), actual.getFolders(), UserCollectionApiDataValidator::validateFolder);
    }

    /////////////////////////
    // CreateFolderResponse
    /////////////////////////

    public static void validateFolder(final Folder expected, final Folder actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateNamedResource(expected, actual),
                () -> assertEquals(expected.getCount(), actual.getCount()));
    }

    ////////////////////////////////////////
    // GetCollectionItemsByReleaseResponse
    ////////////////////////////////////////

    public static void validateGetCollectionItemsByReleaseResponse(
            final GetCollectionItemsByReleaseResponse expected,
            final GetCollectionItemsByReleaseResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validatePaginatedResponseBase(expected, actual),
                () -> validateListOf(
                        expected.getReleases(),
                        actual.getReleases(),
                        UserCollectionApiDataValidator::validateCollectionRelease));
    }

    ///////////////////////////////////////
    // GetCollectionItemsByFolderResponse
    ///////////////////////////////////////

    public static void validateGetCollectionItemsByFolderResponse(
            final GetCollectionItemsByFolderResponse expected,
            final GetCollectionItemsByFolderResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validatePaginatedResponseBase(expected, actual),
                () -> validateListOf(
                        expected.getReleases(),
                        actual.getReleases(),
                        UserCollectionApiDataValidator::validateCollectionRelease));
    }


    private static void validateCollectionRelease(final CollectionRelease expected, final CollectionRelease actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getInstanceId(), actual.getInstanceId()),
                () -> assertEquals(expected.getRating(), actual.getRating()),
                () -> validateBasicInformation(expected.getBasicInformation(), actual.getBasicInformation()),
                () -> assertEquals(expected.getFolderId(), actual.getFolderId()),
                () -> assertEquals(expected.getDateAdded(), actual.getDateAdded()),
                () -> assertEquals(expected.getNotes(), actual.getNotes()));
    }

    private static void validateBasicInformation(final BasicInformation expected, final BasicInformation actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> validateListOf(
                        expected.getLabels(),
                        actual.getLabels(),
                        DatabaseApiDataValidator::validateCatalogEntity),
                () -> assertEquals(expected.getFormats(), actual.getFormats()),
                () -> assertEquals(expected.getThumb(), actual.getThumb()),
                () -> assertEquals(expected.getCoverImage(), actual.getCoverImage()),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> validateListOf(
                        expected.getArtists(),
                        actual.getArtists(),
                        DatabaseApiDataValidator::validateArtist),
                () -> assertEquals(expected.getGenres(), actual.getGenres()),
                () -> assertEquals(expected.getStyles(), actual.getStyles()),
                () -> assertEquals(expected.getYear(), actual.getYear()));
    }
}
