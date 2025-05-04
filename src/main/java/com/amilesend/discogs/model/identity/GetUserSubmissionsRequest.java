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

import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.PaginatedRequestBase;
import com.amilesend.discogs.model.PathParameter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import okhttp3.HttpUrl;

/**
 * The request to retrieve the paginated list of submission for a user.
 *
 * @see PaginatedRequestBase
 */
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetUserSubmissionsRequest extends PaginatedRequestBase {
    /** The username (required). */
    @PathParameter
    private final String username;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        Validate.notBlank(getUsername(), "username must not be blank");
        return super.populateQueryParameters(urlBuilder);
    }
}
