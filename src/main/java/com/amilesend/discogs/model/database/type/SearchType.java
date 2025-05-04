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

import com.amilesend.client.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Defines the resource types to search for */
@RequiredArgsConstructor
@Getter
public enum SearchType {
    RELEASE("release"),
    MASTER("master"),
    ARTIST("artist"),
    LABEL("label");

    /** The query parameter attribute name. */
    public static final String QUERY_PARAM_NAME = "type";

    /** The map of values to enum references used for JSON marshalling. */
    private static Map<String, SearchType> VALUE_TO_ENUM = getValueToEnumMap();

    /** The query parameter attribute value. */
    private final String value;

    /**
     * Gets the {@code SearchType} enum reference from the given value.
     *
     * @param value the value
     * @return the search type, or {@code null} if none exist
     */
    public static SearchType fromValue(final String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return VALUE_TO_ENUM.get(value);
    }

    @Override
    public String toString() {
        return value;
    }

    private static Map<String, SearchType> getValueToEnumMap() {
        return Arrays.stream(SearchType.values())
                .collect(Collectors.toMap(SearchType::getValue, (s) -> s));
    }
}
