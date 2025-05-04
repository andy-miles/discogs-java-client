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
package com.amilesend.discogs.model.marketplace.type;

import com.amilesend.discogs.model.type.Currency;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

/** Describes the original price for a listing. */
@Builder
@Data
public class OriginalPrice {
    /** The currency type. */
    @SerializedName("curr_abbr")
    private final Currency currency;
    /** The currency identifier. */
    @SerializedName("curr_id")
    private final Integer currencyId;
    /** The formated value (e.g., "$20.00"). */
    private final String formatted;
    /** The price. */
    private final Double value;
    /** The converted original price. */
    private final OriginalPrice converted;
}
