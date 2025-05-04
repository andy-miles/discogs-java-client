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
import com.amilesend.discogs.model.wantlist.AddReleaseToWantListRequest;
import com.amilesend.discogs.model.wantlist.AddReleaseToWantListResponse;
import com.amilesend.discogs.model.wantlist.DeleteReleaseFormWantListRequest;
import com.amilesend.discogs.model.wantlist.GetWantListRequest;
import com.amilesend.discogs.model.wantlist.GetWantListResponse;
import com.amilesend.discogs.model.wantlist.UpdateReleaseOnWantListRequest;
import com.amilesend.discogs.model.wantlist.UpdateReleaseOnWantListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.amilesend.discogs.data.UserWantListApiDataHelper.Responses.ADD_RELEASE_TO_WANT_LIST_RESPONSE;
import static com.amilesend.discogs.data.UserWantListApiDataHelper.Responses.GET_WANT_LIST_RESPONSE;
import static com.amilesend.discogs.data.UserWantListApiDataHelper.Responses.UPDATE_RELEASE_ON_WANT_LIST_RESPONSE;
import static com.amilesend.discogs.data.UserWantListApiDataHelper.newAddReleaseToWantListResponse;
import static com.amilesend.discogs.data.UserWantListApiDataHelper.newGetWantListResponse;
import static com.amilesend.discogs.data.UserWantListApiDataHelper.newUpdateReleaseOnWantListResponse;
import static com.amilesend.discogs.data.UserWantListApiDataValidator.validateAddReleaseToWantListResponse;
import static com.amilesend.discogs.data.UserWantListApiDataValidator.validateGetWantListResponse;
import static com.amilesend.discogs.data.UserWantListApiDataValidator.validateUpdateReleaseOnWantListResponse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserWantListApiFunctionalTest extends FunctionalTestBase {
    private UserWantListApi apiUnderTest;

    @BeforeEach
    public void setUpClient() {
        apiUnderTest = getClient().getUserWantListApi();
    }

    ////////////////
    // getWantList
    ////////////////

    @Test
    public void getWantList_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_WANT_LIST_RESPONSE);

        final GetWantListResponse actual = apiUnderTest.getWantList(
                GetWantListRequest.builder()
                        .username("Username")
                        .build());

        final GetWantListResponse expected = newGetWantListResponse();
        validateGetWantListResponse(expected, actual);
    }

    @Test
    public void getWantList_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getWantList(
                GetWantListRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getWantList_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getWantList(
                GetWantListRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getWantList_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getWantList(null));
    }

    /////////////////////////
    // addReleaseToWantList
    /////////////////////////

    @Test
    public void addReleaseToWantList_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, ADD_RELEASE_TO_WANT_LIST_RESPONSE);

        final AddReleaseToWantListResponse actual = apiUnderTest.addReleaseToWantList(
                AddReleaseToWantListRequest.builder()
                        .username("Username")
                        .releaseId(400)
                        .build());

        final AddReleaseToWantListResponse expected = newAddReleaseToWantListResponse();
        validateAddReleaseToWantListResponse(expected, actual);
    }

    @Test
    public void addReleaseToWantList_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.addReleaseToWantList(
                AddReleaseToWantListRequest.builder()
                        .username("Username")
                        .releaseId(400)
                        .build()));
    }

    @Test
    public void addReleaseToWantList_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.addReleaseToWantList(
                AddReleaseToWantListRequest.builder()
                        .username("Username")
                        .releaseId(400L)
                        .build()));
    }

    @Test
    public void addReleaseToWantList_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.addReleaseToWantList(null));
    }

    ////////////////////////////
    // updateReleaseOnWantList
    ////////////////////////////

    @Test
    public void updateReleaseOnWantList_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, UPDATE_RELEASE_ON_WANT_LIST_RESPONSE);

        final UpdateReleaseOnWantListResponse actual = apiUnderTest.updateReleaseOnWantList(
                UpdateReleaseOnWantListRequest.builder()
                        .username("Username")
                        .releaseId(403L)
                        .build());

        final UpdateReleaseOnWantListResponse expected = newUpdateReleaseOnWantListResponse();
        validateUpdateReleaseOnWantListResponse(expected, actual);
    }

    @Test
    public void updateReleaseOnWantList_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.updateReleaseOnWantList(
                UpdateReleaseOnWantListRequest.builder()
                        .username("Username")
                        .releaseId(403L)
                        .build()));
    }

    @Test
    public void updateReleaseOnWantList_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.updateReleaseOnWantList(
                UpdateReleaseOnWantListRequest.builder()
                        .username("Username")
                        .releaseId(403L)
                        .build()));
    }

    @Test
    public void updateReleaseOnWantList_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.updateReleaseOnWantList(null));
    }

    //////////////////////////////
    // deleteReleaseFromWantList
    //////////////////////////////

    @Test
    public void deleteReleaseFromWantList_withValidRequest_shouldSucceed() {
        setUpMockResponse(SUCCESS_STATUS_CODE);

        apiUnderTest.deleteReleaseFromWantList(
                DeleteReleaseFormWantListRequest.builder()
                        .username("Username")
                        .releaseId(400L)
                        .build());
    }

    @Test
    public void deleteReleaseFromWantList_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.deleteReleaseFromWantList(
                DeleteReleaseFormWantListRequest.builder()
                        .username("Username")
                        .releaseId(400L)
                        .build()));
    }

    @Test
    public void deleteReleaseFromWantList_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.deleteReleaseFromWantList(
                DeleteReleaseFormWantListRequest.builder()
                        .username("Username")
                        .releaseId(400L)
                        .build()));
    }

    @Test
    public void deleteReleaseFromWantList_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.deleteReleaseFromWantList(null));
    }
}
