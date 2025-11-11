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
package com.amilesend.discogs.model.database;

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

import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotBlank;
import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotNull;

/**
 * The request to retrieve the paginated list of master release versions.
 *
 * @see PaginatedRequestBase
 */
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetMasterReleaseVersionsRequest extends PaginatedRequestBase {
    /** The master identifier. */
    @PathParameter
    private final long masterId;
    /** The format specifier. */
    @QueryParameter
    private final String format;
    /** The label. */
    @QueryParameter
    private final String label;
    /** The year released. */
    @QueryParameter
    private final Integer released;
    /** The master release country. */
    @QueryParameter
    private final String country;
    /**
     * Sort by specific attributes.
     *
     * @see Sort
     */
    @QueryParameter
    private final Sort sort;
    /**
     * The sort order.
     *
     * @see SortOrder
     */
    @QueryParameter
    private final SortOrder sortOrder;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        Validate.isTrue(masterId > 0L, "masterId must be > 0");

        super.populateQueryParameters(urlBuilder);
        appendIfNotBlank(urlBuilder, "format", format);
        appendIfNotBlank(urlBuilder, "label", label);
        appendIfNotNull(urlBuilder, "released", released);
        appendIfNotBlank(urlBuilder, "country", country);
        appendIfNotNull(urlBuilder, Sort.QUERY_PARAM_NAME, sort);
        appendIfNotNull(urlBuilder, SortOrder.QUERY_PARAM_NAME, sortOrder);

        return urlBuilder;
    }

    /** Defines the supported attributes to sort on for requests. */
    @RequiredArgsConstructor
    @Getter
    public enum Sort {
        /** The release label .*/
        RELEASED("released"),
        /** The release title. */
        TITLE("title"),
        /** The release format. */
        FORMAT("format"),
        /** The label. */
        LABEL("label"),
        /** The release catalog number. */
        CATALOG_NUMBER("catno"),
        COUNTRY("country");

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
