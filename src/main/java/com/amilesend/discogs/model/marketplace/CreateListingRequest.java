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

import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.BodyBasedRequest;
import com.amilesend.discogs.model.BodyParameter;
import com.amilesend.discogs.model.marketplace.type.Condition;
import com.amilesend.discogs.model.marketplace.type.SleeveCondition;
import com.amilesend.discogs.model.type.ListingStatus;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import okhttp3.HttpUrl;

/**
 * The request to create a new listing.
 *
 * @see BodyBasedRequest
 */
@SuperBuilder
@Data
public class CreateListingRequest implements BodyBasedRequest {
    /** The release identifier (required). */
    @BodyParameter
    private final long releaseId;
    /**
     * The condition of the listing item (required).
     *
     * @see Condition
     */
    @BodyParameter
    private final Condition condition;
    /**
     * The listing status (required).
     *
     * @see ListingStatus
     */
    @BodyParameter
    private final ListingStatus status;
    /** The price in the seller's currency (required). */
    @BodyParameter
    private final double price;
    /**
     * The sleeve condition of the listing item (optional).
     *
     * @see SleeveCondition
     */
    @BodyParameter
    private final SleeveCondition sleeveCondition;
    /** Item remarks (optional). */
    @BodyParameter
    private final String comments;
    /** Indicator to allow offers (optional). */
    @BodyParameter
    private final Boolean allowOffers;
    /** The external identifier (optional). */
    @BodyParameter
    private final String externalId;
    /** The item location (optional). */
    @BodyParameter
    private final String location;
    /**
     * The item wight in grams (optional).
     * Note: Set this field to {@code "auto"} to have the weight automatically estimated.
     */
    @BodyParameter
    private final String weight;
    /**
     * The number of items this listing counts as (for shipping calculations) (optional).
     * Note: Set this field to {@code "auto"} to have the quantity estimated.
     */
    @BodyParameter
    private final Double formatQuantity;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        Validate.isTrue(releaseId > 0L, "releaseId must be > 0");
        Validate.notNull(condition, "condition must not be null");
        return urlBuilder;
    }
}
