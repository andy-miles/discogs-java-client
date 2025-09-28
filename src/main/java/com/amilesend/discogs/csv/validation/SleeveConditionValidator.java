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
import com.amilesend.discogs.model.marketplace.type.SleeveCondition;
import lombok.NonNull;

import java.util.Objects;
import java.util.StringJoiner;

import static com.amilesend.discogs.csv.validation.ValueValidator.formatMessage;

/** Validates a sleeve condition attribute. */
public class SleeveConditionValidator implements ValueValidator {
    private static String POSSIBLE_BASE_VALUES_STRING = getPossibleValuesAsSingleLine();

    @Override
    public void validate(
            final String value,
            @NonNull final InventoryHeader header,
            final Integer row,
            final Integer col) throws ValidationException {
        // Blank is valid as this field is optional
        if (StringUtils.isBlank(value)) {
            return;
        }

        final SleeveCondition parsedCondition = SleeveCondition.fromValue(value);
        if (Objects.isNull(parsedCondition)) {
            final String msg = formatMessage(header, "Condition must be of: " + POSSIBLE_BASE_VALUES_STRING);
            throw new ValidationException(
                    msg,
                    ValidationException.Descriptor.builder()
                            .message(msg)
                            .header(header)
                            .value(value)
                            .row(row)
                            .col(col)
                            .build());
        }
    }

    private static String getPossibleValuesAsSingleLine() {
        final StringJoiner sj = new StringJoiner(",");
        SleeveCondition.VALUES.stream().forEach(v -> sj.add("\"" + v + "\""));
        return sj.toString();
    }
}
