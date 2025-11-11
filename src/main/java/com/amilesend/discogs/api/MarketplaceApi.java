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
package com.amilesend.discogs.api;

import com.amilesend.client.parse.parser.MapParser;
import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.model.Api;
import com.amilesend.discogs.model.AuthenticationOptional;
import com.amilesend.discogs.model.AuthenticationRequired;
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
import com.amilesend.discogs.model.marketplace.type.Price;
import com.amilesend.discogs.model.type.Currency;
import lombok.NonNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * The Discogs Marketplace API.
 * <br/>
 * <a href="https://www.discogs.com/developers#page:marketplace">API Documentation</a>
 *
 * @see ApiBase
 */
@Api
public class MarketplaceApi extends ApiBase {
    private static final String USERS_API_PATH = "/users/";
    private static final String MARKETPLACE_API_PATH = "/marketplace";
    private static final String LISTINGS_SUB_API_PATH = "/listings/";
    private static final String ORDERS_SUB_API_PATH = "/orders/";
    private static final String FEE_SUB_API_PATH = "/fee/";

    /**
     * Creates a new {@code MarketplaceApi} object.
     *
     * @param connection the underlying client connection
     */
    public MarketplaceApi(final DiscogsConnection connection) {
        super(connection);
    }

    /**
     * Gets the list of listing for a user's inventory.
     *
     * @param request the request
     * @return the response
     * @see GetInventoryRequest
     * @see GetInventoryResponse
     */
    public GetInventoryResponse getInventory(@NonNull final GetInventoryRequest request) {
        final String subPath = new StringBuilder(USERS_API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append("/inventory")
                .toString();
        return executeGet(subPath, request, GetInventoryResponse.class);
    }

    /**
     * Gets a listing.
     *
     * @param request the request
     * @return the response
     * @see GetListingRequest
     * @see GetListingResponse
     */
    public GetListingResponse getListing(@NonNull final GetListingRequest request) {
        final String subPath = new StringBuilder(MARKETPLACE_API_PATH)
                .append(LISTINGS_SUB_API_PATH)
                .append(request.getListingId())
                .toString();
        return executeGet(subPath, request, GetListingResponse.class);
    }

    /**
     * Updates a listing. Note: Must be authenticated as the listing owner.
     *
     * @param request the request
     * @see UpdateListingRequest
     */
    @AuthenticationRequired
    public void updateListing(@NonNull final UpdateListingRequest request) {
        final String subPath = new StringBuilder(MARKETPLACE_API_PATH)
                .append(LISTINGS_SUB_API_PATH)
                .append(request.getListingId())
                .toString();
        executePost(subPath, request);
    }

    /**
     * Deletes a listing. Note: Must be authenticated as the listing owner.
     *
     * @param request the request
     */
    @AuthenticationRequired
    public void deleteListing(@NonNull final DeleteListingRequest request) {
        final String subPath = new StringBuilder(MARKETPLACE_API_PATH)
                .append(LISTINGS_SUB_API_PATH)
                .append(request.getListingId())
                .toString();
        executeDelete(subPath, request);
    }

    /**
     * Creates a new listing. Note: Must be authenticated as the listing owner.
     *
     * @param request the request that describes the new listing
     * @return the response containing the listing identifier
     * @see CreateListingRequest
     * @see CreateListingResponse
     */
    @AuthenticationRequired
    public CreateListingResponse createListing(@NonNull final CreateListingRequest request) {
        final String subPath = new StringBuilder(MARKETPLACE_API_PATH)
                .append("/listings")
                .toString();
        return executePost(subPath, request, CreateListingResponse.class);
    }

    /**
     * Gets information for a specific order. Note: Must be authenticated as the seller.
     *
     * @param request the request
     * @return the response
     * @see GetOrderRequest
     * @see GetOrderResponse
     */
    @AuthenticationRequired
    public GetOrderResponse getOrder(@NonNull final GetOrderRequest request) {
        final String subPath = new StringBuilder(MARKETPLACE_API_PATH)
                .append(ORDERS_SUB_API_PATH)
                .append(URLEncoder.encode(request.getOrderId(), StandardCharsets.UTF_8))
                .toString();
        return executeGet(subPath, request, GetOrderResponse.class);
    }

    /**
     * Updates an order. Note: Must be authenticated as the seller.
     *
     * @param request the request with the attributes to update
     * @return the response
     * @see UpdateOrderRequest
     * @see UpdateOrderResponse
     */
    @AuthenticationRequired
    public UpdateOrderResponse updateOrder(@NonNull final UpdateOrderRequest request) {
        final String subPath = new StringBuilder(MARKETPLACE_API_PATH)
                .append(ORDERS_SUB_API_PATH)
                .append(URLEncoder.encode(request.getOrderId(), StandardCharsets.UTF_8))
                .toString();
        return executePost(subPath, request, UpdateOrderResponse.class);
    }

    /**
     * Gets the paginated list of orders for the authenticated user.
     *
     * @param request the request
     * @return the paginated list of orders
     * @see GetOrdersRequest
     * @see GetOrdersResponse
     */
    @AuthenticationRequired
    public GetOrdersResponse getOrders(@NonNull final GetOrdersRequest request) {
        final String subPath = new StringBuilder(MARKETPLACE_API_PATH)
                .append("/orders")
                .toString();
        return executeGet(subPath, request, GetOrdersResponse.class);
    }

    /**
     * Gets the paginated list of messages associated with an order. Note: Must be authenticated as the seller.
     *
     * @param request the request
     * @return the paginated list of messages.
     * @see GetOrderMessagesRequest
     * @see GetOrderMessagesResponse
     */
    @AuthenticationRequired
    public GetOrderMessagesResponse getOrderMessages(@NonNull final GetOrderMessagesRequest request) {
        final String subPath  = new StringBuilder(MARKETPLACE_API_PATH)
                .append(ORDERS_SUB_API_PATH)
                .append(URLEncoder.encode(request.getOrderId(), StandardCharsets.UTF_8))
                .append("/messages")
                .toString();
        return executeGet(subPath, request, GetOrderMessagesResponse.class);
    }

    /**
     * Adds a message to an order: Must be authenticated as the seller.
     *
     * @param request the request
     * @return the response
     * @see AddOrderMessageRequest
     * @see AddOrderMessageResponse
     */
    @AuthenticationRequired
    public AddOrderMessageResponse addOrderMessage(@NonNull final AddOrderMessageRequest request) {
        final String subPath = new StringBuilder(MARKETPLACE_API_PATH)
                .append(ORDERS_SUB_API_PATH)
                .append(URLEncoder.encode(request.getOrderId(), StandardCharsets.UTF_8))
                .append("/messages")
                .toString();
        return executePost(subPath, request, AddOrderMessageResponse.class);
    }

    /**
     * Gets the fee when selling an item at a given price.
     *
     * @param request the request
     * @return the response
     * @see GetFeeRequest
     * @see GetFeeResponse
     */
    @AuthenticationRequired
    public GetFeeResponse getFee(@NonNull final GetFeeRequest request) {
        final StringBuilder subPathBuilder = new StringBuilder(MARKETPLACE_API_PATH)
                .append(FEE_SUB_API_PATH)
                .append(request.getPrice());
        final Currency currency = request.getCurrency();
        final String subPath = Objects.nonNull(currency)
                ? subPathBuilder.append("/").append(currency).toString()
                : subPathBuilder.toString();
        return executeGet(subPath, request, GetFeeResponse.class);
    }

    /**
     * Gets a map of prices suggestions foa given release.
     *
     * @param request the request
     * @return the response
     * @see GetPriceSuggestionsRequest
     * @see GetPriceSuggestionsResponse
     */
    @AuthenticationRequired
    public GetPriceSuggestionsResponse getPriceSuggestions(@NonNull final GetPriceSuggestionsRequest request) {
        final String subPath = new StringBuilder(MARKETPLACE_API_PATH)
                .append("/price_suggestions/")
                .append(request.getReleaseId())
                .toString();

        final DiscogsConnection connection = getConnection();
        final Map<Condition, Price> prices =  connection.execute(
                connection.newRequestBuilder()
                        .url(buildHttpUrl(subPath, request))
                        .build(),
                new MapParser<>(Condition.class, Price.class));

        return GetPriceSuggestionsResponse.builder()
                .priceSuggestions(prices)
                .build();
    }

    /**
     * Gets the sales statistics for a release. Note: Authentication is optional. Authenticated users will have the
     * currency expressed by their configured preference. Unauthenticated users will have prices reflected in
     * {@link Currency#USD}.
     *
     * @param request the request
     * @return the response
     * @see GetReleaseStatisticsRequest
     * @see GetReleaseStatisticsResponse
     */
    @AuthenticationOptional
    public GetReleaseStatisticsResponse getReleaseStatistics(@NonNull final GetReleaseStatisticsRequest request) {
        final String subPath = new StringBuilder(MARKETPLACE_API_PATH)
                .append("/stats/")
                .append(request.getReleaseId())
                .toString();
        return executeGet(subPath, request, GetReleaseStatisticsResponse.class);
    }
}
