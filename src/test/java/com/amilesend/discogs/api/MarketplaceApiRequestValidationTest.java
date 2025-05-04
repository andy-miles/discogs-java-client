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
package com.amilesend.discogs.api;

import com.amilesend.client.util.StringUtils;
import com.amilesend.discogs.RequestValidationTestBase;
import com.amilesend.discogs.model.marketplace.AddOrderMessageRequest;
import com.amilesend.discogs.model.marketplace.CreateListingRequest;
import com.amilesend.discogs.model.marketplace.DeleteListingRequest;
import com.amilesend.discogs.model.marketplace.GetFeeRequest;
import com.amilesend.discogs.model.marketplace.GetInventoryRequest;
import com.amilesend.discogs.model.marketplace.GetListingRequest;
import com.amilesend.discogs.model.marketplace.GetOrderMessagesRequest;
import com.amilesend.discogs.model.marketplace.GetOrderRequest;
import com.amilesend.discogs.model.marketplace.GetOrdersRequest;
import com.amilesend.discogs.model.marketplace.GetPriceSuggestionsRequest;
import com.amilesend.discogs.model.marketplace.GetReleaseStatisticsRequest;
import com.amilesend.discogs.model.marketplace.UpdateListingRequest;
import com.amilesend.discogs.model.marketplace.UpdateOrderRequest;
import com.amilesend.discogs.model.marketplace.type.OrderStatus;
import com.amilesend.discogs.model.type.Currency;
import com.amilesend.discogs.model.type.SortOrder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MarketplaceApiRequestValidationTest extends RequestValidationTestBase {

    ////////////////////////
    // GetInventoryRequest
    ////////////////////////

    @Test
    public void getInventoryRequest_withValidRequest_shouldPopulateQueryParameters() {
        GetInventoryRequest.builder()
                .username("Username")
                .status("Status")
                .sort(GetInventoryRequest.Sort.ARTIST)
                .sortOrder(SortOrder.ASC)
                .page(1)
                .perPage(5)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("status", "Status"),
                () -> validateQueryParameter("sort", "artist"),
                () -> validateQueryParameter("sort_order", "asc"),
                () -> validateQueryParameter("page", "1"),
                () -> validateQueryParameter("per_page", "5"));
    }

    @Test
    public void getInventoryRequest_withInvalidUsername_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetInventoryRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetInventoryRequest.builder()
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    //////////////////////
    // GetListingRequest
    //////////////////////

    @Test
    public void getListingRequest_withValidRequest_shouldPopulateQueryParameters() {
        GetListingRequest.builder()
                .listingId(1234L)
                .currency(Currency.USD)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        validateQueryParameter("curr_abbr", "USD");
    }

    @Test
    public void getListingRequest_withInvalidListingId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetListingRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    /////////////////////////
    // UpdateListingRequest
    /////////////////////////

    @Test
    public void updateListingRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> UpdateListingRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> UpdateListingRequest.builder()
                                .listingId(1234L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(NullPointerException.class,
                        () -> UpdateListingRequest.builder()
                                .listingId(1234L)
                                .releaseId(2345L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    /////////////////////////
    // DeleteListingRequest
    /////////////////////////

    @Test
    public void deleteListingRequest_withInvalidListingId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> DeleteListingRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    /////////////////////////
    // CreateListingRequest
    /////////////////////////

    @Test
    public void createListingRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> CreateListingRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(NullPointerException.class,
                        () -> CreateListingRequest.builder()
                                .releaseId(1234L)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ////////////////////
    // GetOrderRequest
    ////////////////////

    @Test
    public void getOrderRequest_withInvalidOrderId_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetOrderRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetOrderRequest.builder()
                                .orderId(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ///////////////////////
    // UpdateOrderRequest
    ///////////////////////

    @Test
    public void updateOrderRequest_withInvalidOrderId_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> UpdateOrderRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> UpdateOrderRequest.builder()
                                .orderId(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    /////////////////////
    // GetOrdersRequest
    /////////////////////

    @Test
    public void getOrdersRequest_withValidRequest_shouldPopulateQueryParmaeters() {
        GetOrdersRequest.builder()
                .status(OrderStatus.NEW_ORDER)
                .createdAfter(LocalDateTime.of(2024, 1, 1, 12, 0, 0))
                .createdBefore(LocalDateTime.of(2024, 2, 1, 12, 0, 0))
                .archived(false)
                .sort(GetOrdersRequest.Sort.BUYER)
                .sortOrder(SortOrder.ASC)
                .page(1)
                .perPage(5)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("status", "New+Order"),
                () -> validateQueryParameter("created_after"),
                () -> validateQueryParameter("created_before"),
                () -> validateQueryParameter("archived", "false"),
                () -> validateQueryParameter("sort", "buyer"),
                () -> validateQueryParameter("sort_order", "asc"),
                () -> validateQueryParameter("page", "1"),
                () -> validateQueryParameter("per_page", "5"));
    }

    ////////////////////////////
    // GetOrderMessagesRequest
    ////////////////////////////

    @Test
    public void getOrderMessagesRequest_withValidRequest_shouldPopulateQueryParameters() {
        GetOrderMessagesRequest.builder()
                .orderId("OrderId")
                .page(1)
                .perPage(5)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        assertAll(
                () -> validateQueryParameter("page", "1"),
                () -> validateQueryParameter("per_page", "5"));
    }

    @Test
    public void getOrderMessagesRequest_withInvalidOrderId_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetOrderMessagesRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetOrderMessagesRequest.builder()
                                .orderId(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ///////////////////////////
    // AddOrderMessageRequest
    ///////////////////////////

    @Test
    public void addOrderMessageRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> AddOrderMessageRequest.builder()
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> AddOrderMessageRequest.builder()
                                .orderId(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> AddOrderMessageRequest.builder()
                                .orderId("OrderId")
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    //////////////////
    // GetFeeRequest
    //////////////////

    @Test
    public void getFeeRequest_withInvalidPrice_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetFeeRequest.builder()
                        .price(-1.0D)
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    ///////////////////////////////
    // GetPriceSuggestionsRequest
    ///////////////////////////////

    @Test
    public void getPriceSuggestionsRequest_withInvalidReleaseId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetPriceSuggestionsRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }

    ////////////////////////////////
    // GetReleaseStatisticsRequest
    ////////////////////////////////

    @Test
    public void getReleaseStatisticsRequest_withValidRequest_shouldPopulateQueryParmaeters() {
        GetReleaseStatisticsRequest.builder()
                .releaseId(1234L)
                .currency(Currency.USD)
                .build()
                .populateQueryParameters(mockHttpUrlBuilder);

        validateQueryParameter("curr_abbr", "USD");
    }

    @Test
    public void getReleaseStatisticsRequest_withInvalidReleaseId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetReleaseStatisticsRequest.builder()
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }
}
