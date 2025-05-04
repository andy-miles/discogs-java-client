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
import com.amilesend.discogs.model.identity.EditUserProfileResponse;
import com.amilesend.discogs.model.identity.GetUserContributionsResponse;
import com.amilesend.discogs.model.identity.GetUserProfileResponse;
import com.amilesend.discogs.model.identity.GetUserSubmissionsResponse;
import com.amilesend.discogs.model.identity.type.AuthenticatedUser;
import com.amilesend.discogs.model.identity.type.SubmissionArtist;
import com.amilesend.discogs.model.type.CatalogEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.amilesend.discogs.data.DatabaseApiDataHelper.newRelease;

@UtilityClass
public class UserIdentityApiDataHelper {
    //////////////////////
    // AuthenticatedUser
    //////////////////////

    public static AuthenticatedUser newAuthenticatedUser() {
        return AuthenticatedUser.builder()
                .id(1000L)
                .resourceUrl("https://someurl/1000")
                .username("Username Value")
                .consumerName("Consumer Name Value")
                .build();
    }

    ///////////////////////////
    // GetUserProfileResponse
    ///////////////////////////

    public static GetUserProfileResponse newGetUserProfileResponse() {
        return GetUserProfileResponse.builder()
                .id(4300L)
                .resourceUrl("https://someurl/4300")
                .profile("Profile value")
                .wantListUrl("https://someurl/4300/want/")
                .rank(512)
                .numPending(1)
                .numForSale(10)
                .homePage("https://someurl")
                .location("USA")
                .collectionFoldersUrl("https://someurl/4300/folders/")
                .username("UsernameValue")
                .email("some@user.com")
                .collectionFieldsUrl("https://someurl/4300/fields")
                .releasesContributed(1)
                .registered(LocalDateTime.of(2015, 3, 12, 14, 23, 00))
                .ratingAvg(8D)
                .numCollection(40)
                .name("Name Value")
                .numWantList(2)
                .inventoryUrl("https://someurl/4300/inventory")
                .avatarUrl("https://someurl/avatar.jpg")
                .bannerUrl("https://someurl/banner.jpg")
                .uri("https://someurl/4300")
                .buyerRating(7.9D)
                .buyerRatingStars(4)
                .buyerNumRatings(30)
                .sellerNumRatings(13)
                .sellerRating(7.4D)
                .sellerRatingStars(4)
                .sellerNumRatings(20)
                .currAbbr("USD")
                .build();
    }

    ////////////////////////////
    // EditUserProfileResponse
    ////////////////////////////

    public static EditUserProfileResponse newEditUserProfileResponse() {
        return EditUserProfileResponse.builder()
                .id(4300L)
                .resourceUrl("https://someurl/4300")
                .profile("Profile value")
                .wantListUrl("https://someurl/4300/want/")
                .rank(512)
                .numPending(1)
                .numForSale(10)
                .homePage("https://someurl")
                .location("USA")
                .collectionFoldersUrl("https://someurl/4300/folders/")
                .username("UsernameValue")
                .email("some@user.com")
                .collectionFieldsUrl("https://someurl/4300/fields")
                .releasesContributed(1)
                .registered(LocalDateTime.of(2015, 3, 12, 14, 23, 00))
                .ratingAvg(8D)
                .numCollection(40)
                .name("Name Value")
                .numWantList(2)
                .inventoryUrl("https://someurl/4300/inventory")
                .avatarUrl("https://someurl/avatar.jpg")
                .bannerUrl("https://someurl/banner.jpg")
                .uri("https://someurl/4300")
                .buyerRating(7.9D)
                .buyerRatingStars(4)
                .buyerNumRatings(30)
                .sellerNumRatings(13)
                .sellerRating(7.4D)
                .sellerRatingStars(4)
                .sellerNumRatings(20)
                .currAbbr("USD")
                .build();
    }

    ///////////////////////////////
    // GetUserSubmissionsResponse
    ///////////////////////////////

    public static GetUserSubmissionsResponse newGetUserSubmissionsResponse() {
        return GetUserSubmissionsResponse.builder()
                .submissions(GetUserSubmissionsResponse.UserSubmissions.builder()
                        .artists(List.of(newSubmissionArtist(0), newSubmissionArtist(1)))
                        .labels(List.of(newLabel(0), newLabel(1)))
                        .releases(List.of(newRelease(0), newRelease(1)))
                        .build())
                .pagination(newPagination())
                .build();
    }

    public static PaginatedResponseBase.Pagination newPagination() {
        return PaginatedResponseBase.Pagination.builder()
                .page(1)
                .pages(2)
                .perPage(50)
                .items(86)
                .urls(Map.of(
                        "last", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50",
                        "next", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50"))
                .build();
    }

    public static SubmissionArtist newSubmissionArtist(final int index) {
        return SubmissionArtist.builder()
                .id(5432L + index)
                .resourceUrl("https://someurl/5432" + index)
                .dataQuality("Data Quality Value")
                .name("Artist Name " + index)
                .nameVariations(List.of("Variation 1", "Variation 2"))
                .releasesUrl("https://someurl/5432" + index + "/releases/")
                .uri("https://someurl/5432" + index)
                .build();
    }

    public static CatalogEntity newLabel(final int index) {
        return CatalogEntity.builder()
                .id(678L + index)
                .resourceUrl("https://someurl/678" + index)
                .catalogNumber("Catalog Number " + index)
                .entityType("label")
                .entityTypeName("label")
                .build();
    }

    /////////////////////////////////
    // GetUserContributionsResponse
    /////////////////////////////////

    public static GetUserContributionsResponse newGetUserContributionsResponse() {
        return GetUserContributionsResponse.builder()
                .contributions(List.of(newRelease(0), newRelease(1), newRelease(2)))
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .page(1)
                        .pages(2)
                        .perPage(50)
                        .items(86)
                        .urls(Map.of(
                                "last", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50",
                                "next", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50"))
                        .build())
                .build();
    }

    @UtilityClass
    public static class Responses {
        private static final String FOLDER = "/useridentity/";

        public static SerializedResource AUTHENTICATED_USER =
                new SerializedResource(FOLDER + "AuthenticatedUser.json");
        public static SerializedResource USER_PROFILE =
                new SerializedResource(FOLDER + "UserProfile.json");
        public static SerializedResource GET_USER_SUBMISSIONS_RESPONSE =
                new SerializedResource(FOLDER + "GetUserSubmissionsResponse.json");
        public static SerializedResource GET_USER_CONTRIBUTIONS_RESPONSE =
                new SerializedResource(FOLDER + "GetUserContributionsResponse.json");
    }
}
