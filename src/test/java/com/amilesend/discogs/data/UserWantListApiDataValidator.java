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

import com.amilesend.discogs.model.type.BasicInformation;
import com.amilesend.discogs.model.wantlist.AddReleaseToWantListResponse;
import com.amilesend.discogs.model.wantlist.GetWantListResponse;
import com.amilesend.discogs.model.wantlist.UpdateReleaseOnWantListResponse;
import com.amilesend.discogs.model.wantlist.type.WantListRelease;
import lombok.experimental.UtilityClass;

import java.util.Objects;

import static com.amilesend.discogs.data.DataValidatorHelper.validateListOf;
import static com.amilesend.discogs.data.DataValidatorHelper.validatePaginatedResponseBase;
import static com.amilesend.discogs.data.DataValidatorHelper.validateResource;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UtilityClass
public class UserWantListApiDataValidator {
    ////////////////////////
    // GetWantListResponse
    ////////////////////////

    public static void validateGetWantListResponse(
            final GetWantListResponse expected,
            final GetWantListResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validatePaginatedResponseBase(expected, actual),
                () -> validateListOf(
                        expected.getWants(),
                        actual.getWants(),
                        UserWantListApiDataValidator::validateWantListRelease));
    }

    private static void validateWantListRelease(final WantListRelease expected, final WantListRelease actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
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

    /////////////////////////////////
    // AddReleaseToWantListResponse
    /////////////////////////////////

    public static void validateAddReleaseToWantListResponse(
            final AddReleaseToWantListResponse expected,
            final AddReleaseToWantListResponse actual) {
        validateWantListRelease(expected, actual);
    }

    ////////////////////////////////////
    // UpdateReleaseOnWantListResponse
    ////////////////////////////////////

    public static void validateUpdateReleaseOnWantListResponse(
            final UpdateReleaseOnWantListResponse expected,
            final UpdateReleaseOnWantListResponse actual) {
        validateWantListRelease(expected, actual);
    }
}
