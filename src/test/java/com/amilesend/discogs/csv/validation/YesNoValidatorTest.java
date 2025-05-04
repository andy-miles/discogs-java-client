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
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class YesNoValidatorTest {
    private YesNoValidator validatorUnderTest = new YesNoValidator();

    @Test
    @SneakyThrows
    public void validate_withValidValue_shouldDoNothing() {
        validatorUnderTest.validate("Y", InventoryHeader.ACCEPT_OFFER, 2, 7);
    }

    @Test
    @SneakyThrows
    public void validate_withInvalidValue_shouldThrowException() {
        final ValidationException thrown = assertThrows(ValidationException.class,
                () -> validatorUnderTest.validate("Unknown", InventoryHeader.ACCEPT_OFFER, 2, 7));

        final ValidationException.Descriptor descriptor = thrown.getErrors().get(0);
        assertAll(
                () -> assertTrue(thrown.getMessage().contains("value must be \"Y\" or \"N\"")),
                () -> assertTrue(descriptor.getMessage().contains("value must be \"Y\" or \"N\"")),
                () -> assertEquals("Unknown", descriptor.getValue()),
                () -> assertEquals(InventoryHeader.ACCEPT_OFFER, descriptor.getHeader()),
                () -> assertEquals(2, descriptor.getRow()),
                () -> assertEquals(7, descriptor.getCol()));
    }

    @Test
    @SneakyThrows
    public void validate_withEmptyValue_shouldDoNothing() {
        validatorUnderTest.validate(StringUtils.EMPTY, InventoryHeader.ACCEPT_OFFER, 2, 7);
    }

    @Test
    @SneakyThrows
    public void validate_withNullValue_shouldDoNothing() {
        validatorUnderTest.validate(null, InventoryHeader.ACCEPT_OFFER, 2, 7);
    }

    @Test
    @SneakyThrows
    public void validate_withNullHeader_shouldThrowException() {
        assertThrows(NullPointerException.class,
                () -> validatorUnderTest.validate("N", null, 2, 7));
    }
}
