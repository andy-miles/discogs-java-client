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
import com.amilesend.discogs.model.identity.EditUserProfileRequest;
import com.amilesend.discogs.model.identity.EditUserProfileResponse;
import com.amilesend.discogs.model.identity.GetUserContributionsRequest;
import com.amilesend.discogs.model.identity.GetUserContributionsResponse;
import com.amilesend.discogs.model.identity.GetUserProfileRequest;
import com.amilesend.discogs.model.identity.GetUserProfileResponse;
import com.amilesend.discogs.model.identity.GetUserSubmissionsRequest;
import com.amilesend.discogs.model.identity.GetUserSubmissionsResponse;
import com.amilesend.discogs.model.identity.type.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.amilesend.discogs.data.UserIdentityApiDataHelper.Responses.AUTHENTICATED_USER;
import static com.amilesend.discogs.data.UserIdentityApiDataHelper.Responses.GET_USER_CONTRIBUTIONS_RESPONSE;
import static com.amilesend.discogs.data.UserIdentityApiDataHelper.Responses.GET_USER_SUBMISSIONS_RESPONSE;
import static com.amilesend.discogs.data.UserIdentityApiDataHelper.Responses.USER_PROFILE;
import static com.amilesend.discogs.data.UserIdentityApiDataHelper.newAuthenticatedUser;
import static com.amilesend.discogs.data.UserIdentityApiDataHelper.newEditUserProfileResponse;
import static com.amilesend.discogs.data.UserIdentityApiDataHelper.newGetUserContributionsResponse;
import static com.amilesend.discogs.data.UserIdentityApiDataHelper.newGetUserProfileResponse;
import static com.amilesend.discogs.data.UserIdentityApiDataHelper.newGetUserSubmissionsResponse;
import static com.amilesend.discogs.data.UserIdentityApiValidator.validateAuthenticatedUser;
import static com.amilesend.discogs.data.UserIdentityApiValidator.validateGetUserContributionsResponse;
import static com.amilesend.discogs.data.UserIdentityApiValidator.validateGetUserSubmissionsResponse;
import static com.amilesend.discogs.data.UserIdentityApiValidator.validateUserProfile;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserIdentityApiFunctionalTest extends FunctionalTestBase {
    private UserIdentityApi apiUnderTest;

    @BeforeEach
    public void setUpClient() {
        apiUnderTest = getClient().getUserIdentityApi();
    }

    /////////////////////////
    // getAuthenticatedUser
    /////////////////////////

    @Test
    public void getAuthenticatedUser_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, AUTHENTICATED_USER);

        final AuthenticatedUser actual = apiUnderTest.getAuthenticatedUser();

        final AuthenticatedUser expected = newAuthenticatedUser();
        validateAuthenticatedUser(actual, expected);
    }

    @Test
    public void getAuthenticatedUser_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getAuthenticatedUser());
    }

    @Test
    public void getAuthenticatedUser_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getAuthenticatedUser());
    }

    ///////////////////
    // getUserProfile
    ///////////////////

    @Test
    public void getUserProfile_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, USER_PROFILE);

        final GetUserProfileResponse actual = apiUnderTest.getUserProfile(
                GetUserProfileRequest.builder()
                        .username("UsernameValue")
                        .build());

        final GetUserProfileResponse expected = newGetUserProfileResponse();
        validateUserProfile(expected, actual);
    }

    @Test
    public void getUserProfile_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getUserProfile(
                GetUserProfileRequest.builder()
                        .username("UsernameValue")
                        .build()));
    }

    @Test
    public void getUserProfile_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getUserProfile(
                GetUserProfileRequest.builder()
                        .username("UsernameValue")
                        .build()));
    }

    @Test
    public void getUserProfile_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getUserProfile(null));
    }

    ////////////////////
    // editUserProfile
    ////////////////////

    @Test
    public void editUserProfile_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, USER_PROFILE);

        final EditUserProfileResponse actual = apiUnderTest.editUserProfile(
                EditUserProfileRequest.builder()
                        .username("UsernameValue")
                        .build());

        final EditUserProfileResponse expected = newEditUserProfileResponse();
        validateUserProfile(expected, actual);
    }

    @Test
    public void editUserProfile_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.editUserProfile(
                EditUserProfileRequest.builder()
                        .username("UsernameValue")
                        .build()));
    }

    @Test
    public void editUserProfile_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.editUserProfile(
                EditUserProfileRequest.builder()
                        .username("UsernameValue")
                        .build()));
    }

    @Test
    public void editUserProfile_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.editUserProfile(null));
    }

    ///////////////////////
    // getUserSubmissions
    ///////////////////////

    @Test
    public void getUserSubmissions_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_USER_SUBMISSIONS_RESPONSE);

        final GetUserSubmissionsResponse actual = apiUnderTest.getUserSubmissions(
                GetUserSubmissionsRequest.builder()
                        .username("Username")
                        .build());

        final GetUserSubmissionsResponse expected = newGetUserSubmissionsResponse();
        validateGetUserSubmissionsResponse(expected, actual);
    }

    @Test
    public void getUserSubmissions_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getUserSubmissions(
                GetUserSubmissionsRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getUserSubmissions_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getUserSubmissions(
                GetUserSubmissionsRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getUserSubmissions_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getUserSubmissions(null));
    }

    /////////////////////////
    // getUserContributions
    /////////////////////////

    @Test
    public void getUserContributions_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_USER_CONTRIBUTIONS_RESPONSE);

        final GetUserContributionsResponse actual = apiUnderTest.getUserContributions(
                GetUserContributionsRequest.builder()
                        .username("Username")
                        .build());

        final GetUserContributionsResponse expected = newGetUserContributionsResponse();
        validateGetUserContributionsResponse(expected, actual);
    }

    @Test
    public void getUserContributions_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getUserContributions(
                GetUserContributionsRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getUserContributions_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getUserContributions(
                GetUserContributionsRequest.builder()
                        .username("Username")
                        .build()));
    }

    @Test
    public void getUserContributions_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getUserContributions(null));
    }
}
