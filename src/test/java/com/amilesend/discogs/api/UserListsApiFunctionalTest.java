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
import com.amilesend.discogs.model.lists.GetUserListRequest;
import com.amilesend.discogs.model.lists.GetUserListResponse;
import com.amilesend.discogs.model.lists.GetUserListsRequest;
import com.amilesend.discogs.model.lists.GetUserListsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.amilesend.discogs.data.UserListsApiDataHelper.Responses.GET_USER_LISTS_RESPONSE;
import static com.amilesend.discogs.data.UserListsApiDataHelper.Responses.GET_USER_LIST_RESPONSE;
import static com.amilesend.discogs.data.UserListsApiDataHelper.newGetUserListResponse;
import static com.amilesend.discogs.data.UserListsApiDataHelper.newGetUserListsResponse;
import static com.amilesend.discogs.data.UserListsApiDataValidator.validateGetUserListResponse;
import static com.amilesend.discogs.data.UserListsApiDataValidator.validateGetUserListsResponse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserListsApiFunctionalTest extends FunctionalTestBase {
    private UserListsApi apiUnderTest;

    @BeforeEach
    public void setUpClient() {
        apiUnderTest = getClient().getUserListsApi();
    }

    /////////////////
    // getUserLists
    /////////////////

    @Test
    public void getUserLists_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_USER_LISTS_RESPONSE);

        final GetUserListsResponse actual = apiUnderTest.getUserLists(
                GetUserListsRequest.builder()
                        .username("Username")
                        .build());

        final GetUserListsResponse expected = newGetUserListsResponse();
        validateGetUserListsResponse(expected, actual);
    }

    @Test
    public void getUserLists_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getUserLists(
                GetUserListsRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getUserLists_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getUserLists(
                GetUserListsRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getUserLists_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getUserLists(null));
    }

    ////////////////
    // getUserList
    ////////////////

    @Test
    public void getUserList_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_USER_LIST_RESPONSE);

        final GetUserListResponse actual = apiUnderTest.getUserList(
                GetUserListRequest.builder()
                        .listId(14L)
                        .build());

        final GetUserListResponse expected = newGetUserListResponse();
        validateGetUserListResponse(expected, actual);
    }

    @Test
    public void getUserList_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getUserList(
                GetUserListRequest.builder()
                        .listId(14L)
                        .build()));
    }

    @Test
    public void getUserList_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getUserList(
                GetUserListRequest.builder()
                        .listId(14L)
                        .build()));
    }

    @Test
    public void getUserList_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getUserList(null));
    }
}
