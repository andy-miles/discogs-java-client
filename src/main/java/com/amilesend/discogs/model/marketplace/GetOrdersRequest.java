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

import com.amilesend.discogs.model.PaginatedRequestBase;
import com.amilesend.discogs.model.QueryParameter;
import com.amilesend.discogs.model.marketplace.type.OrderStatus;
import com.amilesend.discogs.model.type.SortOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import okhttp3.HttpUrl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotBlank;
import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotNull;

/**
 * The request to retrieve the paginated list of orders for a user.
 *
 * @see PaginatedRequestBase
 */
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetOrdersRequest extends PaginatedRequestBase {
    /**
     * The order status (optional).
     *
     * @see OrderStatus
     */
    @QueryParameter
    private final OrderStatus status;
    /** Created after timestamp (optional). */
    @QueryParameter
    private final LocalDateTime createdAfter;
    /** Created before timestamp (optional). */
    @QueryParameter
    private final LocalDateTime createdBefore;
    /** Archived flag indicator (optional). */
    @QueryParameter
    private final Boolean archived;
    /**
     * The attribute to sort on (optional).
     *
     * @see Sort
     */
    @QueryParameter
    private final Sort sort;
    /**
     * The sort order (optional).
     *
     * @see SortOrder
     */
    @QueryParameter
    private final SortOrder sortOrder;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        super.populateQueryParameters(urlBuilder);
        appendIfNotNull(urlBuilder, OrderStatus.QUERY_PARAM_NAME, status);
        if (Objects.nonNull(createdAfter)) {
            appendIfNotBlank(urlBuilder, "created_after", getFormattedTimestamp(createdAfter));
        }
        if (Objects.nonNull(createdBefore)) {
            appendIfNotBlank(urlBuilder, "created_before", getFormattedTimestamp(createdBefore));
        }
        appendIfNotNull(urlBuilder, "archived", archived);
        appendIfNotNull(urlBuilder, Sort.QUERY_PARAM_NAME, sort);
        appendIfNotNull(urlBuilder, SortOrder.QUERY_PARAM_NAME, sortOrder);
        return urlBuilder;
    }

    /* Using ZonedDateTime addresses a potential UnsupportedTemporalTypeException. */
    private String getFormattedTimestamp(final LocalDateTime timestamp) {
        final ZonedDateTime zonedDateTime = timestamp.atZone(ZoneId.systemDefault());
        return zonedDateTime.format(DateTimeFormatter.ISO_INSTANT);
    }

    /** Describes the sort options for requesting a list of orders. */
    @RequiredArgsConstructor
    public enum Sort {
        ID("id"),
        BUYER("buyer"),
        CREATED("created"),
        STATUS("status"),
        LAST_ACTIVITY("last_activity");

        public static final String QUERY_PARAM_NAME = "sort";

        private final String value;

        @Override
        public String toString() {
            return value;
        }
    }
}
