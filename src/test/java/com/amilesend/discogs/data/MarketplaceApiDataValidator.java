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
package com.amilesend.discogs.data;

import com.amilesend.discogs.model.marketplace.GetInventoryResponse;
import com.amilesend.discogs.model.marketplace.GetOrderMessagesResponse;
import com.amilesend.discogs.model.marketplace.GetOrdersResponse;
import com.amilesend.discogs.model.marketplace.type.Buyer;
import com.amilesend.discogs.model.marketplace.type.Listing;
import com.amilesend.discogs.model.marketplace.type.ListingRelease;
import com.amilesend.discogs.model.marketplace.type.Order;
import com.amilesend.discogs.model.marketplace.type.OrderItem;
import com.amilesend.discogs.model.marketplace.type.OrderMessage;
import com.amilesend.discogs.model.marketplace.type.OrderRefund;
import com.amilesend.discogs.model.marketplace.type.Seller;
import lombok.experimental.UtilityClass;

import java.util.Objects;

import static com.amilesend.discogs.data.DataValidatorHelper.validateListOf;
import static com.amilesend.discogs.data.DataValidatorHelper.validatePaginatedResponseBase;
import static com.amilesend.discogs.data.DataValidatorHelper.validateResource;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UtilityClass
public class MarketplaceApiDataValidator {

    /////////////////////////
    // GetInventoryResponse
    /////////////////////////

    public static void validateGetInventoryResponse(
            final GetInventoryResponse expected,
            final GetInventoryResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validatePaginatedResponseBase(expected, actual),
                () -> validateListOf(
                        expected.getListings(),
                        actual.getListings(),
                        MarketplaceApiDataValidator::validateListing));
    }


    public static void validateListing(final Listing expected, final Listing actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getStatus(), actual.getStatus()),
                () -> assertEquals(expected.getPrice(), actual.getPrice()),
                () -> assertEquals(expected.getOriginalPrice(), actual.getOriginalPrice()),
                () -> assertEquals(expected.getAllowOffers(), actual.getAllowOffers()),
                () -> assertEquals(expected.getOfferSubmitted(), actual.getOfferSubmitted()),
                () -> assertEquals(expected.getSleeveCondition(), actual.getSleeveCondition()),
                () -> assertEquals(expected.getCondition(), actual.getCondition()),
                () -> assertEquals(expected.getPosted(), actual.getPosted()),
                () -> assertEquals(expected.getShipsFrom(), actual.getShipsFrom()),
                () -> assertEquals(expected.getUri(), actual.getUri()),
                () -> assertEquals(expected.getComments(), actual.getComments()),
                () -> validateSeller(expected.getSeller(), actual.getSeller()),
                () -> validateListingRelease(expected.getRelease(), actual.getRelease()),
                () -> assertEquals(expected.getAudio(), actual.getAudio()),
                () -> assertEquals(expected.getWeight(), actual.getWeight()),
                () -> assertEquals(expected.getFormatQuantity(), actual.getFormatQuantity()),
                () -> assertEquals(expected.getExternalId(), actual.getExternalId()),
                () -> assertEquals(expected.getLocation(), actual.getLocation()),
                () -> assertEquals(expected.getQuantity(), actual.getQuantity()),
                () -> assertEquals(expected.getInCart(), actual.getInCart()));
    }

    private static void validateSeller(final Seller expected, final Seller actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getUsername(), actual.getUsername()),
                () -> assertEquals(expected.getAvatarUrl(), actual.getAvatarUrl()),
                () -> assertEquals(expected.getUrl(), actual.getUrl()),
                () -> assertEquals(expected.getShipping(), actual.getShipping()),
                () -> assertEquals(expected.getPayment(), actual.getPayment()),
                () -> assertEquals(expected.getStats(), actual.getStats()),
                () -> assertEquals(expected.getMinOrderTotal(), actual.getMinOrderTotal()),
                () -> assertEquals(expected.getHtmlUrl(), actual.getHtmlUrl()),
                () -> assertEquals(expected.getUid(), actual.getUid()));
    }

    private static void validateListingRelease(final ListingRelease expected, final ListingRelease actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getCatalogNumber(), actual.getCatalogNumber()),
                () -> assertEquals(expected.getYear(), actual.getYear()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> assertEquals(expected.getArtist(), actual.getArtist()),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> assertEquals(expected.getFormat(), actual.getFormat()),
                () -> assertEquals(expected.getThumbnail(), actual.getThumbnail()),
                () -> assertEquals(expected.getLabel(), actual.getLabel()),
                () -> assertEquals(expected.getStats(), actual.getStats()),
                () -> assertEquals(expected.getImages(), actual.getImages()));
    }

    /////////////////////
    // GetOrderResponse
    /////////////////////

    public static void validateOrder(final Order expected, final Order actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getMessagesUrl(), actual.getMessagesUrl()),
                () -> assertEquals(expected.getUri(), actual.getUri()),
                () -> assertEquals(expected.getStatus(), actual.getStatus()),
                () -> assertEquals(expected.getNextStatus(), actual.getNextStatus()),
                () -> assertEquals(expected.getFee(), actual.getFee()),
                () -> assertEquals(expected.getCreated(), actual.getCreated()),
                () -> validateListOf(
                        expected.getItems(),
                        actual.getItems(),
                        MarketplaceApiDataValidator::validateOrderItem),
                () -> assertEquals(expected.getShipping(), actual.getShipping()),
                () -> assertEquals(expected.getShippingAddress(), actual.getShippingAddress()),
                () -> assertEquals(expected.getAdditionalInstructions(), actual.getAdditionalInstructions()),
                () -> assertEquals(expected.getArchived(), actual.getArchived()),
                () -> validateSeller(expected.getSeller(), actual.getSeller()),
                () -> assertEquals(expected.getLastActivity(), actual.getLastActivity()),
                () -> validateBuyer(expected.getBuyer(), actual.getBuyer()),
                () -> assertEquals(expected.getTotal(), actual.getTotal()));
    }

    private static void validateBuyer(final Buyer expected, final Buyer actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getUsername(), actual.getUsername()));
    }

    private static void validateOrderItem(final OrderItem expected, final OrderItem actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> validateRelease(expected.getRelease(), actual.getRelease()),
                () -> assertEquals(expected.getPrice(), actual.getPrice()));
    }

    private static void validateRelease(final OrderItem.Release expected, final OrderItem.Release actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()));
    }

    //////////////////////
    // GetOrdersResponse
    //////////////////////

    public static void validateGetOrdersResponse(final GetOrdersResponse expected, final GetOrdersResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateListOf(
                        expected.getOrders(),
                        actual.getOrders(),
                        MarketplaceApiDataValidator::validateOrder),
                () -> assertEquals(expected.getPagination(), actual.getPagination()));
    }

    /////////////////////////////
    // GetOrderMessagesResponse
    /////////////////////////////

    public static void validateGetOrderMessagesResponse(
            final GetOrderMessagesResponse expected,
            final GetOrderMessagesResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validatePaginatedResponseBase(expected, actual),
                () -> validateListOf(
                        expected.getMessages(),
                        actual.getMessages(),
                        MarketplaceApiDataValidator::validateOrderMessage));
    }

    public static void validateOrderMessage(final OrderMessage expected, final OrderMessage actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateOrderRefund(expected.getRefund(), actual.getRefund()),
                () -> assertEquals(expected.getTimestamp(), actual.getTimestamp()),
                () -> assertEquals(expected.getMessage(), actual.getMessage()),
                () -> assertEquals(expected.getType() , actual.getType()),
                () -> validateResource(expected.getOrder(), actual.getOrder()),
                () -> assertEquals(expected.getSubject(), actual.getSubject()));
    }

    private static void validateOrderRefund(final OrderRefund expected, final OrderRefund actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> assertEquals(expected.getAmount(), actual.getAmount()),
                () -> validateResource(expected.getOrder(), actual.getOrder()));
    }
}
