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
import com.amilesend.discogs.model.database.DeleteUserReleaseRequest;
import com.amilesend.discogs.model.database.GetArtistInformationRequest;
import com.amilesend.discogs.model.database.GetArtistInformationResponse;
import com.amilesend.discogs.model.database.GetArtistReleasesRequest;
import com.amilesend.discogs.model.database.GetArtistReleasesResponse;
import com.amilesend.discogs.model.database.GetCommunityReleaseRatingRequest;
import com.amilesend.discogs.model.database.GetCommunityReleaseRatingResponse;
import com.amilesend.discogs.model.database.GetLabelInformationRequest;
import com.amilesend.discogs.model.database.GetLabelInformationResponse;
import com.amilesend.discogs.model.database.GetLabelReleasesRequest;
import com.amilesend.discogs.model.database.GetLabelReleasesResponse;
import com.amilesend.discogs.model.database.GetMasterReleaseRequest;
import com.amilesend.discogs.model.database.GetMasterReleaseResponse;
import com.amilesend.discogs.model.database.GetMasterReleaseVersionsRequest;
import com.amilesend.discogs.model.database.GetMasterReleaseVersionsResponse;
import com.amilesend.discogs.model.database.GetReleaseRequest;
import com.amilesend.discogs.model.database.GetReleaseResponse;
import com.amilesend.discogs.model.database.GetUserReleaseRatingRequest;
import com.amilesend.discogs.model.database.GetUserReleaseRatingResponse;
import com.amilesend.discogs.model.database.SearchRequest;
import com.amilesend.discogs.model.database.SearchResponse;
import com.amilesend.discogs.model.database.UpdateUserReleaseRatingRequest;
import com.amilesend.discogs.model.database.UpdateUserReleaseRatingResponse;
import com.amilesend.discogs.model.database.type.UserReleaseRating;
import com.amilesend.discogs.model.type.Release;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.amilesend.discogs.data.DatabaseApiDataHelper.Responses.ARTIST_INFORMATION;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.Responses.ARTIST_RELEASES;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.Responses.COMMUNITY_RELEASE_RATING;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.Responses.LABEL_INFORMATION;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.Responses.LABEL_RELEASES;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.Responses.MASTER_RELEASE;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.Responses.MASTER_RELEASE_VERSIONS;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.Responses.RELEASE;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.Responses.SEARCH_RESPONSE;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.Responses.USER_RELEASE_RATING;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newGetArtistInformationResponse;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newGetArtistReleasesResponse;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newGetCommunityReleaseRatingResponse;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newGetLabelInformationResponse;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newGetLabelReleasesResponse;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newGetMasterReleaseResponse;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newGetMasterReleaseVersionsResponse;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newGetUserReleaseRatingResponse;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newRelease;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newSearchResponse;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newUpdateUserReleaseRatingResponse;
import static com.amilesend.discogs.data.DatabaseApiDataValidator.validateArtistInformation;
import static com.amilesend.discogs.data.DatabaseApiDataValidator.validateGetArtistReleasesResponse;
import static com.amilesend.discogs.data.DatabaseApiDataValidator.validateGetLabelInformationResponse;
import static com.amilesend.discogs.data.DatabaseApiDataValidator.validateGetLabelReleasesResponse;
import static com.amilesend.discogs.data.DatabaseApiDataValidator.validateGetMasterReleaseResponse;
import static com.amilesend.discogs.data.DatabaseApiDataValidator.validateGetMasterReleaseVersionsResponse;
import static com.amilesend.discogs.data.DatabaseApiDataValidator.validateRelease;
import static com.amilesend.discogs.data.DatabaseApiDataValidator.validateSearchResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseApiFunctionalTest extends FunctionalTestBase {
    private DatabaseApi apiUnderTest;

    @BeforeEach
    public void setUpClient() {
        apiUnderTest = getClient().getDatabaseApi();
    }

    ///////////////
    // getRelease
    ///////////////

    @Test
    public void getRelease_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, RELEASE);

        final GetReleaseResponse actual = apiUnderTest.getRelease(
                GetReleaseRequest.builder()
                        .releaseId(1827596L)
                        .build());

        final Release expected = newRelease();
        validateRelease(expected, actual);
    }

    @Test
    public void getRelease_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getRelease(
                GetReleaseRequest.builder()
                        .releaseId(1827596L)
                        .build()));
    }

    @Test
    public void getRelease_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getRelease(
                GetReleaseRequest.builder()
                        .releaseId(1827596L)
                        .build()));
    }

    @Test
    public void getRelease_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getRelease(null));
    }

    /////////////////////////
    // getUserReleaseRating
    /////////////////////////

    @Test
    public void getUserReleaseRating_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, USER_RELEASE_RATING);

        final GetUserReleaseRatingResponse actual = apiUnderTest.getUserReleaseRating(
                GetUserReleaseRatingRequest.builder()
                        .releaseId(12345678L)
                        .username("User")
                        .build());

        final GetUserReleaseRatingResponse expected = newGetUserReleaseRatingResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void getUserReleaseRating_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getUserReleaseRating(
                GetUserReleaseRatingRequest.builder()
                        .releaseId(12345678L)
                        .username("User")
                        .build()));
    }

    @Test
    public void getUserReleaseRating_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getUserReleaseRating(
                GetUserReleaseRatingRequest.builder()
                        .releaseId(12345678L)
                        .username("User")
                        .build()));
    }

    @Test
    public void getUserReleaseRating_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getUserReleaseRating(null));
    }

    ////////////////////////////
    // updateUserReleaseRating
    ////////////////////////////

    @Test
    public void updateUserReleaseRating_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, USER_RELEASE_RATING);

        final UpdateUserReleaseRatingResponse actual = apiUnderTest.updateUserReleaseRating(
                UpdateUserReleaseRatingRequest.builder()
                        .releaseId(12345678L)
                        .username("User")
                        .rating(3)
                        .build());

        final UserReleaseRating expected = newUpdateUserReleaseRatingResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void updateUserReleaseRating_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.updateUserReleaseRating(
                UpdateUserReleaseRatingRequest.builder()
                        .releaseId(12345678L)
                        .username("User")
                        .rating(3)
                        .build()));
    }

    @Test
    public void updateUserReleaseRating_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.updateUserReleaseRating(
                UpdateUserReleaseRatingRequest.builder()
                        .releaseId(12345678L)
                        .username("User")
                        .rating(3)
                        .build()));
    }

    @Test
    public void updateUserReleaseRating_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.updateUserReleaseRating(null));
    }

    ////////////////////////////
    // deleteUserReleaseRating
    ////////////////////////////

    @Test
    public void deleteUserReleaseRating_withValidRequest_shouldDoNothing() {
        setUpMockResponse(SUCCESS_STATUS_CODE);

        apiUnderTest.deleteUserReleaseRating(DeleteUserReleaseRequest.builder()
                .username("User")
                .releaseId(12345678L)
                .build());
    }

    @Test
    public void deleteUserReleaseRating_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.deleteUserReleaseRating(
                DeleteUserReleaseRequest.builder()
                        .username("User")
                        .releaseId(12345678L)
                        .build()));
    }

    @Test
    public void deleteUserReleaseRating_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.deleteUserReleaseRating(
                DeleteUserReleaseRequest.builder()
                        .username("User")
                        .releaseId(12345678L)
                        .build()));
    }

    @Test
    public void deleteUserReleaseRating_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.deleteUserReleaseRating(null));
    }

    //////////////////////////////
    // getCommunityReleaseRating
    //////////////////////////////

    @Test
    public void getCommunityReleaseRating_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, COMMUNITY_RELEASE_RATING);

        final GetCommunityReleaseRatingResponse actual = apiUnderTest.getCommunityReleaseRating(
                GetCommunityReleaseRatingRequest.builder()
                        .releaseId(12345678L)
                        .build());

        final GetCommunityReleaseRatingResponse expected = newGetCommunityReleaseRatingResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void getCommunityReleaseRating_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getCommunityReleaseRating(
                GetCommunityReleaseRatingRequest.builder()
                        .releaseId(12345678L)
                        .build()));
    }

    @Test
    public void getCommunityReleaseRating_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getCommunityReleaseRating(
                GetCommunityReleaseRatingRequest.builder()
                        .releaseId(12345678L)
                        .build()));
    }

    @Test
    public void getCommunityReleaseRating_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getCommunityReleaseRating(null));
    }

    /////////////////////
    // getMasterRelease
    /////////////////////

    @Test
    public void getMasterRelease_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, MASTER_RELEASE);

        final GetMasterReleaseResponse actual = apiUnderTest.getMasterRelease(
                GetMasterReleaseRequest.builder()
                        .masterId(52086)
                        .build());

        final GetMasterReleaseResponse expected = newGetMasterReleaseResponse();
        validateGetMasterReleaseResponse(expected, actual);
    }

    @Test
    public void getMasterRelease_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getMasterRelease(
                GetMasterReleaseRequest.builder()
                        .masterId(52086)
                        .build()));
    }

    @Test
    public void getMasterRelease_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getMasterRelease(
                GetMasterReleaseRequest.builder()
                        .masterId(52086)
                        .build()));
    }

    @Test
    public void getMasterRelease_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getMasterRelease(null));
    }

    /////////////////////////////
    // getMasterReleaseVersions
    /////////////////////////////

    @Test
    public void getMasterReleaseVersions_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, MASTER_RELEASE_VERSIONS);

        final GetMasterReleaseVersionsResponse actual = apiUnderTest.getMasterReleaseVersions(
                GetMasterReleaseVersionsRequest.builder()
                        .masterId(8924382)
                        .format("Cassette")
                        .country("US")
                        .build());

        final GetMasterReleaseVersionsResponse expected = newGetMasterReleaseVersionsResponse(getConnection());
        validateGetMasterReleaseVersionsResponse(expected, actual);
    }

    @Test
    public void getMasterReleaseVersions_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getMasterReleaseVersions(
                GetMasterReleaseVersionsRequest.builder()
                        .masterId(8924382)
                        .format("Cassette")
                        .country("US")
                        .build()));
    }

    @Test
    public void getMasterReleaseVersions_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getMasterReleaseVersions(
                GetMasterReleaseVersionsRequest.builder()
                        .masterId(8924382)
                        .format("Cassette")
                        .country("US")
                        .build()));
    }

    @Test
    public void getMasterReleaseVersions_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getMasterReleaseVersions(null));
    }

    /////////////////////////
    // getArtistInformation
    /////////////////////////

    @Test
    public void getArtistInformation_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, ARTIST_INFORMATION);

        final GetArtistInformationResponse actual = apiUnderTest.getArtistInformation(
                GetArtistInformationRequest.builder()
                        .artistId(260935)
                        .build());

        final GetArtistInformationResponse expected = newGetArtistInformationResponse();
        validateArtistInformation(expected, actual);
    }

    @Test
    public void getArtistInformation_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getArtistInformation(
                GetArtistInformationRequest.builder()
                        .artistId(260935)
                        .build()));
    }

    @Test
    public void getArtistInformation_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getArtistInformation(
                GetArtistInformationRequest.builder()
                        .artistId(260935)
                        .build()));
    }

    @Test
    public void getArtistInformation_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getArtistInformation(null));
    }

    //////////////////////
    // getArtistReleases
    //////////////////////

    @Test
    public void getArtistReleases_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, ARTIST_RELEASES);

        final GetArtistReleasesResponse actual = apiUnderTest.getArtistReleases(
                GetArtistReleasesRequest.builder()
                        .artistId(260935)
                        .build());

        final GetArtistReleasesResponse expected = newGetArtistReleasesResponse();
        validateGetArtistReleasesResponse( expected, actual);
    }

    @Test
    public void getArtistReleases_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getArtistReleases(
                GetArtistReleasesRequest.builder()
                        .artistId(260935)
                        .build()));
    }

    @Test
    public void getArtistReleases_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getArtistReleases(
                GetArtistReleasesRequest.builder()
                        .artistId(260935)
                        .build()));
    }

    @Test
    public void getArtistReleases_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getArtistReleases(null));
    }

    ////////////////////////
    // getLabelInformation
    ////////////////////////

    @Test
    public void getLabelInformation_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, LABEL_INFORMATION);

        final GetLabelInformationResponse actual = apiUnderTest.getLabelInformation(
                GetLabelInformationRequest.builder()
                        .labelId(3456)
                        .build());

        final GetLabelInformationResponse expected = newGetLabelInformationResponse();
        validateGetLabelInformationResponse(expected, actual);
    }

    @Test
    public void getLabelInformation_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getLabelInformation(
                GetLabelInformationRequest.builder()
                        .labelId(3456)
                        .build()));
    }

    @Test
    public void getLabelInformation_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getLabelInformation(
                GetLabelInformationRequest.builder()
                        .labelId(3456)
                        .build()));
    }

    @Test
    public void getLabelInformation_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getLabelInformation(null));
    }

    /////////////////////
    // getLabelReleases
    /////////////////////

    @Test
    public void getLabelReleases_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, LABEL_RELEASES);

        final GetLabelReleasesResponse actual = apiUnderTest.getLabelReleases(
                GetLabelReleasesRequest.builder()
                        .labelId(3456)
                        .build());

        final GetLabelReleasesResponse expected = newGetLabelReleasesResponse();
        validateGetLabelReleasesResponse(expected, actual);
    }

    @Test
    public void getLabelReleases_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getLabelReleases(
                GetLabelReleasesRequest.builder()
                        .labelId(3456)
                        .build()));
    }

    @Test
    public void getLabelReleases_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getLabelReleases(
                GetLabelReleasesRequest.builder()
                        .labelId(3456)
                        .build()));
    }

    @Test
    public void getLabelReleases_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getLabelReleases(null));
    }

    ///////////
    // search
    ///////////

    @Test
    public void search_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, SEARCH_RESPONSE);

        final SearchResponse actual = apiUnderTest.search(
                SearchRequest.builder()
                        .artist("Babymetal")
                        .build());

        final SearchResponse expected = newSearchResponse();
        validateSearchResponse(expected, actual);
    }

    @Test
    public void search_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.search(
                SearchRequest.builder()
                        .artist("Babymetal")
                        .build()));
    }

    @Test
    public void search_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.search(
                SearchRequest.builder()
                        .artist("Babymetal")
                        .build()));
    }

    @Test
    public void search_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.search(null));
    }
}
