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

public class ZeroOrPositiveIntValidatorTest {
    private ZeroOrPositiveIntValidator nullableValidatorUnderTest = new ZeroOrPositiveIntValidator(true);
    private ZeroOrPositiveIntValidator validatorUnderTest = new ZeroOrPositiveIntValidator(false);

    @Test
    @SneakyThrows
    public void validate_withValidValue_shouldDoNothing() {
        validatorUnderTest.validate("10", InventoryHeader.WEIGHT, 2, 2);
    }

    @Test
    @SneakyThrows
    public void validate_withZeroValue_shouldDoNothing() {
        validatorUnderTest.validate("0", InventoryHeader.WEIGHT, 2, 2);
    }

    @Test
    @SneakyThrows
    public void validate_withNegativeValue_shouldThrowException() {
        final ValidationException thrown = assertThrows(ValidationException.class,
                () -> validatorUnderTest.validate("-1", InventoryHeader.PRICE, 2, 2));

        final ValidationException.Descriptor descriptor = thrown.getErrors().get(0);
        assertAll(
                () -> assertTrue(thrown.getMessage().contains("value must be >= 0")),
                () -> assertTrue(descriptor.getMessage().contains("value must be >= 0")),
                () -> assertEquals("-1", descriptor.getValue()),
                () -> assertEquals(InventoryHeader.PRICE, descriptor.getHeader()),
                () -> assertEquals(2, descriptor.getRow()),
                () -> assertEquals(2, descriptor.getCol()));
    }

    @Test
    @SneakyThrows
    public void validate_withNonIntegerValue_shouldThrowException() {
        final ValidationException thrown = assertThrows(ValidationException.class,
                () -> validatorUnderTest.validate("Not an integer", InventoryHeader.PRICE, 2, 2));

        final ValidationException.Descriptor descriptor = thrown.getErrors().get(0);
        assertAll(
                () -> assertInstanceOf(NumberFormatException.class, thrown.getCause()),
                () -> assertTrue(thrown.getMessage().contains("value is not an integer")),
                () -> assertTrue(descriptor.getMessage().contains("value is not an integer")),
                () -> assertEquals("Not an integer", descriptor.getValue()),
                () -> assertEquals(InventoryHeader.PRICE, descriptor.getHeader()),
                () -> assertEquals(2, descriptor.getRow()),
                () -> assertEquals(2, descriptor.getCol()));
    }

    @Test
    @SneakyThrows
    public void validate_withEmptyValueAndNonNullableValidator_shouldThrowException() {
        final ValidationException thrown = assertThrows(ValidationException.class,
                () -> validatorUnderTest.validate(StringUtils.EMPTY, InventoryHeader.PRICE, 2, 2));

        final ValidationException.Descriptor descriptor = thrown.getErrors().get(0);
        assertAll(
                () -> assertTrue(thrown.getMessage().contains("value must not be blank")),
                () -> assertTrue(descriptor.getMessage().contains("value must not be blank")),
                () -> assertEquals(StringUtils.EMPTY, descriptor.getValue()),
                () -> assertEquals(InventoryHeader.PRICE, descriptor.getHeader()),
                () -> assertEquals(2, descriptor.getRow()),
                () -> assertEquals(2, descriptor.getCol()));
    }

    @Test
    @SneakyThrows
    public void validate_withNullValueAndNonNullableValidator_shouldThrowException() {
        final ValidationException thrown = assertThrows(ValidationException.class,
                () -> validatorUnderTest.validate(null, InventoryHeader.PRICE, 2, 2));

        final ValidationException.Descriptor descriptor = thrown.getErrors().get(0);
        assertAll(
                () -> assertTrue(thrown.getMessage().contains("value must not be blank")),
                () -> assertTrue(descriptor.getMessage().contains("value must not be blank")),
                () -> assertNull(descriptor.getValue()),
                () -> assertEquals(InventoryHeader.PRICE, descriptor.getHeader()),
                () -> assertEquals(2, descriptor.getRow()),
                () -> assertEquals(2, descriptor.getCol()));
    }

    @Test
    @SneakyThrows
    public void validate_withEmptyValueAndNullableValidator_shouldDoNothing() {
        nullableValidatorUnderTest.validate(StringUtils.EMPTY, InventoryHeader.PRICE, 2, 2);
    }

    @Test
    @SneakyThrows
    public void validate_withNullValueAndNullableValidator_shouldDoNothing() {
        nullableValidatorUnderTest.validate(null, InventoryHeader.PRICE, 2, 2);
    }

    @Test
    @SneakyThrows
    public void validate_withNullHeader_shouldThrowException() {
        assertThrows(NullPointerException.class,
                () -> validatorUnderTest.validate("10", null, 2, 2));
    }
}
