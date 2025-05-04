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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PriceValidatorTest {
    private PriceValidator validatorUnderTest = new PriceValidator();

    @Test
    @SneakyThrows
    public void validate_withValidValue_shouldDoNothing() {
        validatorUnderTest.validate("9.99", InventoryHeader.PRICE, 2, 3);
    }

    @Test
    @SneakyThrows
    public void validate_withNonNumberValue_shouldThrowException() {
        final ValidationException thrown = assertThrows(ValidationException.class,
                () -> validatorUnderTest.validate("Not a number", InventoryHeader.PRICE, 2, 3));

        final ValidationException.Descriptor descriptor = thrown.getErrors().get(0);
        assertAll(
                () -> assertInstanceOf(NumberFormatException.class, thrown.getCause()),
                () -> assertTrue(thrown.getMessage().contains("value is not a number")),
                () -> assertTrue(descriptor.getMessage().contains("value is not a number")),
                () -> assertEquals("Not a number", descriptor.getValue()),
                () -> assertEquals(InventoryHeader.PRICE, descriptor.getHeader()),
                () -> assertEquals(2, descriptor.getRow()),
                () -> assertEquals(3, descriptor.getCol()));
    }

    @Test
    @SneakyThrows
    public void validate_withEmptyValue_shouldThrowException() {
        final ValidationException thrown = assertThrows(ValidationException.class,
                () -> validatorUnderTest.validate(StringUtils.EMPTY, InventoryHeader.PRICE, 2, 3));

        final ValidationException.Descriptor descriptor = thrown.getErrors().get(0);
        assertAll(
                () -> assertTrue(thrown.getMessage().contains("value must not be blank")),
                () -> assertTrue(descriptor.getMessage().contains("value must not be blank")),
                () -> assertEquals(StringUtils.EMPTY, descriptor.getValue()),
                () -> assertEquals(InventoryHeader.PRICE, descriptor.getHeader()),
                () -> assertEquals(2, descriptor.getRow()),
                () -> assertEquals(3, descriptor.getCol()));
    }

    @Test
    @SneakyThrows
    public void validate_withNullValue_shouldThrowException() {
        final ValidationException thrown = assertThrows(ValidationException.class,
                () -> validatorUnderTest.validate(null, InventoryHeader.PRICE, 2, 3));

        final ValidationException.Descriptor descriptor = thrown.getErrors().get(0);
        assertAll(
                () -> assertTrue(thrown.getMessage().contains("value must not be blank")),
                () -> assertTrue(descriptor.getMessage().contains("value must not be blank")),
                () -> assertNull(descriptor.getValue()),
                () -> assertEquals(InventoryHeader.PRICE, descriptor.getHeader()),
                () -> assertEquals(2, descriptor.getRow()),
                () -> assertEquals(3, descriptor.getCol()));
    }

    @Test
    @SneakyThrows
    public void validate_withNullHeader_shouldThrowException() {
        assertThrows(NullPointerException.class,
                () -> validatorUnderTest.validate("9.99", null, 2, 3));
    }
}
