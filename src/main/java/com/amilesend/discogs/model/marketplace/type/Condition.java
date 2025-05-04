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
package com.amilesend.discogs.model.marketplace.type;

import com.amilesend.client.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum Condition {
    MINT("Mint (M)"),
    NEAR_MINT("Near Mint (NM or M-)"),
    VERY_GOOD_PLUS("Very Good Plus (VG+)"),
    VERY_GOOD("Very Good (VG)"),
    GOOD_PLUS("Good Plus (G+)"),
    GOOD("Good (G)"),
    FAIR("Fair (F)"),
    POOR("Poor (P)");

    /** The list of possible values. */
    public static List<String> VALUES = getValueList();

    /** The map of values to enum references used for JSON marshalling. */
    private static Map<String, Condition> VALUE_TO_ENUM = getValueToEnumMap();

    private final String value;

    @Override
    public String toString() {
        return value;
    }

    /**
     * Gets the {@code Condition} enum from the given value.
     *
     * @param value the value
     * @return the condition, or {@code null}
     */
    public static Condition fromValue(final String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return VALUE_TO_ENUM.get(value);
    }

    private static Map<String, Condition> getValueToEnumMap() {
        return Arrays.stream(Condition.values())
                .collect(Collectors.toMap(Condition::getValue, (c) -> c));
    }

    private static List<String> getValueList() {
        return Arrays.stream(Condition.values())
                .map(Condition::getValue)
                .collect(Collectors.toList());
    }
}
