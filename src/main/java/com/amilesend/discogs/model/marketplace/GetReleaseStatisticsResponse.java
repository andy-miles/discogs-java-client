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
package com.amilesend.discogs.model.marketplace;

import com.amilesend.discogs.model.marketplace.type.Price;
import lombok.Builder;
import lombok.Data;

/**
 * Describes the response for the sales statistics for a given release.
 */
@Builder
@Data
public class GetReleaseStatisticsResponse {
    /**
     * The lowest price.
     *
     * @see Price
     */
    private final Price lowestPrice;
    /** The number of items for sale. */
    private final Integer numForSale;
    /** Blocked from stale flag indicator. */
    private final Boolean blockedFormSale;
}
