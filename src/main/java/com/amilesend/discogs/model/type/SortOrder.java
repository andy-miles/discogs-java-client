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
package com.amilesend.discogs.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Defines the types of sort order options used for requests. */
@RequiredArgsConstructor
@Getter
public enum SortOrder {
    /** Ascending sort order. */
    ASC("asc"),
    /** Descending sort order. */
    DESC("desc");

    /** The query parameter value name. */
    public static final String QUERY_PARAM_NAME = "sort_order";

    /** The query parameter value. */
    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
