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

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/** Describes a message associated with an order. */
@SuperBuilder
@Data
public class OrderMessage {
    /**
     * The amount refunded (if applicable).
     *
     * @see OrderRefund
     */
    private final OrderRefund refund;
    /** The message timestamp. */
    private final LocalDateTime timestamp;
    /** The message content. */
    private final String message;
    /** The message type. */
    private final String type;
    /**
     * The associated order reference.
     *
     * @see OrderReference
     */
    private final OrderReference order;
    /** The message subject. */
    private final String subject;
}
