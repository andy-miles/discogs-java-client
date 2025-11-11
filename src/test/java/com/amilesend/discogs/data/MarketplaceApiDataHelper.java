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

import com.amilesend.discogs.model.PaginatedResponseBase;
import com.amilesend.discogs.model.database.type.Image;
import com.amilesend.discogs.model.database.type.Stat;
import com.amilesend.discogs.model.database.type.Stats;
import com.amilesend.discogs.model.marketplace.AddOrderMessageResponse;
import com.amilesend.discogs.model.marketplace.CreateListingResponse;
import com.amilesend.discogs.model.marketplace.GetFeeResponse;
import com.amilesend.discogs.model.marketplace.GetInventoryResponse;
import com.amilesend.discogs.model.marketplace.GetListingResponse;
import com.amilesend.discogs.model.marketplace.GetOrderMessagesResponse;
import com.amilesend.discogs.model.marketplace.GetOrderResponse;
import com.amilesend.discogs.model.marketplace.GetOrdersResponse;
import com.amilesend.discogs.model.marketplace.GetPriceSuggestionsResponse;
import com.amilesend.discogs.model.marketplace.GetReleaseStatisticsResponse;
import com.amilesend.discogs.model.marketplace.UpdateOrderResponse;
import com.amilesend.discogs.model.marketplace.type.Buyer;
import com.amilesend.discogs.model.marketplace.type.Condition;
import com.amilesend.discogs.model.marketplace.type.Listing;
import com.amilesend.discogs.model.marketplace.type.ListingRelease;
import com.amilesend.discogs.model.marketplace.type.Order;
import com.amilesend.discogs.model.marketplace.type.OrderItem;
import com.amilesend.discogs.model.marketplace.type.OrderMessage;
import com.amilesend.discogs.model.marketplace.type.OrderReference;
import com.amilesend.discogs.model.marketplace.type.OrderRefund;
import com.amilesend.discogs.model.marketplace.type.OrderStatus;
import com.amilesend.discogs.model.marketplace.type.OriginalPrice;
import com.amilesend.discogs.model.marketplace.type.Price;
import com.amilesend.discogs.model.marketplace.type.Seller;
import com.amilesend.discogs.model.marketplace.type.SellerStats;
import com.amilesend.discogs.model.marketplace.type.ShippingChargeAmount;
import com.amilesend.discogs.model.marketplace.type.SleeveCondition;
import com.amilesend.discogs.model.type.Currency;
import com.amilesend.discogs.model.type.ListingStatus;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@UtilityClass
public class MarketplaceApiDataHelper {

    /////////////////////////
    // GetInventoryResponse
    /////////////////////////

    public static GetInventoryResponse newGetInventoryResponse() {
        return GetInventoryResponse.builder()
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .page(1)
                        .pages(1)
                        .perPage(50)
                        .items(2)
                        .urls(Collections.emptyMap())
                        .build())
                .listings(List.of(
                        Listing.builder()
                                .id(3526621535L)
                                .resourceUrl("https://api.discogs.com/marketplace/listings/3526621535")
                                .uri("https://www.discogs.com/sell/item/3526621535")
                                .status(ListingStatus.FOR_SALE)
                                .condition(Condition.NEAR_MINT)
                                .sleeveCondition(SleeveCondition.VERY_GOOD_PLUS)
                                .comments("Case has some scratches. Nothing major")
                                .shipsFrom("United States")
                                .posted(LocalDateTime.of(2025,4, 13, 20, 16, 17))
                                .allowOffers(true)
                                .offerSubmitted(false)
                                .audio(false)
                                .price(Price.builder()
                                        .value(14.99D)
                                        .currency(Currency.USD)
                                        .build())
                                .originalPrice(OriginalPrice.builder()
                                        .currency(Currency.USD)
                                        .currencyId(1)
                                        .formatted("$14.99")
                                        .value(14.99D)
                                        .build())
                                .seller(newSeller())
                                .release(ListingRelease.builder()
                                        .id(416300L)
                                        .resourceUrl("https://api.discogs.com/releases/416300")
                                        .thumbnail("https://i.discogs.com/akTxx56BFp74nutI6vMlJTmq1KUHCZFluQirvuM7Btc/LTU0MDkuanBlZw.jpeg")
                                        .description("Live - Selling The Drama (CD, Maxi)")
                                        .images(List.of(Image.builder()
                                                        .type("primary")
                                                        .uri("https://i.discogs.com/bMrMDvPk2jkFNMfDp7i4JMx6njkE-TiCIJLvA_eN90s/LTU0MDkuanBlZw.jpeg")
                                                        .resourceUrl("https://i.discogs.com/bMrMDvPk2jkFNMfDp7i4JMx6njkE-TiCIJLvA_eN90s/LTU0MDkuanBlZw.jpeg")
                                                        .uri150("https://i.discogs.com/akTxx56BFp74nutI6vMlJTmq1KUHCZFluQirvuM7Btc/LTU0MDkuanBlZw.jpeg")
                                                        .width(600)
                                                        .height(518)
                                                        .build()))
                                        .artist("Live")
                                        .format("CD, Maxi")
                                        .title("Selling The Drama")
                                        .year(1994)
                                        .label("Radioactive")
                                        .catalogNumber("RAD 31974")
                                        .stats(Stats.builder()
                                                .community(Stat.builder()
                                                        .inWantList(27)
                                                        .inCollection(209)
                                                        .build())
                                                .user(Stat.builder()
                                                        .inWantList(0)
                                                        .inCollection(0)
                                                        .build())
                                                .build())
                                        .build())
                                .inCart(false)
                                .build(),
                        Listing.builder()
                                .id(3458734932L)
                                .resourceUrl("https://api.discogs.com/marketplace/listings/3458734932")
                                .uri("https://www.discogs.com/sell/item/3458734932")
                                .status(ListingStatus.FOR_SALE)
                                .condition(Condition.VERY_GOOD_PLUS)
                                .sleeveCondition(SleeveCondition.VERY_GOOD)
                                .comments("Has hype sticker")
                                .shipsFrom("United States")
                                .posted(LocalDateTime.of(2025, 4, 13, 20, 16, 17))
                                .allowOffers(false)
                                .offerSubmitted(false)
                                .audio(false)
                                .price(Price.builder()
                                        .value(16.99D)
                                        .currency(Currency.USD)
                                        .build())
                                .originalPrice(OriginalPrice.builder()
                                        .currency(Currency.USD)
                                        .currencyId(1)
                                        .formatted("$16.99")
                                        .value(16.99D)
                                        .build())
                                .seller(newSeller())
                                .release(ListingRelease.builder()
                                        .id(488180L)
                                        .resourceUrl("https://api.discogs.com/releases/488180")
                                        .thumbnail("https://i.discogs.com/1uu4OLLHoEY7HSA76nAxSeyOHnfrABT7k421w33Xh3E/LmpwZw.jpeg")
                                        .description("Too Short - Shorty The Player (LP, Promo)")
                                        .images(List.of(
                                                Image.builder()
                                                        .type("primary")
                                                        .uri("https://i.discogs.com/jCQGgYBW3XhQSkMkcLoNiWcIvBo6TTjrNufHsWaFV_U/LmpwZw.jpeg")
                                                        .resourceUrl("https://i.discogs.com/jCQGgYBW3XhQSkMkcLoNiWcIvBo6TTjrNufHsWaFV_U/LmpwZw.jpeg")
                                                        .uri150("https://i.discogs.com/1uu4OLLHoEY7HSA76nAxSeyOHnfrABT7k421w33Xh3E/LmpwZw.jpeg")
                                                        .width(500)
                                                        .height(245)
                                                        .build(),
                                                Image.builder()
                                                        .type("secondary")
                                                        .uri("https://i.discogs.com/30npS3V4IYqkV3KKHF8HWUCMJ2UdGe27UARl24LsnBM/LmpwZw.jpeg")
                                                        .resourceUrl("https://i.discogs.com/30npS3V4IYqkV3KKHF8HWUCMJ2UdGe27UARl24LsnBM/LmpwZw.jpeg")
                                                        .uri150("https://i.discogs.com/4kzSCvjhf5YwUKuzD2RCQAl4tlFDzDP1ZVmANz3GFsQ/LmpwZw.jpeg")
                                                        .width(400)
                                                        .height(394)
                                                        .build()))
                                        .artist("Too Short")
                                        .format("LP, Promo")
                                        .resourceUrl("https://api.discogs.com/releases/488180")
                                        .title("Shorty The Player")
                                        .year(1992)
                                        .label("Jive, Dangerous Music")
                                        .catalogNumber("JDJ-41505-1")
                                        .stats(Stats.builder()
                                                .community(Stat.builder()
                                                        .inWantList(160)
                                                        .inCollection(35)
                                                        .build())
                                                .user(Stat.builder()
                                                        .inWantList(0)
                                                        .inCollection(0)
                                                        .build())
                                                .build())
                                        .build())
                                .inCart(false)
                                .build()))
                .build();
    }

    private static Seller newSeller() {
        return Seller.builder()
                .id(1234567L)
                .resourceUrl("https://api.discogs.com/users/SomeSeller")
                .username("SomeSeller")
                .avatarUrl("https://i.discogs.com/W9m0QfAHkYbBd2jfmD5CiNGiiEgbCOE5gL7CMSVzWcw/cGVn.jpeg")
                .stats(SellerStats.builder()
                        .rating("100.0")
                        .stars(5.0D)
                        .total(50)
                        .build())
                .minOrderTotal(9.99D)
                .htmlUrl("https://www.discogs.com/user/SomeSeller")
                .uid(1234567L)
                .url("https://api.discogs.com/users/SomeSeller")
                .payment("PayPal Commerce")
                .shipping("12\" records $6.50 1-2 records. $7.50 3 records and up.  Shipping in the USA.\r\nNo international shipping Sorry\r\nI cannot guarantee the condition of sealed records. ")
                .build();
    }

    ///////////////////////
    // GetListingResponse
    ///////////////////////

    public static GetListingResponse newGetListingResponse() {
        return GetListingResponse.builder()
                .id(1265949252L)
                .resourceUrl("https://api.discogs.com/marketplace/listings/1265949252")
                .uri("https://www.discogs.com/sell/item/1265949252")
                .status(ListingStatus.FOR_SALE)
                .condition(Condition.MINT)
                .sleeveCondition(SleeveCondition.MINT)
                .comments("[FULL STOCK SALE - 15% on all items - Discogs only! - Prices already discounted!]    sealed")
                .shipsFrom("Germany")
                .posted(LocalDateTime.of(2025, 8,14, 7, 37, 55))
                .allowOffers(false)
                .offerSubmitted(false)
                .audio(false)
                .price(Price.builder()
                        .value(89.34117647058824D)
                        .currency(Currency.USD)
                        .build())
                .originalPrice(OriginalPrice.builder()
                        .currency(Currency.EUR)
                        .currencyId(3)
                        .formatted("€75.94")
                        .value(75.94D)
                        .converted(OriginalPrice.builder()
                                .currency(Currency.USD)
                                .currencyId(1)
                                .formatted("$89.34")
                                .value(89.34D)
                                .build())
                        .build())
                .seller(Seller.builder()
                        .id(1900186L)
                        .resourceUrl("https://api.discogs.com/users/oldschool-berlin")
                        .username("oldschool-berlin")
                        .avatarUrl("https://i.discogs.com/7-XRW885Cc5QAfGyuS_X5Eu3BDWZdfF-8VWS-aRUxZQ/cGVn.jpeg")
                        .stats(SellerStats.builder()
                                .rating("99.8")
                                .stars(4.5D)
                                .total(15571)
                                .build())
                        .minOrderTotal(0.0D)
                        .htmlUrl("https://www.discogs.com/user/oldschool-berlin")
                        .uid(1900186L)
                        .url("https://api.discogs.com/users/oldschool-berlin")
                        .payment("PayPal Commerce")
                        .shipping("Fast shipping")
                        .build())
                .release(ListingRelease.builder()
                        .id(16274231L)
                        .resourceUrl("https://api.discogs.com/releases/16274231")
                        .thumbnail("https://i.discogs.com/Jg7s84HvUBS5HTNCjlS0_TwhiIJchE43m6bACWjm4Pw/OTctMjgwMS5qcGVn.jpeg")
                        .description("Dream Theater - Distant Memories - Live In London (Box, Ltd + 4xLP, Album, Sil + 3xCD, Album)")
                        .images(List.of(
                                Image.builder()
                                        .type("primary")
                                        .uri("https://i.discogs.com/rwTRtdTyQQ7Q9JlHCS-uQ7KXCQQkGUCraspSlUKNyGM/OTctMjgwMS5qcGVn.jpeg")
                                        .resourceUrl("https://i.discogs.com/rwTRtdTyQQ7Q9JlHCS-uQ7KXCQQkGUCraspSlUKNyGM/OTctMjgwMS5qcGVn.jpeg")
                                        .uri150("https://i.discogs.com/Jg7s84HvUBS5HTNCjlS0_TwhiIJchE43m6bACWjm4Pw/OTctMjgwMS5qcGVn.jpeg")
                                        .width(444)
                                        .height(443)
                                        .build(),
                                Image.builder()
                                        .type("secondary")
                                        .uri("https://i.discogs.com/feVTHuiOZCHKwdcObtZ7Wh6Mc9X34331ufQR5UWmX7E/MjMtNTkyNy5qcGVn.jpeg")
                                        .resourceUrl("https://i.discogs.com/feVTHuiOZCHKwdcObtZ7Wh6Mc9X34331ufQR5UWmX7E/MjMtNTkyNy5qcGVn.jpeg")
                                        .uri150("https://i.discogs.com/WgxF7Qd9mUuB7kL7i15vfxOobz3niUIK2jTPkCzc3Fk/MjMtNTkyNy5qcGVn.jpeg")
                                        .width(470)
                                        .height(458)
                                        .build()))
                        .artist("Dream Theater")
                        .format("Box, Ltd + 4xLP, Album, Sil + 3xCD, Album")
                        .title("Distant Memories - Live In London")
                        .year(2020)
                        .label("Inside Out Music, Sony Music")
                        .catalogNumber("IOMLP 569, 19439774561")
                        .stats(Stats.builder()
                                .community(Stat.builder()
                                        .inWantList(41)
                                        .inCollection(94)
                                        .build())
                                .user(Stat.builder()
                                        .inWantList(0)
                                        .inCollection(0)
                                        .build())
                                .build())
                        .build())
                .inCart(false)
                .build();
    }

    //////////////////////////
    // CreateListingResponse
    //////////////////////////

    public static CreateListingResponse newCreateListingResponse() {
        return CreateListingResponse.builder()
                .listingId(123456789L)
                .resourceUrl("https://api.discogs.com/marketplace/listings/123456789")
                .build();
    }

    /////////////////////
    // GetOrderResponse
    /////////////////////

    public static GetOrderResponse newGetOrderResponse() {
        return GetOrderResponse.builder()
                .id("OrderId")
                .resourceUrl("https://api.discogs.com/marketplace/orders/OrderId")
                .messagesUrl("https://api.discogs.com/marketplace/orders/OrderId/messages")
                .uri("https://www.discogs.com/marketplace/orders/OrderId")
                .status(OrderStatus.NEW_ORDER)
                .nextStatus(List.of(OrderStatus.IN_PROGRESS))
                .fee(Price.builder()
                        .value(9.99D)
                        .currency(Currency.USD)
                        .build())
                .created(LocalDateTime.of(2024, 1, 12, 14, 30, 30))
                .items(List.of(
                        OrderItem.builder()
                                .id(123456L)
                                .release(OrderItem.Release.builder()
                                        .id(234567L)
                                        .description("Release Description")
                                        .build())
                                .price(Price.builder()
                                        .value(9.99D)
                                        .currency(Currency.USD)
                                        .build())

                                .build()))
                .shipping(ShippingChargeAmount.builder()
                        .value(2.99D)
                        .currency(Currency.USD)
                        .method("USPS")
                        .build())
                .shippingAddress("Shipping Address Value")
                .additionalInstructions("Additional instructions")
                .archived(false)
                .seller(newSeller())
                .lastActivity(LocalDateTime.of(2024, 1, 12, 14, 30 ,30))
                .buyer(Buyer.builder()
                        .id(98764L)
                        .resourceUrl("https://wwww.discogs.com/user/98764")
                        .username("Username")
                        .build())
                .total(Price.builder()
                        .value(12.98D)
                        .currency(Currency.USD)
                        .build())
                .build();
    }

    ////////////////////////
    // UpdateOrderResponse
    ////////////////////////

    public static UpdateOrderResponse newUpdateOrderResponse() {
        return UpdateOrderResponse.builder()
                .id("OrderId")
                .resourceUrl("https://api.discogs.com/marketplace/orders/OrderId")
                .messagesUrl("https://api.discogs.com/marketplace/orders/OrderId/messages")
                .uri("https://www.discogs.com/marketplace/orders/OrderId")
                .status(OrderStatus.NEW_ORDER)
                .nextStatus(List.of(OrderStatus.IN_PROGRESS))
                .fee(Price.builder()
                        .value(9.99D)
                        .currency(Currency.USD)
                        .build())
                .created(LocalDateTime.of(2024, 1, 12, 14, 30, 30))
                .items(List.of(
                        OrderItem.builder()
                                .id(123456L)
                                .release(OrderItem.Release.builder()
                                        .id(234567L)
                                        .description("Release Description")
                                        .build())
                                .price(Price.builder()
                                        .value(9.99D)
                                        .currency(Currency.USD)
                                        .build())

                                .build()))
                .shipping(ShippingChargeAmount.builder()
                        .value(2.99D)
                        .currency(Currency.USD)
                        .method("USPS")
                        .build())
                .shippingAddress("Shipping Address Value")
                .additionalInstructions("Additional instructions")
                .archived(false)
                .seller(newSeller())
                .lastActivity(LocalDateTime.of(2024, 1, 12, 14, 30 ,30))
                .buyer(Buyer.builder()
                        .id(98764L)
                        .resourceUrl("https://wwww.discogs.com/user/98764")
                        .username("Username")
                        .build())
                .total(Price.builder()
                        .value(12.98D)
                        .currency(Currency.USD)
                        .build())
                .build();
    }

    //////////////////////
    // GetOrdersResponse
    //////////////////////

    public static GetOrdersResponse newGetOrdersResponse() {
        return GetOrdersResponse.builder()
                .orders(List.of(
                        Order.builder()
                                .id("OrderId1")
                                .resourceUrl("https://api.discogs.com/marketplace/orders/OrderId1")
                                .messagesUrl("https://api.discogs.com/marketplace/orders/OrderId1/messages")
                                .uri("https://www.discogs.com/marketplace/orders/OrderId")
                                .status(OrderStatus.NEW_ORDER)
                                .nextStatus(List.of(OrderStatus.IN_PROGRESS))
                                .fee(Price.builder()
                                        .value(9.99D)
                                        .currency(Currency.USD)
                                        .build())
                                .created(LocalDateTime.of(2024, 1, 12, 14, 30, 30))
                                .items(List.of(
                                        OrderItem.builder()
                                                .id(123456L)
                                                .release(OrderItem.Release.builder()
                                                        .id(234567L)
                                                        .description("Release Description")
                                                        .build())
                                                .price(Price.builder()
                                                        .value(9.99D)
                                                        .currency(Currency.USD)
                                                        .build())

                                                .build()))
                                .shipping(ShippingChargeAmount.builder()
                                        .value(2.99D)
                                        .currency(Currency.USD)
                                        .method("USPS")
                                        .build())
                                .shippingAddress("Shipping Address Value")
                                .additionalInstructions("Additional instructions")
                                .archived(false)
                                .seller(newSeller())
                                .lastActivity(LocalDateTime.of(2024, 1, 12, 14, 30 ,30))
                                .buyer(Buyer.builder()
                                        .id(98764L)
                                        .resourceUrl("https://wwww.discogs.com/user/98764")
                                        .username("Username")
                                        .build())
                                .total(Price.builder()
                                        .value(12.98D)
                                        .currency(Currency.USD)
                                        .build())
                                .build(),
                        Order.builder()
                                .id("OrderId2")
                                .resourceUrl("https://api.discogs.com/marketplace/orders/OrderId2")
                                .messagesUrl("https://api.discogs.com/marketplace/orders/OrderId2/messages")
                                .uri("https://www.discogs.com/marketplace/orders/OrderId")
                                .status(OrderStatus.NEW_ORDER)
                                .nextStatus(List.of(OrderStatus.IN_PROGRESS))
                                .fee(Price.builder()
                                        .value(9.99D)
                                        .currency(Currency.USD)
                                        .build())
                                .created(LocalDateTime.of(2024, 1, 12, 14, 30, 30))
                                .items(List.of(
                                        OrderItem.builder()
                                                .id(123456L)
                                                .release(OrderItem.Release.builder()
                                                        .id(234567L)
                                                        .description("Release Description")
                                                        .build())
                                                .price(Price.builder()
                                                        .value(9.99D)
                                                        .currency(Currency.USD)
                                                        .build())

                                                .build()))
                                .shipping(ShippingChargeAmount.builder()
                                        .value(2.99D)
                                        .currency(Currency.USD)
                                        .method("USPS")
                                        .build())
                                .shippingAddress("Shipping Address Value")
                                .additionalInstructions("Additional instructions")
                                .archived(false)
                                .seller(newSeller())
                                .lastActivity(LocalDateTime.of(2024, 1, 12, 14, 30 ,30))
                                .buyer(Buyer.builder()
                                        .id(98764L)
                                        .resourceUrl("https://wwww.discogs.com/user/98764")
                                        .username("Username")
                                        .build())
                                .total(Price.builder()
                                        .value(12.98D)
                                        .currency(Currency.USD)
                                        .build())
                                .build()))
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .page(1)
                        .items(2)
                        .perPage(5)
                        .build())
                .build();
    }

    /////////////////////////////
    // GetOrderMessagesResponse
    /////////////////////////////

    public static GetOrderMessagesResponse newGetOrderMessagesResponse() {
        return GetOrderMessagesResponse.builder()
                .messages(List.of(newOrderMessage(), newOrderMessage()))
                .build();
    }

    public static OrderMessage newOrderMessage() {
        final OrderReference orderReference = OrderReference.builder()
                .id("OrderReferenceId")
                .build();
        return OrderMessage.builder()
                .refund(OrderRefund.builder()
                        .order(orderReference)
                        .amount(9.99D)
                        .build())
                .timestamp(LocalDateTime.of(2024, 3,24, 14,23, 24))
                .message("Order Message")
                .type("Message Type")
                .order(orderReference)
                .subject("Message Subject")
                .build();
    }

    ////////////////////////////
    // AddOrderMessageResponse
    ////////////////////////////

    public static AddOrderMessageResponse newAddOrderMessageResponse() {
        final OrderReference orderReference = OrderReference.builder()
                .id("OrderReferenceId")
                .build();
        return AddOrderMessageResponse.builder()
                .refund(OrderRefund.builder()
                        .order(orderReference)
                        .amount(9.99D)
                        .build())
                .timestamp(LocalDateTime.of(2024, 3,24, 14,23, 24))
                .message("Order Message")
                .type("Message Type")
                .order(orderReference)
                .subject("Message Subject")
                .build();
    }

    ///////////////////
    // GetFeeResponse
    ///////////////////

    public static GetFeeResponse newGetFeeResponse() {
        return GetFeeResponse.builder()
                .value(1.99D)
                .currency(Currency.USD)
                .build();
    }

    ////////////////////////////////
    // GetPriceSuggestionsResponse
    ////////////////////////////////

    public static GetPriceSuggestionsResponse newGetPriceSuggestionsResponse() {
        return GetPriceSuggestionsResponse.builder()
                .priceSuggestions(Map.of(
                        Condition.MINT, Price.builder()
                                .currency(Currency.USD)
                                .value(14.99D)
                                .build(),
                        Condition.NEAR_MINT, Price.builder()
                                .currency(Currency.USD)
                                .value(12.99D)
                                .build(),
                        Condition.VERY_GOOD, Price.builder()
                                .currency(Currency.USD)
                                .value(9.99D)
                                .build(),
                        Condition.GOOD, Price.builder()
                                .currency(Currency.USD)
                                .value(7.99D)
                                .build(),
                        Condition.FAIR, Price.builder()
                                .currency(Currency.USD)
                                .value(5.99D)
                                .build(),
                        Condition.POOR, Price.builder()
                                .currency(Currency.USD)
                                .value(3.99D)
                                .build()))
                .build();
    }

    /////////////////////////////////
    // GetReleaseStatisticsResponse
    /////////////////////////////////

    public static GetReleaseStatisticsResponse newGetReleaseStatisticsResponse() {
        return GetReleaseStatisticsResponse.builder()
                .lowestPrice(Price.builder()
                        .currency(Currency.USD)
                        .value(6.99D)
                        .build())
                .blockedFormSale(false)
                .numForSale(23)
                .build();
    }

    @UtilityClass
    public static class Responses {
        private static final String FOLDER = "/marketplace/";

        public static SerializedResource GET_INVENTORY_RESPONSE =
                new SerializedResource(FOLDER + "GetInventoryResponse.json");
        public static SerializedResource GET_LISTING_RESPONSE =
                new SerializedResource(FOLDER + "GetListingResponse.json");
        public static SerializedResource CREATE_LISTING_RESPONSE =
                new SerializedResource(FOLDER + "CreateListingResponse.json");
        public static SerializedResource ORDER_RESPONSE =
                new SerializedResource(FOLDER + "OrderResponse.json");
        public static SerializedResource GET_ORDERS_RESPONSE =
                new SerializedResource(FOLDER + "GetOrdersResponse.json");
        public static SerializedResource GET_ORDER_MESSAGES =
                new SerializedResource(FOLDER + "GetOrderMessagesResponse.json");
        public static SerializedResource ADD_ORDER_MESSAGE =
                new SerializedResource(FOLDER + "AddOrderMessageResponse.json");
        public static SerializedResource GET_FEE_RESPONSE =
                new SerializedResource(FOLDER + "GetFeeResponse.json");
        public static SerializedResource GET_PRICE_SUGGESTIONS_RESPONSE =
                new SerializedResource(FOLDER + "GetPriceSuggestionsResponse.json");
        public static SerializedResource GET_RELEASE_STATISTICS_RESPONSE =
                new SerializedResource(FOLDER + "GetReleaseStatisticsResponse.json");
    }
}
