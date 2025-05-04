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
package com.amilesend.discogs.model.database;

import com.amilesend.client.parse.strategy.GsonExclude;
import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.BodyBasedRequest;
import com.amilesend.discogs.model.BodyParameter;
import com.amilesend.discogs.model.PathParameter;
import lombok.Builder;
import lombok.Data;
import okhttp3.HttpUrl;

/**
 * Request to update a user's rating for a release.
 *
 * @see BodyBasedRequest
 */
@Builder
@Data
public class UpdateUserReleaseRatingRequest implements BodyBasedRequest {
    /** The release identifier (required). */
    @PathParameter
    @GsonExclude
    private final long releaseId;
    /** The username (required). */
    @PathParameter
    @GsonExclude
    private final String username;
    /** The rating (1-5)(required). */
    @BodyParameter
    private final int rating;

    @Override
    public HttpUrl.Builder populateQueryParameters(final HttpUrl.Builder urlBuilder) {
        Validate.isTrue(releaseId > 0L, "releaseId must be > 0");
        Validate.notBlank(username, "username must not be blank");
        Validate.isTrue(rating > 0 && rating < 6, "rating must be between 1-5");
        return urlBuilder;
    }
}
