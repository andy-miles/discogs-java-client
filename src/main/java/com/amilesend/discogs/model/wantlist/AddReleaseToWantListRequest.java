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
package com.amilesend.discogs.model.wantlist;

import com.amilesend.client.parse.strategy.GsonExclude;
import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.BodyBasedRequest;
import com.amilesend.discogs.model.PathParameter;
import com.amilesend.discogs.model.QueryParameter;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import okhttp3.HttpUrl;

import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotBlank;
import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotNull;

/**
 * The request to add a release to a user's want list.
 *
 * @see BodyBasedRequest
 */
@SuperBuilder
@Data
public class AddReleaseToWantListRequest implements BodyBasedRequest {
    /** The username (required). */
    @PathParameter
    @GsonExclude
    private final String username;
    /** The release identifier (required). */
    @PathParameter
    @GsonExclude
    private final long releaseId;
    /** The user notes associated with the release (optional). */
    @QueryParameter
    @GsonExclude
    private final String notes;
    /** The rating (1-5) for the release (optional). */
    @QueryParameter
    @GsonExclude
    private final Integer rating;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        Validate.notBlank(username, "username must not be blank");
        Validate.isTrue(releaseId > 0L, "releaseId must be > 0");

        appendIfNotBlank(urlBuilder, "notes", notes);
        appendIfNotNull(urlBuilder, "rating", rating);
        return urlBuilder;
    }
}
