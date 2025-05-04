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
package com.amilesend.discogs.model.database.type;

import com.amilesend.discogs.model.Resource;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Defines a search result.
 *
 * @see Resource
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class SearchResult extends Resource<Long, SearchResult> {
    /** The music style. */
    private final List<String> style;
    /** Barcodes associated with the search result. */
    private final List<String> barcode;
    /** Thumbnail URL. */
    private final String thumb;
    /** The title. */
    private final String title;
    /** The release country. */
    private final String country;
    /** The available formats. */
    private final List<String> format;
    /** The website URI. */
    private final String uri;
    /**
     * The stats for the search result.
     *
     * @see SearchResultStat
     */
    private final SearchResultStat community;
    /** Associated labels. */
    private final List<String> label;
    /** The catalog number. */
    @SerializedName("catno")
    private final String catalogNumber;
    /** The release year. */
    private final String year;
    /** List of genres. */
    private final List<String> genre;
    /**
     * The search type.
     *
     * @see SearchType
     */
    private final SearchType type;
    /**
     * User stats.
     *
     * @see SearchResultUserData
     */
    private final SearchResultUserData userData;
    /** The master identifier. */
    private final Long masterId;
    /** The master URL. */
    private final String masterUrl;
    /** The cover image URL. */
    private final String coverImage;
    /** The format quantity. */
    private final Integer formatQuantity;
    /**
     * The list of formats.
     *
     * @see SearchResultFormat
     */
    private final List<SearchResultFormat> formats;
}