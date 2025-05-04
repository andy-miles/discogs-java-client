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
package com.amilesend.discogs.model.identity.type;

import com.amilesend.discogs.model.Resource;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Describes a user profile.
 *
 * @see Resource
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class UserProfile extends Resource<Long, UserProfile> {
    /** User profile description. */
    private final String profile;
    /** The URL to the user's want list. */
    @SerializedName("wantlist_url")
    private final String wantListUrl;
    /** The user's contributor rank stat. */
    private final Integer rank;
    /** The number of pending transactions. */
    private final Integer numPending;
    /** The number of items for sale. */
    private final Integer numForSale;
    /** URL for the user's home page. */
    private final String homePage;
    /** Description of the user's location. */
    private final String location;
    /** URL for the user's collection folders. */
    private final String collectionFoldersUrl;
    /** The username .*/
    private final String username;
    /** The email if the user profile matches the authenticated user. */
    private final String email;
    /** URL for the user's collection fields. */
    private final String collectionFieldsUrl;
    /** The number of contributed releases. */
    private final Integer releasesContributed;
    /** The user's registration timestamp. */
    private final LocalDateTime registered;
    /** The overage rating. */
    private final Double ratingAvg;
    /** The number of collections if the user profile matches the authenticated user. */
    private final Integer numCollection;
    /** The user's name. */
    private final String name;
    /** The number of items in the user's want list if the user profile matches the authenticated user. */
    @SerializedName("num_wantlist")
    private final Integer numWantList;
    /** URL for the user's inventory. */
    private final String inventoryUrl;
    /** URL for the user's avatar image. */
    private final String avatarUrl;
    /** URL for the user's banner image. */
    private final String bannerUrl;
    /** URL for the user's profile. */
    private final String uri;
    /** The user's buyer rating. */
    private final Double buyerRating;
    /** The user's star rating. */
    private final Integer buyerRatingStars;
    /** The number of total buyer ratings. */
    private final Integer buyerNumRatings;
    /** The user's seller rating. */
    private final Double sellerRating;
    /** The user's seller star rating. */
    private final Integer sellerRatingStars;
    /** The number of total seller ratings. */
    private final Integer sellerNumRatings;
    /** The user's configured currency type (e.g., "USD" or "EUR"). */
    private final String currAbbr;
}
