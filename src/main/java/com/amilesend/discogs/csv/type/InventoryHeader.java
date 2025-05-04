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
package com.amilesend.discogs.csv.type;

import com.amilesend.client.util.StringUtils;
import com.amilesend.discogs.csv.validation.MediaConditionValidator;
import com.amilesend.discogs.csv.validation.NoOpValidator;
import com.amilesend.discogs.csv.validation.PositiveIntValidator;
import com.amilesend.discogs.csv.validation.PriceValidator;
import com.amilesend.discogs.csv.validation.SleeveConditionValidator;
import com.amilesend.discogs.csv.validation.ValueValidator;
import com.amilesend.discogs.csv.validation.YesNoValidator;
import com.amilesend.discogs.csv.validation.ZeroOrPositiveIntValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Defines the CSV headers for an inventory IMPORT/EXPORT file. */
@RequiredArgsConstructor
@Getter
public enum InventoryHeader {
    RELEASE_ID("release_id", true, new PositiveIntValidator( false)),
    PRICE("price", true, new PriceValidator()),
    MEDIA_CONDITION("media_condition", true, new MediaConditionValidator()),
    COMMENTS("comments", false, new NoOpValidator()),
    SLEEVE_CONDITION("sleeve_condition", false, new SleeveConditionValidator()),
    ACCEPT_OFFER("accept_offer", false, new YesNoValidator()),
    LOCATION("location", false, new NoOpValidator()),
    EXTERNAL_ID("external_id", false, new NoOpValidator()),
    WEIGHT("weight", false, new ZeroOrPositiveIntValidator(true)),
    FORMAT_QUANTITY("format_quantity", false, new ZeroOrPositiveIntValidator(true));

    /** The map of CSV header values to enum references. */
    private static Map<String, InventoryHeader> VALUE_TO_ENUM = getValueToEnumMap();

    private static List<InventoryHeader> REQUIRED_FOR_NEW_RECORD = List.of(RELEASE_ID, PRICE, MEDIA_CONDITION);
    private static List<InventoryHeader> REQUIRED_FOR_UPDATE = List.of(RELEASE_ID);

    private final String header;
    private final boolean isRequired;
    private final ValueValidator validator;

    /**
     * Gets the {@code InventoryHeader} enum from the given value.
     *
     * @param value the value
     * @return the header, or {@code null}
     */
    public static InventoryHeader fromValue(final String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return VALUE_TO_ENUM.get(value);
    }

    /**
     * Gets the list of headers that are required.
     *
     * @return the list of required headers
     */
    public static List<InventoryHeader> getRequiredHeaders(final InventoryRecordType type) {
        switch (type) {
            case UPDATE:
                return REQUIRED_FOR_UPDATE;
            default:
            case NEW:
                return REQUIRED_FOR_NEW_RECORD;
        }
    }

    @Override
    public String toString() {
        return header;
    }

    private static Map<String, InventoryHeader> getValueToEnumMap() {
        return Arrays.stream(InventoryHeader.values())
                .collect(Collectors.toMap(InventoryHeader::getHeader, (c) -> c));
    }
}
