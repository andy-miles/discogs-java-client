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
package com.amilesend.discogs.csv.validation;

import com.amilesend.discogs.csv.type.InventoryHeader;

/** Defines the interface to validate CSV value attributes based on type and/or conditions. */
public interface ValueValidator {
    /**
     * Validates a CSV attribute.
     *
     * @param value to validate
     * @param header the associated header
     * @param row the row index
     * @param col the column index
     * @throws ValidationException if the field contains an invalid/malformed value
     */
    void validate(String value, InventoryHeader header, Integer row, Integer col) throws ValidationException;

    static String formatMessage(final InventoryHeader header, final String msg) {
        return new StringBuilder("[")
                .append(header.getHeader())
                .append("] ")
                .append(msg)
                .toString();
    }
}
