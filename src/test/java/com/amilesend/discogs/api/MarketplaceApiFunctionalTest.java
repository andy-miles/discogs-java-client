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

import com.amilesend.client.connection.RequestException;
import com.amilesend.client.connection.ResponseException;
import com.amilesend.discogs.FunctionalTestBase;
import com.amilesend.discogs.model.marketplace.AddOrderMessageRequest;
import com.amilesend.discogs.model.marketplace.AddOrderMessageResponse;
import com.amilesend.discogs.model.marketplace.CreateListingRequest;
import com.amilesend.discogs.model.marketplace.CreateListingResponse;
import com.amilesend.discogs.model.marketplace.DeleteListingRequest;
import com.amilesend.discogs.model.marketplace.GetFeeRequest;
import com.amilesend.discogs.model.marketplace.GetFeeResponse;
import com.amilesend.discogs.model.marketplace.GetInventoryRequest;
import com.amilesend.discogs.model.marketplace.GetInventoryResponse;
import com.amilesend.discogs.model.marketplace.GetListingRequest;
import com.amilesend.discogs.model.marketplace.GetListingResponse;
import com.amilesend.discogs.model.marketplace.GetOrderMessagesRequest;
import com.amilesend.discogs.model.marketplace.GetOrderMessagesResponse;
import com.amilesend.discogs.model.marketplace.GetOrderRequest;
import com.amilesend.discogs.model.marketplace.GetOrderResponse;
import com.amilesend.discogs.model.marketplace.GetOrdersRequest;
import com.amilesend.discogs.model.marketplace.GetOrdersResponse;
import com.amilesend.discogs.model.marketplace.GetPriceSuggestionsRequest;
import com.amilesend.discogs.model.marketplace.GetPriceSuggestionsResponse;
import com.amilesend.discogs.model.marketplace.GetReleaseStatisticsRequest;
import com.amilesend.discogs.model.marketplace.GetReleaseStatisticsResponse;
import com.amilesend.discogs.model.marketplace.UpdateListingRequest;
import com.amilesend.discogs.model.marketplace.UpdateOrderRequest;
import com.amilesend.discogs.model.marketplace.UpdateOrderResponse;
import com.amilesend.discogs.model.marketplace.type.Condition;
import com.amilesend.discogs.model.marketplace.type.OrderStatus;
import com.amilesend.discogs.model.type.Currency;
import com.amilesend.discogs.model.type.ListingStatus;
import com.amilesend.discogs.model.type.SortOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.amilesend.discogs.data.MarketplaceApiDataHelper.Responses.ADD_ORDER_MESSAGE;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.Responses.CREATE_LISTING_RESPONSE;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.Responses.GET_FEE_RESPONSE;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.Responses.GET_INVENTORY_RESPONSE;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.Responses.GET_LISTING_RESPONSE;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.Responses.GET_ORDERS_RESPONSE;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.Responses.GET_ORDER_MESSAGES;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.Responses.GET_PRICE_SUGGESTIONS_RESPONSE;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.Responses.GET_RELEASE_STATISTICS_RESPONSE;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.Responses.ORDER_RESPONSE;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.newAddOrderMessageResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.newCreateListingResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.newGetFeeResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.newGetInventoryResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.newGetListingResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.newGetOrderMessagesResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.newGetOrderResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.newGetOrdersResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.newGetPriceSuggestionsResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.newGetReleaseStatisticsResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataHelper.newUpdateOrderResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataValidator.validateGetInventoryResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataValidator.validateGetOrderMessagesResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataValidator.validateGetOrdersResponse;
import static com.amilesend.discogs.data.MarketplaceApiDataValidator.validateListing;
import static com.amilesend.discogs.data.MarketplaceApiDataValidator.validateOrder;
import static com.amilesend.discogs.data.MarketplaceApiDataValidator.validateOrderMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MarketplaceApiFunctionalTest extends FunctionalTestBase {
    private MarketplaceApi apiUnderTest;

    @BeforeEach
    public void setUpClient() {
        apiUnderTest = getClient().getMarketplaceApi();
    }

    /////////////////
    // getInventory
    /////////////////

    @Test
    public void getInventory_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_INVENTORY_RESPONSE);

        final GetInventoryResponse actual = apiUnderTest.getInventory(
                GetInventoryRequest.builder()
                        .username("SomeUser")
                        .build());

        final GetInventoryResponse expected = newGetInventoryResponse();
        validateGetInventoryResponse(expected, actual);
    }

    @Test
    public void getInventory_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getInventory(
                GetInventoryRequest.builder()
                        .username("SomeUser")
                        .build()));
    }

    @Test
    public void getInventory_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getInventory(
                GetInventoryRequest.builder()
                        .username("SomeUser")
                        .build()));
    }

    @Test
    public void getInventory_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getInventory(null));
    }

    ///////////////
    // getListing
    ///////////////

    @Test
    public void getListing_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_LISTING_RESPONSE);

        final GetListingResponse actual = apiUnderTest.getListing(
                GetListingRequest.builder()
                        .listingId(1265949252L)
                        .build());

        final GetListingResponse expected = newGetListingResponse();
        validateListing(expected, actual);
    }

    @Test
    public void getListing_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getListing(
                GetListingRequest.builder()
                        .listingId(1265949252L)
                        .build()));
    }

    @Test
    public void getListing_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getListing(
                GetListingRequest.builder()
                        .listingId(1265949252L)
                        .build()));
    }

    @Test
    public void getListing_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getListing(null));
    }

    //////////////////
    // updateListing
    //////////////////

    @Test
    public void updateListing_withValidRequest_shouldSucceed() {
        setUpMockResponse(SUCCESS_STATUS_CODE);

        apiUnderTest.updateListing(UpdateListingRequest.builder()
                .listingId(123456789L)
                .releaseId(16274231L)
                .condition(Condition.VERY_GOOD)
                .build());
    }

    @Test
    public void updateListing_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.updateListing(UpdateListingRequest.builder()
                .listingId(123456789L)
                .releaseId(16274231L)
                .condition(Condition.VERY_GOOD)
                .build()));
    }

    @Test
    public void updateListing_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.updateListing(UpdateListingRequest.builder()
                .listingId(123456789L)
                .releaseId(16274231L)
                .condition(Condition.VERY_GOOD)
                .build()));
    }

    @Test
    public void updateListing_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.updateListing(null));
    }

    //////////////////
    // deleteListing
    //////////////////

    @Test
    public void deleteListing_withValidRequest_shouldSucceed() {
        setUpMockResponse(SUCCESS_STATUS_CODE);

        apiUnderTest.deleteListing(DeleteListingRequest.builder()
                .listingId(123456789L)
                .build());
    }

    @Test
    public void deleteListing_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.deleteListing(DeleteListingRequest.builder()
                .listingId(123456789L)
                .build()));
    }

    @Test
    public void deleteListing_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.deleteListing(DeleteListingRequest.builder()
                .listingId(123456789L)
                .build()));
    }

    @Test
    public void deleteListing_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.deleteListing(null));
    }

    //////////////////
    // createListing
    //////////////////

    @Test
    public void createListing_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, CREATE_LISTING_RESPONSE);

        final CreateListingResponse actual = apiUnderTest.createListing(
                CreateListingRequest.builder()
                        .releaseId(234567L)
                        .condition(Condition.VERY_GOOD)
                        .status(ListingStatus.FOR_SALE)
                        .price(9.99D)
                        .build());

        final CreateListingResponse expected = newCreateListingResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void createListing_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.createListing(
                CreateListingRequest.builder()
                        .releaseId(234567L)
                        .condition(Condition.VERY_GOOD)
                        .status(ListingStatus.FOR_SALE)
                        .price(9.99D)
                        .build()));
    }

    @Test
    public void createListing_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.createListing(
                CreateListingRequest.builder()
                        .releaseId(234567L)
                        .condition(Condition.VERY_GOOD)
                        .status(ListingStatus.FOR_SALE)
                        .price(9.99D)
                        .build()));
    }

    @Test
    public void createListing_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.createListing(null));
    }

    /////////////
    // getOrder
    /////////////

    @Test
    public void getOrder_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, ORDER_RESPONSE);

        final GetOrderResponse actual = apiUnderTest.getOrder(
                GetOrderRequest.builder()
                        .orderId("OrderId")
                        .build());

        final GetOrderResponse expected = newGetOrderResponse();
        validateOrder(expected, actual);
    }

    @Test
    public void getOrder_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getOrder(
                GetOrderRequest.builder()
                        .orderId("OrderId")
                        .build()));
    }

    @Test
    public void getOrder_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getOrder(
                GetOrderRequest.builder()
                        .orderId("OrderId")
                        .build()));
    }

    @Test
    public void getOrder_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getOrder(null));
    }

    ////////////////
    // updateOrder
    ////////////////

    @Test
    public void updateOrder_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, ORDER_RESPONSE);

        final UpdateOrderResponse actual = apiUnderTest.updateOrder(
                UpdateOrderRequest.builder()
                        .orderId("OrderId")
                        .build());

        final UpdateOrderResponse expected = newUpdateOrderResponse();
        validateOrder(expected, actual);
    }

    @Test
    public void updateOrder_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.updateOrder(
                UpdateOrderRequest.builder()
                        .orderId("OrderId")
                        .build()));
    }

    @Test
    public void updateOrder_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.updateOrder(
                UpdateOrderRequest.builder()
                        .orderId("OrderId")
                        .build()));
    }

    @Test
    public void updateOrder_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.updateOrder(null));
    }

    //////////////
    // getOrders
    //////////////

    @Test
    public void getOrders_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_ORDERS_RESPONSE);

        final GetOrdersResponse actual = apiUnderTest.getOrders(
                GetOrdersRequest.builder()
                        .status(OrderStatus.NEW_ORDER)
                        .page(1)
                        .perPage(5)
                        .sort(GetOrdersRequest.Sort.BUYER)
                        .sortOrder(SortOrder.ASC)
                        .build());

        final GetOrdersResponse expected = newGetOrdersResponse();
        validateGetOrdersResponse(expected, actual);
    }

    @Test
    public void getOrders_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getOrders(
                GetOrdersRequest.builder()
                        .status(OrderStatus.NEW_ORDER)
                        .page(1)
                        .perPage(5)
                        .sort(GetOrdersRequest.Sort.BUYER)
                        .sortOrder(SortOrder.ASC)
                        .build()));
    }

    @Test
    public void getOrders_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getOrders(
                GetOrdersRequest.builder()
                        .status(OrderStatus.NEW_ORDER)
                        .page(1)
                        .perPage(5)
                        .sort(GetOrdersRequest.Sort.BUYER)
                        .sortOrder(SortOrder.ASC)
                        .build()));
    }

    @Test
    public void getOrders_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getOrders(null));
    }

    /////////////////////
    // getOrderMessages
    /////////////////////

    @Test
    public void getOrderMessages_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_ORDER_MESSAGES);

        final GetOrderMessagesResponse actual = apiUnderTest.getOrderMessages(
                GetOrderMessagesRequest.builder()
                        .orderId("OrderId")
                        .build());

        final GetOrderMessagesResponse expected = newGetOrderMessagesResponse();
        validateGetOrderMessagesResponse(expected, actual);
    }

    @Test
    public void getOrderMessages_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getOrderMessages(
                GetOrderMessagesRequest.builder()
                        .orderId("OrderId")
                        .build()));
    }

    @Test
    public void getOrderMessages_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getOrderMessages(
                GetOrderMessagesRequest.builder()
                        .orderId("OrderId")
                        .build()));
    }

    @Test
    public void getOrderMessages_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getOrderMessages(null));
    }

    ////////////////////
    // addOrderMessage
    ////////////////////

    @Test
    public void addOrderMessage_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, ADD_ORDER_MESSAGE);

        final AddOrderMessageResponse actual = apiUnderTest.addOrderMessage(
                AddOrderMessageRequest.builder()
                        .orderId("Order Id")
                        .message("Order Message")
                        .status("Status")
                        .build());

        final AddOrderMessageResponse expected = newAddOrderMessageResponse();
        validateOrderMessage(expected, actual);
    }

    @Test
    public void addOrderMessage_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.addOrderMessage(
                AddOrderMessageRequest.builder()
                        .orderId("Order Id")
                        .message("Order Message")
                        .status("Status")
                        .build()));
    }

    @Test
    public void addOrderMessage_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.addOrderMessage(
                AddOrderMessageRequest.builder()
                        .orderId("Order Id")
                        .message("Order Message")
                        .status("Status")
                        .build()));
    }

    @Test
    public void addOrderMessage_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.addOrderMessage(null));
    }

    ///////////
    // getFee
    ///////////

    @Test
    public void getFee_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_FEE_RESPONSE);

        final GetFeeResponse actual = apiUnderTest.getFee(
                GetFeeRequest.builder()
                        .currency(Currency.USD)
                        .price(9.99D)
                        .build());

        final GetFeeResponse expected = newGetFeeResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void getFee_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getFee(
                GetFeeRequest.builder()
                        .currency(Currency.USD)
                        .price(9.99D)
                        .build()));
    }

    @Test
    public void getFee_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getFee(
                GetFeeRequest.builder()
                        .currency(Currency.USD)
                        .price(9.99D)
                        .build()));
    }

    @Test
    public void getFee_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getFee(null));
    }

    ////////////////////////
    // getPriceSuggestions
    ////////////////////////

    @Test
    public void getPriceSuggestions_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_PRICE_SUGGESTIONS_RESPONSE);

        final GetPriceSuggestionsResponse actual = apiUnderTest.getPriceSuggestions(
                GetPriceSuggestionsRequest.builder()
                        .releaseId(1234L)
                        .build());

        final GetPriceSuggestionsResponse expected = newGetPriceSuggestionsResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void getPriceSuggestions_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getPriceSuggestions(
                GetPriceSuggestionsRequest.builder()
                        .releaseId(1234L)
                        .build()));
    }

    @Test
    public void getPriceSuggestions_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getPriceSuggestions(
                GetPriceSuggestionsRequest.builder()
                        .releaseId(1234L)
                        .build()));
    }

    @Test
    public void getPriceSuggestions_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getPriceSuggestions(null));
    }

    /////////////////////////
    // getReleaseStatistics
    /////////////////////////

    @Test
    public void getReleaseStatistics_withValidRequest_shouldReturnResponse() {
        setUpMockResponse(SUCCESS_STATUS_CODE, GET_RELEASE_STATISTICS_RESPONSE);

        final GetReleaseStatisticsResponse actual = apiUnderTest.getReleaseStatistics(
                GetReleaseStatisticsRequest.builder()
                        .releaseId(1234L)
                        .build());

        final GetReleaseStatisticsResponse expected = newGetReleaseStatisticsResponse();
        assertEquals(expected, actual);
    }

    @Test
    public void getReleaseStatistics_withRequestException_shouldThrowException() {
        setUpMockResponse(USER_ERROR_CODE);

        assertThrows(RequestException.class, () -> apiUnderTest.getReleaseStatistics(
                GetReleaseStatisticsRequest.builder()
                        .releaseId(1234L)
                        .build()));
    }

    @Test
    public void getReleaseStatistics_withResponseException_shouldThrowException() {
        setUpMockResponse(SERVICE_ERROR_CODE);

        assertThrows(ResponseException.class, () -> apiUnderTest.getReleaseStatistics(
                GetReleaseStatisticsRequest.builder()
                        .releaseId(1234L)
                        .build()));
    }

    @Test
    public void getReleaseStatistics_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> apiUnderTest.getReleaseStatistics(null));
    }
}
