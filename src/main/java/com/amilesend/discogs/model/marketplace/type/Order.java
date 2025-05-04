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

import java.time.LocalDateTime;
import java.util.List;

/**
 * Describes an order.
 *
 * @see Resource
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class Order extends Resource<String, Order> {
    /** The URL for messages related to the order. */
    private final String messagesUrl;
    /** The website URI. */
    private final String uri;
    /**
     * The order status.
     *
     * @see OrderStatus
     */
    private final OrderStatus status;
    /**
     * The list of remaining order status values for the order.
     *
     * @see OrderStatus
     */
    private final List<OrderStatus> nextStatus;
    /**
     * The order fee.
     *
     * @see Price
     */
    private final Price fee;
    /** The order creation timestamp. */
    private final LocalDateTime created;
    /**
     * The list of items that were ordered.
     *
     * @see OrderItem
     */
    private final List<OrderItem> items;
    /**
     * The shipping charge information.
     *
     * @see ShippingChargeAmount
     */
    private final ShippingChargeAmount shipping;
    /** The shipping address. */
    private final String shippingAddress;
    /** Additional instructions provided by the buyer. */
    private final String additionalInstructions;
    /** Archived order flag indicator. */
    private final Boolean archived;
    /**
     * The seller information.
     *
     * @see Seller
     */
    private final Seller seller;
    /** The last updated timestamp. */
    private final LocalDateTime lastActivity;
    /**
     * The buyer information.
     *
     * @see Buyer
     */
    private final Buyer buyer;
    /**
     * The total order amount.
     *
     * @see Price
     */
    private final Price total;
}
