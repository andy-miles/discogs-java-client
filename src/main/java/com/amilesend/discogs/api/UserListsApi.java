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
import com.amilesend.discogs.model.AuthenticationOptional;
import com.amilesend.discogs.model.lists.GetUserListRequest;
import com.amilesend.discogs.model.lists.GetUserListResponse;
import com.amilesend.discogs.model.lists.GetUserListsRequest;
import com.amilesend.discogs.model.lists.GetUserListsResponse;
import lombok.NonNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * The Discogs User Lists API.
 * <br/>
 * <a href="https://www.discogs.com/developers#page:user-lists">API Documentation</a>
 *
 * @see ApiBase
 */
@Api
public class UserListsApi extends ApiBase {
    /**
     * Creates a new {@code UserWantListApi} object.
     *
     * @param connection the underlying client connection
     */
    public UserListsApi(final DiscogsConnection connection) {
        super(connection);
    }

    /**
     * Gets the list of user-defined lists for a user. Note: If authenticated as the owner, then private lists
     * will also be returned.
     *
     * @param request the request
     * @return the response
     * @see GetUserListsRequest
     * @see GetUserListsResponse
     */
    @AuthenticationOptional
    public GetUserListsResponse getUserLists(@NonNull final GetUserListsRequest request) {
        final String subPath = new StringBuilder("/users/")
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append("/lists")
                .toString();
        return executeGet(subPath, request, GetUserListsResponse.class);
    }

    /**
     * Gets a user's list. Note: Must be authenticated as the owner if retrieving a private list.
     *
     * @param request the request
     * @return the response
     * @see GetUserListRequest
     * @see GetUserListResponse
     */
    @AuthenticationOptional
    public GetUserListResponse getUserList(@NonNull final GetUserListRequest request) {
        final String subPath = new StringBuilder("/lists/")
                .append(request.getListId())
                .toString();
        return executeGet(subPath, request, GetUserListResponse.class);
    }
}
