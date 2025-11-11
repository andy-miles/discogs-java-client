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
package com.amilesend.discogs.model.marketplace.type;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/** Describes an item associated with an {@link Order}. */
@Builder
@Getter
@ToString
public class OrderItem {
    /** The item identifier. */
    private final Long id;
    /**
     * The release information.
     *
     * @see Release
     */
    private final Release release;
    /**
     * The price.
     *
     * @see Price
     */
    private final Price price;

    @Override
    public boolean equals(final Object that) {
        if (that == null || getClass() != that.getClass()) {
            return false;
        }

        final OrderItem thatOrderItem = (OrderItem) that;
        return Objects.equals(id, thatOrderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /** Describes a release for an order item. */
    @Builder
    @Getter
    @ToString
    public static class Release {
        /** The release identifier. */
        private final Long id;
        /** The release description. */
        private final String description;

        @Override
        public boolean equals(final Object that) {
            if (that == null || getClass() != that.getClass()) {
                return false;
            }

            final Release thatRelease = (Release) that;
            return Objects.equals(id, thatRelease.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }
}
