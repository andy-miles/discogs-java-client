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

import com.amilesend.discogs.model.identity.GetUserContributionsResponse;
import com.amilesend.discogs.model.identity.GetUserSubmissionsResponse;
import com.amilesend.discogs.model.identity.type.AuthenticatedUser;
import com.amilesend.discogs.model.identity.type.SubmissionArtist;
import com.amilesend.discogs.model.identity.type.UserProfile;
import lombok.experimental.UtilityClass;

import java.util.Objects;

import static com.amilesend.discogs.data.DataValidatorHelper.validateListOf;
import static com.amilesend.discogs.data.DataValidatorHelper.validatePaginatedResponseBase;
import static com.amilesend.discogs.data.DataValidatorHelper.validateResource;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UtilityClass
public class UserIdentityApiValidator {
    //////////////////////
    // AuthenticatedUser
    //////////////////////

    public static void validateAuthenticatedUser(final AuthenticatedUser expected, final AuthenticatedUser actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getUsername(), actual.getUsername()),
                () -> assertEquals(expected.getConsumerName(), actual.getConsumerName()));
    }

    /////////////////////////
    // UserProfileResponse
    /////////////////////////

    public static void validateUserProfile(final UserProfile expected, final UserProfile actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getProfile(), actual.getProfile()),
                () -> assertEquals(expected.getWantListUrl(), actual.getWantListUrl()),
                () -> assertEquals(expected.getRank(), actual.getRank()),
                () -> assertEquals(expected.getNumPending(), actual.getNumPending()),
                () -> assertEquals(expected.getNumForSale(), actual.getNumForSale()),
                () -> assertEquals(expected.getHomePage(), actual.getHomePage()),
                () -> assertEquals(expected.getLocation(), actual.getLocation()),
                () -> assertEquals(expected.getCollectionFoldersUrl(), actual.getCollectionFoldersUrl()),
                () -> assertEquals(expected.getUsername(), actual.getUsername()),
                () -> assertEquals(expected.getEmail(), actual.getEmail()),
                () -> assertEquals(expected.getCollectionFieldsUrl(), actual.getCollectionFieldsUrl()),
                () -> assertEquals(expected.getReleasesContributed(), actual.getReleasesContributed()),
                () -> assertEquals(expected.getRatingAvg() , actual.getRatingAvg()),
                () -> assertEquals(expected.getNumCollection(), actual.getNumCollection()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getNumWantList(), actual.getNumWantList()),
                () -> assertEquals(expected.getInventoryUrl(), actual.getInventoryUrl()),
                () -> assertEquals(expected.getAvatarUrl(), actual.getAvatarUrl()),
                () -> assertEquals(expected.getBannerUrl(), actual.getBannerUrl()),
                () -> assertEquals(expected.getUri(), actual.getUri()),
                () -> assertEquals(expected.getBuyerRating(), actual.getBuyerRating()),
                () -> assertEquals(expected.getBuyerRatingStars(), actual.getBuyerRatingStars()),
                () -> assertEquals(expected.getSellerRating(), actual.getSellerRating()),
                () -> assertEquals(expected.getSellerRatingStars(), actual.getSellerRatingStars()),
                () -> assertEquals(expected.getSellerNumRatings(), actual.getSellerNumRatings()),
                () -> assertEquals(expected.getCurrAbbr(), actual.getCurrAbbr()));
    }

    ///////////////////////////////
    // GetUserSubmissionsResponse
    ///////////////////////////////

    public static void validateGetUserSubmissionsResponse(
            final GetUserSubmissionsResponse expected,
            final GetUserSubmissionsResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validatePaginatedResponseBase(expected, actual),
                () -> validateUserSubmissions(expected.getSubmissions(), actual.getSubmissions()));
    }

    private static void validateUserSubmissions(
            final GetUserSubmissionsResponse.UserSubmissions expected,
            final GetUserSubmissionsResponse.UserSubmissions actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateListOf(
                        expected.getArtists(),
                        actual.getArtists(),
                        UserIdentityApiValidator::validateSubmissionArtist),
                () -> validateListOf(
                        expected.getLabels(),
                        actual.getLabels(),
                        DatabaseApiDataValidator::validateCatalogEntity),
                () -> validateListOf(
                        expected.getReleases(),
                        actual.getReleases(),
                        DatabaseApiDataValidator::validateRelease));
    }

    private static void validateSubmissionArtist(
            final SubmissionArtist expected,
            final SubmissionArtist actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getDataQuality(), actual.getDataQuality()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getNameVariations(), actual.getNameVariations()),
                () -> assertEquals(expected.getReleasesUrl(), actual.getReleasesUrl()),
                () -> assertEquals(expected.getUri(), actual.getUri()));
    }

    /////////////////////////////////
    // GetUserContributionsResponse
    /////////////////////////////////

    public static void validateGetUserContributionsResponse(
            final GetUserContributionsResponse expected,
            final GetUserContributionsResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validatePaginatedResponseBase(expected, actual),
                () -> validateListOf(
                        expected.getContributions(),
                        actual.getContributions(),
                        DatabaseApiDataValidator::validateRelease));
    }
}
