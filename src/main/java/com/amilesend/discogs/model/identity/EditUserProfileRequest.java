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
package com.amilesend.discogs.model.identity;

import com.amilesend.client.parse.strategy.GsonExclude;
import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.BodyBasedRequest;
import com.amilesend.discogs.model.BodyParameter;
import com.amilesend.discogs.model.PathParameter;
import com.amilesend.discogs.model.type.Currency;
import lombok.Builder;
import lombok.Data;
import okhttp3.HttpUrl;

/**
 * The request to update a user profile.
 *
 * @see BodyBasedRequest
 */
@Builder
@Data
public class EditUserProfileRequest implements BodyBasedRequest {
    /** The username (required). */
    @PathParameter
    @GsonExclude
    private final String username;
    /** The user's name (optional). */
    @BodyParameter
    private final String name;
    /** URL to the user's home page (optional). */
    @BodyParameter
    private final String homePage;
    /** Location description (optional). */
    @BodyParameter
    private final String location;
    /** The user's profile summary (optional). */
    @BodyParameter
    private final String profile;
    /** The user's preferred currency (optional). */
    @BodyParameter
    private final Currency currAbbr;

    @Override
    public HttpUrl.Builder populateQueryParameters(final HttpUrl.Builder urlBuilder) {
        Validate.notBlank(username, "username must not be blank");
        return urlBuilder;
    }
}
