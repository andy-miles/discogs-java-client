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
package com.amilesend.discogs.data;

import com.amilesend.discogs.model.NamedResource;
import com.amilesend.discogs.model.PaginatedResponseBase;
import com.amilesend.discogs.model.Resource;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UtilityClass
public class DataValidatorHelper {
    public static void validateResource(final Resource expected, final Resource actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getResourceUrl(), actual.getResourceUrl()));
    }

    public static void validateNamedResource(final NamedResource expected, final NamedResource actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getThumbnailUrl(), actual.getThumbnailUrl()));
    }

    public static <T> void validateListOf(
            final List<T> expected,
            final List<T> actual,
            final ItemValidator<T> itemValidator) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertNotNull(actual);

        if (expected.isEmpty()) {
            assertTrue(actual.isEmpty());
            return;
        }

        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); ++i) {
            itemValidator.validate(expected.get(i), actual.get(i));
        }
    }

    public static void validatePaginatedResponseBase(
            final PaginatedResponseBase expected,
            final PaginatedResponseBase actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertEquals(expected.getPagination(), actual.getPagination());
    }
}
