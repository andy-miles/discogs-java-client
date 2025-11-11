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

import com.amilesend.client.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    NEW_ORDER("New Order"),
    BUYER_CONTACTED("Buyer Contacted"),
    INVOICE_SENT("Invoice Sent"),
    PAYMENT_PENDING("Payment Pending"),
    PAYMENT_RECEIVED("Payment Received"),
    IN_PROGRESS("In Progress"),
    SHIPPED("Shipped"),
    REFUND_SENT("Refund Sent"),
    CANCELLED_NON_PAYMENT("Cancelled (Non-Paying Buyer)"),
    CANCELLED_ITEM_UNAVAILABLE("Cancelled (Item Unavailable)"),
    CANCELLED_BUYER_REQUEST("Cancelled (Per Buyer's Request)");

    public static String QUERY_PARAM_NAME = "status";

    /** The map of values to enum references used for JSON marshalling. */
    private static Map<String, OrderStatus> VALUE_TO_ENUM = getValueToEnumMap();

    private final String value;

    @Override
    public String toString() {
        return value;
    }

    /**
     * Gets the {@code Condition} enum from the given value.
     *
     * @param value the value
     * @return the condition, or {@code null}
     */
    public static OrderStatus fromValue(final String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        return VALUE_TO_ENUM.get(value);
    }

    private static Map<String, OrderStatus> getValueToEnumMap() {
        return Arrays.stream(OrderStatus.values())
                .collect(Collectors.toMap(OrderStatus::getValue, (os) -> os));
    }
}
