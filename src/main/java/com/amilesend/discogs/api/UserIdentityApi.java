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

import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.model.Api;
import com.amilesend.discogs.model.AuthenticationRequired;
import com.amilesend.discogs.model.identity.EditUserProfileRequest;
import com.amilesend.discogs.model.identity.EditUserProfileResponse;
import com.amilesend.discogs.model.identity.GetUserContributionsRequest;
import com.amilesend.discogs.model.identity.GetUserContributionsResponse;
import com.amilesend.discogs.model.identity.GetUserProfileRequest;
import com.amilesend.discogs.model.identity.GetUserProfileResponse;
import com.amilesend.discogs.model.identity.GetUserSubmissionsRequest;
import com.amilesend.discogs.model.identity.GetUserSubmissionsResponse;
import com.amilesend.discogs.model.identity.type.AuthenticatedUser;
import lombok.NonNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * The Discogs Identity API.
 * <br/>
 * <a href="https://www.discogs.com/developers#page:user-identity">API Documentation</a>
 *
 * @see ApiBase
 */
@Api
public class UserIdentityApi extends ApiBase {
    /** Base path for the user identity URLs. */
    private static final String API_PATH = "/users/";

    /**
     * Creates a new {@code IdentityApi} object.
     *
     * @param connection the underlying client connection
     */
    public UserIdentityApi(final DiscogsConnection connection) {
        super(connection);
    }

    /**
     * Gets the basic information about the authenticated user.
     *
     * @return the authenticated user information
     * @see AuthenticatedUser
     */
    @AuthenticationRequired
    public AuthenticatedUser getAuthenticatedUser() {
        return executeGet("/oauth/identity", AuthenticatedUser.class);
    }

    /**
     * Gets a user profile.
     *
     * @param request the request
     * @return the user profile
     * @see GetUserProfileRequest
     * @see GetUserProfileResponse
     */
    @AuthenticationRequired
    public GetUserProfileResponse getUserProfile(@NonNull final GetUserProfileRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .toString();
        return executeGet(subPath, request, GetUserProfileResponse.class);
    }

    /**
     * Edits a user profile.
     *
     * @param request the request with the attributes to edit
     * @return the updated user profile
     * @see EditUserProfileRequest
     * @see EditUserProfileResponse
     */
    @AuthenticationRequired
    public EditUserProfileResponse editUserProfile(@NonNull final EditUserProfileRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .toString();
        return executePost(subPath, request, EditUserProfileResponse.class);
    }

    /**
     * Gets the submissions made by the given user.
     *
     * @param request the request
     * @return the submissions
     * @see GetUserSubmissionsRequest
     * @see GetUserSubmissionsResponse
     */
    public GetUserSubmissionsResponse getUserSubmissions(@NonNull final GetUserSubmissionsRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append("/submissions")
                .toString();
        return executeGet(subPath, GetUserSubmissionsResponse.class);
    }

    /**
     * Gets the contributions made by the give user, sort, and sort order.
     *
     * @param request the request specifying user, sort, and sort order
     * @return the contributions
     * @see GetUserContributionsRequest
     * @see GetUserContributionsResponse
     */
    public GetUserContributionsResponse getUserContributions(@NonNull final GetUserContributionsRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append("/contributions")
                .toString();
        return executeGet(subPath, request, GetUserContributionsResponse.class);
    }
}
