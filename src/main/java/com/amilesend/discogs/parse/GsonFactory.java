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
package com.amilesend.discogs.parse;

import com.amilesend.client.parse.GsonFactoryBase;
import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.model.collection.GetCollectionItemsByFolderResponse;
import com.amilesend.discogs.model.collection.GetCollectionItemsByReleaseResponse;
import com.amilesend.discogs.model.database.GetArtistReleasesResponse;
import com.amilesend.discogs.model.database.GetLabelReleasesResponse;
import com.amilesend.discogs.model.database.GetMasterReleaseVersionsResponse;
import com.amilesend.discogs.model.database.SearchResponse;
import com.amilesend.discogs.model.database.type.SearchType;
import com.amilesend.discogs.model.identity.GetUserContributionsResponse;
import com.amilesend.discogs.model.identity.GetUserSubmissionsResponse;
import com.amilesend.discogs.model.inventory.GetExportsResponse;
import com.amilesend.discogs.model.inventory.GetUploadsResponse;
import com.amilesend.discogs.model.lists.GetUserListsResponse;
import com.amilesend.discogs.model.marketplace.GetInventoryResponse;
import com.amilesend.discogs.model.marketplace.GetOrderMessagesResponse;
import com.amilesend.discogs.model.marketplace.GetOrdersResponse;
import com.amilesend.discogs.model.marketplace.type.Condition;
import com.amilesend.discogs.model.marketplace.type.SleeveCondition;
import com.amilesend.discogs.model.type.ListingStatus;
import com.amilesend.discogs.model.wantlist.GetWantListResponse;
import com.amilesend.discogs.parse.adapter.ConditionAdapter;
import com.amilesend.discogs.parse.adapter.ListingStatusAdapter;
import com.amilesend.discogs.parse.adapter.LocalDateTimeTypeAdapter;
import com.amilesend.discogs.parse.adapter.LocalDateTypeAdapter;
import com.amilesend.discogs.parse.adapter.SearchTypeAdapter;
import com.amilesend.discogs.parse.adapter.SleeveConditionAdapter;
import com.amilesend.discogs.parse.creator.GetArtistReleasesResponseCreator;
import com.amilesend.discogs.parse.creator.GetCollectionItemsByFolderResponseCreator;
import com.amilesend.discogs.parse.creator.GetCollectionItemsByReleaseResponseCreator;
import com.amilesend.discogs.parse.creator.GetExportsResponseCreator;
import com.amilesend.discogs.parse.creator.GetInventoryResponseCreator;
import com.amilesend.discogs.parse.creator.GetLabelReleasesResponseCreator;
import com.amilesend.discogs.parse.creator.GetMasterReleaseVersionsResponseCreator;
import com.amilesend.discogs.parse.creator.GetOrderMessagesResponseCreator;
import com.amilesend.discogs.parse.creator.GetOrdersResponseCreator;
import com.amilesend.discogs.parse.creator.GetUploadsResponseCreator;
import com.amilesend.discogs.parse.creator.GetUserContributionsResponseCreator;
import com.amilesend.discogs.parse.creator.GetUserListsResponseCreator;
import com.amilesend.discogs.parse.creator.GetUserSubmissionsResponseCreator;
import com.amilesend.discogs.parse.creator.GetWantListResponseCreator;
import com.amilesend.discogs.parse.creator.SearchResponseCreator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

/** Factory that vends new pre-configured {@link Gson} instances. */
@NoArgsConstructor
public class GsonFactory extends GsonFactoryBase<DiscogsConnection> {
    private static final GsonFactory INSTANCE = new GsonFactory();
    private static final Gson AUTH_INSTANCE = new GsonBuilder()
            .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
            .create();

    /**
     * Gets the {@link Gson} instance used for authentication.
     *
     * @return the configured Gson instance
     */
    public static Gson getGsonForAuthentication() {
        return AUTH_INSTANCE;
    }

    @Override
    protected GsonBuilder configure(final GsonBuilder gsonBuilder, final DiscogsConnection connection) {
        return gsonBuilder.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .registerTypeAdapter(SearchType.class, new SearchTypeAdapter())
                .registerTypeAdapter(Condition.class, new ConditionAdapter())
                .registerTypeAdapter(SleeveCondition.class, new SleeveConditionAdapter())
                .registerTypeAdapter(ListingStatus.class, new ListingStatusAdapter())
                .registerTypeAdapter(
                        GetUserSubmissionsResponse.class,
                        new GetUserSubmissionsResponseCreator(connection))
                .registerTypeAdapter(
                        GetUserContributionsResponse.class,
                        new GetUserContributionsResponseCreator(connection))
                .registerTypeAdapter(
                        GetMasterReleaseVersionsResponse.class,
                        new GetMasterReleaseVersionsResponseCreator(connection))
                .registerTypeAdapter(
                        GetArtistReleasesResponse.class,
                        new GetArtistReleasesResponseCreator(connection))
                .registerTypeAdapter(
                        GetLabelReleasesResponse.class,
                        new GetLabelReleasesResponseCreator(connection))
                .registerTypeAdapter(
                        SearchResponse.class,
                        new SearchResponseCreator(connection))
                .registerTypeAdapter(
                        GetInventoryResponse.class,
                        new GetInventoryResponseCreator(connection))
                .registerTypeAdapter(
                        GetOrdersResponse.class,
                        new GetOrdersResponseCreator(connection))
                .registerTypeAdapter(
                        GetOrderMessagesResponse.class,
                        new GetOrderMessagesResponseCreator(connection))
                .registerTypeAdapter(
                        GetCollectionItemsByReleaseResponse.class,
                        new GetCollectionItemsByReleaseResponseCreator(connection))
                .registerTypeAdapter(
                        GetCollectionItemsByFolderResponse.class,
                        new GetCollectionItemsByFolderResponseCreator(connection))
                .registerTypeAdapter(
                        GetWantListResponse.class,
                        new GetWantListResponseCreator(connection))
                .registerTypeAdapter(
                        GetUserListsResponse.class,
                        new GetUserListsResponseCreator(connection))
                .registerTypeAdapter(
                        GetExportsResponse.class,
                        new GetExportsResponseCreator(connection))
                .registerTypeAdapter(
                        GetUploadsResponse.class,
                        new GetUploadsResponseCreator(connection));
    }
}
