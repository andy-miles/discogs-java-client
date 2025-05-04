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
package com.amilesend.discogs.csv.validation;

import com.amilesend.client.util.StringUtils;
import com.amilesend.discogs.csv.type.InventoryHeader;
import lombok.NonNull;

import static com.amilesend.discogs.csv.validation.ValueValidator.formatMessage;

/** Validates a price. */
public class PriceValidator implements ValueValidator {
    @Override
    public void validate(
            final String value,
            @NonNull final InventoryHeader header,
            final Integer row,
            final Integer col) throws ValidationException {
        if (StringUtils.isBlank(value)) {
            final String msg = formatMessage(header,  "value must not be blank");
            throw new ValidationException(
                    msg,
                    ValidationException.Descriptor.builder()
                            .message(msg)
                            .header(header)
                            .row(row)
                            .col(col)
                            .value(value)
                            .build());
        }

        try {
            Double.parseDouble(value);
        } catch (final NumberFormatException ex) {
            final String msg = formatMessage(header, "value is not a number");
            throw new ValidationException(
                    msg,
                    ValidationException.Descriptor.builder()
                            .message(msg)
                            .header(header)
                            .row(row)
                            .col(col)
                            .value(value)
                            .build(),
                    ex);
        }
    }
}
