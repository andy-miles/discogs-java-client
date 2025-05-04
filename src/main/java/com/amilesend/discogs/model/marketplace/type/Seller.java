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

import com.amilesend.discogs.model.Resource;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Describes a seller resource.
 *
 * @see Resource
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class Seller extends Resource<Long, Seller> {
    /** The seller's username. */
    private final String username;
    /** The URL to the user's avatar image. */
    private final String avatarUrl;
    /** The API resource URL for the user. */
    private final String url;
    /** Shipping information for the seller. */
    private final String shipping;
    /** Supported payment method. */
    private final String payment;
    /** Seller statistics. */
    private final SellerStats stats;
    /** The minimum order total. */
    private final Double minOrderTotal;
    /** The web-based URL for the seller's profile. */
    private final String htmlUrl;
    /** The associated user identifier. */
    private final Long uid;
}
