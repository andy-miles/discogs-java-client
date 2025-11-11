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

import com.amilesend.client.util.StringUtils;
import com.amilesend.discogs.csv.type.InventoryHeader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.amilesend.discogs.csv.validation.ValueValidator.formatMessage;

/** Validates tha a number is defined and positive. */
@RequiredArgsConstructor
public class PositiveIntValidator implements ValueValidator {
    private final boolean isNullable;

    @Override
    public void validate(
            final String value,
            @NonNull final InventoryHeader header,
            final Integer row,
            final Integer col) throws ValidationException {
        if (StringUtils.isBlank(value)) {
            if (isNullable) {
                return;
            }

            final String msg = formatMessage(header, "value must not be blank");
            throw new ValidationException(
                    msg,
                    ValidationException.Descriptor.builder()
                            .message(msg)
                            .row(row)
                            .col(col)
                            .value(value)
                            .header(header)
                            .build());
        }

        try {
            final int parsedValue = Integer.parseInt(value);

            if (parsedValue < 1) {
                final String msg = formatMessage(header, "value must be > 0");
                throw new ValidationException(
                        msg,
                        ValidationException.Descriptor.builder()
                                .message(msg)
                                .row(row)
                                .col(col)
                                .value(value)
                                .header(header)
                                .build());
            }
        } catch (final NumberFormatException ex) {
            final String msg = formatMessage(header, "value is not an integer");
            throw new ValidationException(
                    msg,
                    ValidationException.Descriptor.builder()
                            .message(msg)
                            .row(row)
                            .col(col)
                            .value(value)
                            .header(header)
                            .build(),
                    ex);
        }
    }
}
