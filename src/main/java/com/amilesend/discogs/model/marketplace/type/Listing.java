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

import com.amilesend.discogs.model.AuthenticationRequired;
import com.amilesend.discogs.model.Resource;
import com.amilesend.discogs.model.type.ListingStatus;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Describes a listing.
 *
 * @see Resource
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class Listing extends Resource<Long, Listing> {
    /**
     * The listing status.
     *
     * @see ListingStatus
     */
    private final ListingStatus status;
    /**
     * The price.
     *
     * @see Price
     */
    private final Price price;
    /** The original price. */
    private final OriginalPrice originalPrice;
    /** Flag indicator for allowing offers. */
    private final Boolean allowOffers;
    /** Flag indicator for offer submission status. */
    private final Boolean offerSubmitted;
    /**
     * Describes the packaging condition.
     *
     * @see SleeveCondition
     */
    private final SleeveCondition sleeveCondition;
    /**
     * The condition.
     *
     * @see Condition
     */
    private final Condition condition;
    /** The listing posted timestamp. */
    private final LocalDateTime posted;
    /** Where the item is shipped from. */
    private final String shipsFrom;
    /** The website URI. */
    private final String uri;
    /** Seller commends. */
    private final String comments;
    /**
     * The seller.
     *
     * @see Seller
     */
    private final Seller seller;
    /**
     * The release being listed.
     *
     * @see ListingRelease
     */
    private final ListingRelease release;
    /** Indicates if the item is audio. */
    private final Boolean audio;
    /** The listing weight (only visible to the inventory owner). */
    @AuthenticationRequired
    private final Integer weight;
    /** The number of items of a given format available (only visible to the inventory owner). */
    @AuthenticationRequired
    private final Integer formatQuantity;
    /** The external identifier (only visible to the inventory owner). */
    @AuthenticationRequired
    private final String externalId;
    /** The location (only visible to the inventory owner). */
    @AuthenticationRequired
    private final String location;
    /** The total quantity available (only visible to the inventory owner). */
    @AuthenticationRequired
    private final Integer quantity;
    /** Indicates if an item is in a user's cart (only visible to the inventory owner). */
    @AuthenticationRequired
    private final Boolean inCart;
}
