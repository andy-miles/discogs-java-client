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

import com.amilesend.discogs.model.PaginatedRequestBase;
import com.amilesend.discogs.model.QueryParameter;
import com.amilesend.discogs.model.database.type.SearchType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import okhttp3.HttpUrl;

import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotBlank;
import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotNull;

/**
 * The request to search for resources
 *
 * @see PaginatedRequestBase
 */
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SearchRequest extends PaginatedRequestBase {
    /** The search query (optional). */
    @QueryParameter
    private final String query;
    /**
     * The resource type (optional).
     *
     * @see SearchType
     */
    @QueryParameter
    private final SearchType type;
    /** The title (optional). */
    @QueryParameter
    private final String title;
    /** The release title (optional). */
    @QueryParameter
    private final String releaseTitle;
    /** Credit (optional). */
    @QueryParameter
    private final String credit;
    /** The artist name (optional). */
    @QueryParameter
    private final String artist;
    /** Artist ANV (optional). */
    @QueryParameter
    private final String anv;
    /** Label (optional). */
    @QueryParameter
    private final String label;
    /** Genre (optional). */
    @QueryParameter
    private final String genre;
    /** Style (optional). */
    @QueryParameter
    private final String style;
    /** Release country (optional). */
    @QueryParameter
    private final String country;
    /** Release year (optional). */
    @QueryParameter
    private final Integer year;
    /** Release format (optional). */
    @QueryParameter
    private final String format;
    /** Catalog number (i.e., "catno") (optional). */
    @QueryParameter
    private final String catalogNumber;
    /** Barcode (optional). */
    @QueryParameter
    private final String barcode;
    /** Track name (optional). */
    @QueryParameter
    private final String track;
    /** The submitter username (optional). */
    @QueryParameter
    private final String submitter;
    /** The contributor username (optional). */
    @QueryParameter
    private final String contributor;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        super.populateQueryParameters(urlBuilder);
        appendIfNotBlank(urlBuilder, "q", query);
        appendIfNotNull(urlBuilder, "type", type);
        appendIfNotBlank(urlBuilder, "title", title);
        appendIfNotBlank(urlBuilder, "release_title", releaseTitle);
        appendIfNotBlank(urlBuilder, "credit", credit);
        appendIfNotBlank(urlBuilder, "artist", artist);
        appendIfNotBlank(urlBuilder, "anv", anv);
        appendIfNotBlank(urlBuilder, "label", label);
        appendIfNotBlank(urlBuilder, "genre", genre);
        appendIfNotBlank(urlBuilder, "style", style);
        appendIfNotBlank(urlBuilder, "country", country);
        appendIfNotNull(urlBuilder, "year", year);
        appendIfNotBlank(urlBuilder, "format", format);
        appendIfNotBlank(urlBuilder, "catno", catalogNumber);
        appendIfNotBlank(urlBuilder, "barcode", barcode);
        appendIfNotBlank(urlBuilder, "track", track);
        appendIfNotBlank(urlBuilder, "submitter", submitter);
        appendIfNotBlank(urlBuilder, "contributor", contributor);
        return urlBuilder;
    }
}
