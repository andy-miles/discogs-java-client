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

import com.amilesend.client.util.StringUtils;
import com.amilesend.discogs.RequestValidationTestBase;
import com.amilesend.discogs.model.database.DeleteUserReleaseRequest;
import com.amilesend.discogs.model.database.GetArtistInformationRequest;
import com.amilesend.discogs.model.database.GetArtistReleasesRequest;
import com.amilesend.discogs.model.database.GetCommunityReleaseRatingRequest;
import com.amilesend.discogs.model.database.GetLabelInformationRequest;
import com.amilesend.discogs.model.database.GetLabelReleasesRequest;
import com.amilesend.discogs.model.database.GetMasterReleaseRequest;
import com.amilesend.discogs.model.database.GetMasterReleaseVersionsRequest;
import com.amilesend.discogs.model.database.GetReleaseRequest;
import com.amilesend.discogs.model.database.GetUserReleaseRatingRequest;
import com.amilesend.discogs.model.database.SearchRequest;
import com.amilesend.discogs.model.database.UpdateUserReleaseRatingRequest;
import com.amilesend.discogs.model.database.type.SearchType;
import com.amilesend.discogs.model.type.Currency;
import com.amilesend.discogs.model.type.SortOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class DatabaseApiRequestValidationTest extends RequestValidationTestBase {

    //////////////////////
    // GetReleaseRequest
    //////////////////////

    @Test
    public void getReleaseRequest_withValidRequest_shouldPopulateQueryParameters() {
        GetReleaseRequest.builder()
                .releaseId(1234L)
                .currAbbr(Currency.USD)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        verify(mockHttpUrlBuilder).addQueryParameter(eq("curr_abbr"), eq("USD"));
    }

    @Test
    public void getReleaseRequest_withUndefinedReleaseId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetReleaseRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    ////////////////////////////////
    // GetUserReleaseRatingRequest
    ////////////////////////////////

    @Test
    public void getUserReleaseRatingRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetUserReleaseRatingRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(NullPointerException.class,
                        () -> GetUserReleaseRatingRequest.builder()
                                .releaseId(1234L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetUserReleaseRatingRequest.builder()
                                .releaseId(1234L)
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ///////////////////////////////////
    // UpdateUserReleaseRatingRequest
    ///////////////////////////////////

    @Test
    public void updateUserReleaseRatingRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> UpdateUserReleaseRatingRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(NullPointerException.class,
                        () -> UpdateUserReleaseRatingRequest.builder()
                                .releaseId(1234L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> UpdateUserReleaseRatingRequest.builder()
                                .releaseId(1234L)
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> UpdateUserReleaseRatingRequest.builder()
                                .releaseId(1234L)
                                .username("Username")
                                .rating(0)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> UpdateUserReleaseRatingRequest.builder()
                                .releaseId(1234L)
                                .username("Username")
                                .rating(6)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    /////////////////////////////
    // DeleteUserReleaseRequest
    /////////////////////////////

    @Test
    public void deleteUserReleaseRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> DeleteUserReleaseRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(NullPointerException.class,
                        () -> DeleteUserReleaseRequest.builder()
                                .releaseId(1234L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> DeleteUserReleaseRequest.builder()
                                .releaseId(1234L)
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    /////////////////////////////////////
    // GetCommunityReleaseRatingRequest
    /////////////////////////////////////

    @Test
    public void getCommunityReleaseRatingRequest_withInvalidReleaseId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetCommunityReleaseRatingRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    ////////////////////////////
    // GetMasterReleaseRequest
    ////////////////////////////

    @Test
    public void getMasterReleaseRequest_withInvalidMasterId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetMasterReleaseRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    ////////////////////////////////////
    // GetMasterReleaseVersionsRequest
    ////////////////////////////////////

    @Test
    public void getMasterReleaseVersionsRequest_withValidRequest_shouldPopulateQueryParameters() {
        GetMasterReleaseVersionsRequest.builder()
                .masterId(1234L)
                .format("Format")
                .label("Label")
                .released(2020)
                .country("US")
                .sort(GetMasterReleaseVersionsRequest.Sort.RELEASED)
                .sortOrder(SortOrder.ASC)
                .page(1)
                .perPage(5)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("format", "Format"),
                () -> validateQueryParameter("label", "Label"),
                () -> validateQueryParameter("released", "2020"),
                () -> validateQueryParameter("country", "US"),
                () -> validateQueryParameter("sort", "released"),
                () -> validateQueryParameter("sort_order", "asc"),
                () -> validateQueryParameter("page", "1"),
                () -> validateQueryParameter("per_page", "5"));
    }

    @Test
    public void getMasterReleaseVersionsRequest_withInvalidMasterId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetMasterReleaseVersionsRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    ////////////////////////////////
    // GetArtistInformationRequest
    ////////////////////////////////

    @Test
    public void getArtistInformationRequest_withInvalidArtistId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetArtistInformationRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    /////////////////////////////
    // GetArtistReleasesRequest
    /////////////////////////////

    @Test
    public void getArtistReleasesRequest_withValidRequest_shouldPopulateQueryParameters() {
        GetArtistReleasesRequest.builder()
                .artistId(1234L)
                .sort(GetArtistReleasesRequest.Sort.TITLE)
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
    public void getArtistReleasesRequest_withInvalidArtistId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetArtistReleasesRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    ///////////////////////////////
    // GetLabelInformationRequest
    ///////////////////////////////

    @Test
    public void getLabelInformationRequest_withInvalidLabelId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetLabelInformationRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    ////////////////////////////
    // GetLabelReleasesRequest
    ////////////////////////////

    @Test
    public void getLabelReleasesRequest_withValidRequest_shouldPopulateQueryParmaeters() {
        GetLabelReleasesRequest.builder()
                .labelId(1234L)
                .page(1)
                .perPage(10)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("page", "1"),
                () -> validateQueryParameter("per_page", "10"));
    }

    @Test
    public void getLabelReleasesRequest_withInvalidLabelId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetLabelReleasesRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    //////////////////
    // SearchRequest
    //////////////////

    @Test
    public void searchRequest_withValidRequest_shouldPopulateQueryParameters() {
        SearchRequest.builder()
                .query("Query")
                .type(SearchType.RELEASE)
                .title("Title")
                .releaseTitle("Release Title")
                .credit("Credit")
                .artist("Artist")
                .anv("Anv")
                .label("Label")
                .genre("Genre")
                .style("Style")
                .country("US")
                .year(2022)
                .format("Format")
                .catalogNumber("101")
                .barcode("Barcode")
                .track("1")
                .submitter("Submitted")
                .contributor("Contributor")
                .page(1)
                .perPage(5)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("q", "Query"),
                () -> validateQueryParameter("type", "release"),
                () -> validateQueryParameter("title", "Title"),
                () -> validateQueryParameter("release_title", "Release+Title"),
                () -> validateQueryParameter("credit", "Credit"),
                () -> validateQueryParameter("artist", "Artist"),
                () -> validateQueryParameter("anv", "Anv"),
                () -> validateQueryParameter("label", "Label"),
                () -> validateQueryParameter("genre", "Genre"),
                () -> validateQueryParameter("style", "Style"),
                () -> validateQueryParameter("country", "US"),
                () -> validateQueryParameter("year", "2022"),
                () -> validateQueryParameter("format", "Format"),
                () -> validateQueryParameter("catno", "101"),
                () -> validateQueryParameter("barcode", "Barcode"),
                () -> validateQueryParameter("track", "1"),
                () -> validateQueryParameter("submitter", "Submitted"),
                () -> validateQueryParameter("contributor", "Contributor"),
                () -> validateQueryParameter("page", "1"),
                () -> validateQueryParameter("per_page", "5"));
    }
}
