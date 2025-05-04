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
package com.amilesend.discogs.model.marketplace;

import com.amilesend.client.parse.strategy.GsonExclude;
import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.BodyBasedRequest;
import com.amilesend.discogs.model.BodyParameter;
import com.amilesend.discogs.model.PathParameter;
import com.amilesend.discogs.model.marketplace.type.Order;
import com.amilesend.discogs.model.marketplace.type.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import okhttp3.HttpUrl;

/**
 * The request to update an order.
 *
 * @see Order
 */
@Builder
@Data
public class UpdateOrderRequest implements BodyBasedRequest {
    /** The order identifier (required). */
    @PathParameter
    @GsonExclude
    private final String orderId;
    /** The updated order status (optional). */
    @BodyParameter
    private final OrderStatus orderStatus;
    /** The updated shipping amount (optional). */
    @BodyParameter
    private final Double shipping;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        Validate.notBlank(orderId, "orderId must not be blank");
        return urlBuilder;
    }
}
