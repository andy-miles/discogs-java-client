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

import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.model.Api;
import com.amilesend.discogs.model.AuthenticationRequired;
import com.amilesend.discogs.model.wantlist.AddReleaseToWantListRequest;
import com.amilesend.discogs.model.wantlist.AddReleaseToWantListResponse;
import com.amilesend.discogs.model.wantlist.DeleteReleaseFormWantListRequest;
import com.amilesend.discogs.model.wantlist.GetWantListRequest;
import com.amilesend.discogs.model.wantlist.GetWantListResponse;
import com.amilesend.discogs.model.wantlist.UpdateReleaseOnWantListRequest;
import com.amilesend.discogs.model.wantlist.UpdateReleaseOnWantListResponse;
import lombok.NonNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * The Discogs User Wantlist API.
 * <br/>
 * <a href="https://www.discogs.com/developers#page:user-wantlist">API Documentation</a>
 *
 * @see ApiBase
 */
@Api
public class UserWantListApi extends ApiBase {
    /** Base path for the user identity URLs. */
    private static final String API_PATH = "/users/";
    private static final String WANTS_SUB_PATH = "/wants";

    /**
     * Creates a new {@code UserWantListApi} object.
     *
     * @param connection the underlying client connection
     */
    public UserWantListApi(final DiscogsConnection connection) {
        super(connection);
    }

    /**
     * Gets the want list for a user. Note: Must be authenticated. If authenticated as the owner, then the notes field
     * will be visible.
     *
     * @param request the request
     * @return the response
     * @see GetWantListRequest
     * @see GetWantListResponse
     */
    @AuthenticationRequired
    public GetWantListResponse getWantList(@NonNull final GetWantListRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(WANTS_SUB_PATH)
                .toString();
        return executeGet(subPath, request, GetWantListResponse.class);
    }

    /**
     * Adds a release to a user's want list. Note: must be authenticated as the list owner.
     *
     * @param request the request
     * @return the response
     * @see AddReleaseToWantListRequest
     * @see AddReleaseToWantListResponse
     */
    @AuthenticationRequired
    public AddReleaseToWantListResponse addReleaseToWantList(@NonNull final AddReleaseToWantListRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(WANTS_SUB_PATH)
                .append("/")
                .append(request.getReleaseId())
                .toString();
        return executePut(subPath, request, AddReleaseToWantListResponse.class);
    }

    /**
     * Updates an existing release from a user's want list. Note: must be authenticated as the list owner.
     *
     * @param request the request
     * @return the response
     * @see UpdateReleaseOnWantListRequest
     * @see UpdateReleaseOnWantListResponse
     */
    @AuthenticationRequired
    public UpdateReleaseOnWantListResponse updateReleaseOnWantList(
            @NonNull final UpdateReleaseOnWantListRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(WANTS_SUB_PATH)
                .append("/")
                .append(request.getReleaseId())
                .toString();
        return executePost(subPath, request, UpdateReleaseOnWantListResponse.class);
    }

    /**
     * Deletes a release form a user's want list. Note: must be authenticated as the list owner.
     *
     * @param request the request
     * @see DeleteReleaseFormWantListRequest
     */
    @AuthenticationRequired
    public void deleteReleaseFromWantList(@NonNull final DeleteReleaseFormWantListRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(WANTS_SUB_PATH)
                .append("/")
                .append(request.getReleaseId())
                .toString();
        executeDelete(subPath, request);
    }
}
