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
import com.amilesend.discogs.model.PathParameter;
import com.amilesend.discogs.model.QueryParameter;
import com.amilesend.discogs.model.QueryParameterBasedRequest;
import com.amilesend.discogs.model.type.SortOrder;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;

import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotNull;

/**
 * Defines the request to fetch user contributions.
 *
 * @see QueryParameterBasedRequest
 */
@Builder
@Data
public class GetUserContributionsRequest implements QueryParameterBasedRequest {
    /** The username (required). */
    @PathParameter
    private final String username;
    /** The attribute to sort on (optional). */
    @QueryParameter
    private final Sort sort;
    /** The sort order (optional). */
    @QueryParameter
    private final SortOrder sortOrder;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        Validate.notBlank(getUsername(), "username must not be blank");

        appendIfNotNull(urlBuilder, Sort.QUERY_PARAM_NAME, getSort());
        appendIfNotNull(urlBuilder, SortOrder.QUERY_PARAM_NAME, getSortOrder());

        return urlBuilder;
    }

    /** Defines the supported attributes to sort on for requests. */
    @RequiredArgsConstructor
    @Getter
    public enum Sort {
        /** The release label .*/
        LABEL("label"),
        /** The release artist. */
        ARTIST("artist"),
        /** The release title. */
        TITLE("title"),
        /** The release catalog number. */
        CATALOG_NUMBER("catno"),
        /** The release format. */
        FORMAT("format"),
        /** The rating. */
        RATING("rating"),
        /** The year released. */
        YEAR("year"),
        /** The time when the release was added. */
        ADDED("added");

        /** The query parameter attribute name. */
        public static final String QUERY_PARAM_NAME = "sort";

        /** The query parameter attribute value. */
        private final String value;

        @Override
        public String toString() {
            return value;
        }
    }
}
