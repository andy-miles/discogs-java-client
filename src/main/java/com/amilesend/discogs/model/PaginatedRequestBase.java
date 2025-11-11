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
package com.amilesend.discogs.model;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import okhttp3.HttpUrl;

import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotNull;

/**
 * Base class to define requests that support pagination.
 *
 * @see QueryParameterBasedRequest
 */
@SuperBuilder
@Data
public abstract class PaginatedRequestBase implements QueryParameterBasedRequest {
    /** The page specifier. */
    @QueryParameter
    private final Integer page;
    /** The number of items to include per paginated response. */
    @QueryParameter
    private final Integer perPage;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        appendIfNotNull(urlBuilder, "page", page);
        appendIfNotNull(urlBuilder, "per_page", perPage);
        return urlBuilder;
    }
}
