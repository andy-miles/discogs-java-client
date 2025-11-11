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
import com.amilesend.discogs.model.wantlist.AddReleaseToWantListRequest;
import com.amilesend.discogs.model.wantlist.DeleteReleaseFormWantListRequest;
import com.amilesend.discogs.model.wantlist.GetWantListRequest;
import com.amilesend.discogs.model.wantlist.UpdateReleaseOnWantListRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserWantListApiRequestValidationTest extends RequestValidationTestBase {
    ///////////////////////
    // GetWantListRequest
    ///////////////////////

    @Test
    public void getWantListRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetWantListRequest.builder()
                                .username(null)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetWantListRequest.builder()
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ////////////////////////////////
    // AddReleaseToWantListRequest
    ////////////////////////////////

    @Test
    public void addReleaseToWantListRequest_withValidRequest_shouldPopulateQueryParameters() {
        AddReleaseToWantListRequest.builder()
                .username("Username")
                .releaseId(1L)
                .notes("Notes Value")
                .rating(8)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("notes", "Notes+Value"),
                () -> validateQueryParameter("rating", "8"));
    }

    @Test
    public void addReleaseToWantListRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> AddReleaseToWantListRequest.builder()
                                .username(null)
                                .releaseId(1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> AddReleaseToWantListRequest.builder()
                                .username(StringUtils.EMPTY)
                                .releaseId(1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> AddReleaseToWantListRequest.builder()
                                .username("Username")
                                .releaseId(0L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ///////////////////////////////////
    // UpdateReleaseOnWantListRequest
    ///////////////////////////////////

    @Test
    public void updateReleaseOnWantListRequest_withValidRequest_shouldPopulateQueryParameters() {
        UpdateReleaseOnWantListRequest.builder()
                .username("Username")
                .releaseId(1L)
                .notes("Notes Value")
                .rating(8)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("notes", "Notes+Value"),
                () -> validateQueryParameter("rating", "8"));
    }

    @Test
    public void updateReleaseOnWantListRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> UpdateReleaseOnWantListRequest.builder()
                                .username(null)
                                .releaseId(1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> UpdateReleaseOnWantListRequest.builder()
                                .username(StringUtils.EMPTY)
                                .releaseId(1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> UpdateReleaseOnWantListRequest.builder()
                                .username("Username")
                                .releaseId(0L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    /////////////////////////////////////
    // DeleteReleaseFormWantListRequest
    /////////////////////////////////////

    @Test
    public void deleteReleaseFormWantListRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> DeleteReleaseFormWantListRequest.builder()
                                .username(null)
                                .releaseId(1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> DeleteReleaseFormWantListRequest.builder()
                                .username(StringUtils.EMPTY)
                                .releaseId(1L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> DeleteReleaseFormWantListRequest.builder()
                                .username("Username")
                                .releaseId(0L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }
}
