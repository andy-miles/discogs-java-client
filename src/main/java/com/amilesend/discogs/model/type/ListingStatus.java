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
package com.amilesend.discogs.model.type;

import com.amilesend.client.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/** Defines the listing status types. */
@RequiredArgsConstructor
@Getter
public enum ListingStatus {
    FOR_SALE("For Sale"),
    DRAFT("Draft");

    /** The query parameter attribute name. */
    public static final String QUERY_PARAM_NAME = "status";

    /** The map of values to enum references used for JSON marshalling. */
    private static Map<String, ListingStatus> VALUE_TO_ENUM = getValueToEnumMap();

    /** The query parameter attribute value. */
    private final String value;

    /**
     * Gets the {@code ListingStatus} enum reference from the given value.
     *
     * @param value the value
     * @return the search type, or {@code null} if none exist
     */
    public static ListingStatus fromValue(final String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return VALUE_TO_ENUM.get(value);
    }

    @Override
    public String toString() {
        return value;
    }

    private static Map<String, ListingStatus> getValueToEnumMap() {
        return Arrays.stream(ListingStatus.values())
                .collect(Collectors.toMap(ListingStatus::getValue, (ls) -> ls));
    }
}
