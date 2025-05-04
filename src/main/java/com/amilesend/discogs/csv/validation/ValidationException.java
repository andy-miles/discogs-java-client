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

import com.amilesend.discogs.csv.type.InventoryHeader;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** Defines the exception type for inventory CSV validation. */
@Getter
public class ValidationException extends Exception {
    private final List<Descriptor> errors;

    /**
     * Creates a new {@code ValidationException}.
     *
     * @param msg the exception message
     */
    public ValidationException(final String msg) {
        super(msg);
        errors = Collections.emptyList();
    }

    /**
     * Creates a new {@code ValidationException}.
     *
     * @param msg the exception message
     * @param error the error descriptor
     */
    public ValidationException(final String msg, final Descriptor error) {
        super(msg);
        errors = Collections.singletonList(error);
    }

    /**
     * Creates a new {@code ValidationException}.
     *
     * @param msg the exception message
     * @param error the error descriptor
     * @param cause the cause
     */
    public ValidationException(final String msg, final Descriptor error, final Throwable cause) {
        super(msg, cause);
        errors = Collections.singletonList(error);
    }

    /**
     * Creates a new {@code ValidationException}.
     *
     * @param msg the exception message
     * @param errors the collection of error descriptors
     */
    public ValidationException(final String msg, final Collection<Descriptor> errors) {
        super(msg);
        this.errors = List.copyOf(errors);
    }

    @Builder
    @Data
    public static class Descriptor {
        private final InventoryHeader header;
        private final String value;
        private final Integer row;
        private final Integer col;
        private final String message;
    }
}
