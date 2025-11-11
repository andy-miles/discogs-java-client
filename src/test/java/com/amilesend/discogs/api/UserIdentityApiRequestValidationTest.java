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
import com.amilesend.discogs.model.identity.EditUserProfileRequest;
import com.amilesend.discogs.model.identity.GetUserContributionsRequest;
import com.amilesend.discogs.model.identity.GetUserProfileRequest;
import com.amilesend.discogs.model.identity.GetUserSubmissionsRequest;
import com.amilesend.discogs.model.type.SortOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserIdentityApiRequestValidationTest extends RequestValidationTestBase {
    //////////////////////////
    // GetUserProfileRequest
    //////////////////////////

    @Test
    public void getUserProfileRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetUserProfileRequest.builder()
                                .username(null)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetUserProfileRequest.builder()
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ///////////////////////////
    // EditUserProfileRequest
    ///////////////////////////

    @Test
    public void editUserProfileRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> EditUserProfileRequest.builder()
                                .username(null)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> EditUserProfileRequest.builder()
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    //////////////////////////////
    // GetUserSubmissionsRequest
    //////////////////////////////

    @Test
    public void getUserSubmissionsRequest_withValidRequest_shouldPopulateQueryParameters() {
        GetUserSubmissionsRequest.builder()
                .username("username")
                .page(1)
                .perPage(5)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("page", "1"),
                () -> validateQueryParameter("per_page", "5"));
    }

    @Test
    public void getUserSubmissionsRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetUserSubmissionsRequest.builder()
                                .username(null)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetUserSubmissionsRequest.builder()
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ////////////////////////////////
    // GetUserContributionsRequest
    ////////////////////////////////

    @Test
    public void getUserContributionsRequest_withValidRequest_shouldPopulateQueryParameters() {
        GetUserContributionsRequest.builder()
                .username("username")
                .sort(GetUserContributionsRequest.Sort.TITLE)
                .sortOrder(SortOrder.ASC)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("sort", "title"),
                () -> validateQueryParameter("sort_order", "asc"));
    }

    @Test
    public void getUserContributionsRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetUserContributionsRequest.builder()
                                .username(null)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetUserContributionsRequest.builder()
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }
}
