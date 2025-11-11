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
package com.amilesend.discogs.model.collection;

import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.PaginatedRequestBase;
import com.amilesend.discogs.model.PathParameter;
import com.amilesend.discogs.model.QueryParameter;
import com.amilesend.discogs.model.type.SortOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import okhttp3.HttpUrl;

import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotNull;

/**
 * The request to retrieve a paginated list of collection items by folder.
 *
 * @see PaginatedRequestBase
 */
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetCollectionItemsByFolderRequest extends PaginatedRequestBase {
    /** The username (required). */
    @PathParameter
    private final String username;
    /** The folder identifier (required). */
    @PathParameter
    private final long folderId;
    /**
     * The attribute to sort on (optional).
     *
     * @see Sort
     */
    @QueryParameter
    private final Sort sort;
    /**
     * The sort order (optional).
     *
     * @see SortOrder
     */
    @QueryParameter
    private final SortOrder sortOrder;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        Validate.notBlank(username, "username must not be blank");
        Validate.isTrue(folderId >= 0L, "folderId must be >= 0");

        super.populateQueryParameters(urlBuilder);
        appendIfNotNull(urlBuilder, Sort.QUERY_PARAM_NAME, sort);
        appendIfNotNull(urlBuilder, SortOrder.QUERY_PARAM_NAME, sortOrder);
        return urlBuilder;
    }

    /** Describes the attributes to sort on. */
    @RequiredArgsConstructor
    @Getter
    public enum Sort {
        LABEL("label"),
        ARTIST("artist"),
        TITLE("title"),
        CATALOG_NUMBER("catno"),
        FORMAT("format"),
        RATING("rating"),
        ADDED("added"),
        YEAR("year");

        public static final String QUERY_PARAM_NAME = "sort";

        private final String value;

        @Override
        public String toString() {
            return value;
        }
    }
}
