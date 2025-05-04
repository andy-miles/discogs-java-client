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
package com.amilesend.discogs.model;

import com.amilesend.client.parse.parser.BasicParser;
import com.amilesend.client.parse.strategy.GsonExclude;
import com.amilesend.client.util.StringUtils;
import com.amilesend.discogs.connection.DiscogsConnection;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import okhttp3.HttpUrl;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Defines a paginated response with a limited set of results.
 *
 * @param <T> The response implementation type
 */
@SuperBuilder
@Data
public abstract class PaginatedResponseBase<T> {
    /** The underlying API connection used to navigate response pages. */
    @GsonExclude
    private final DiscogsConnection connection;
    /**
     * The pagination information.
     *
     * @see Pagination
     */
    private final Pagination pagination;

    /** Defines the method to return the implementation class type. */
    public abstract Class<T> getType();

    /**
     * Gets the first page.
     *
     * @return the first page, or {@code null} if no pages exist
     */
    public T getFirst() {
       return navigatePage(getPagination().getFirstUrl());
    }

    /**
     * Determines if there is a first page available for navigation.
     *
     * @return {@code true} if there is a first page; else, {@code false}
     */
    public boolean hasFirst() {
        return Objects.nonNull(getPagination().getFirstUrl());
    }

    /**
     * Gets the previous page.
     *
     * @return the previous page, or {@code null} if no pages exist
     */
    public T getPrevious() {
        return navigatePage(getPagination().getPreviousUrl());
    }

    /**
     * Determines if there is a previous page available for navigation.
     *
     * @return {@code true} if there is a previous page; else, {@code false}
     */
    public boolean hasPrevious() {
        return Objects.nonNull(getPagination().getPreviousUrl());
    }

    /**
     * Gets the next page.
     *
     * @return the next page, or {@code null} if no pages exist
     */
    public T getNext() {
        return navigatePage(getPagination().getNextUrl());
    }

    /**
     * Determines if there is a next page available for navigation.
     *
     * @return {@code true} if there is a next page; else, {@code false}
     */
    public boolean hasNext() {
        return Objects.nonNull(getPagination().getNextUrl());
    }

    /**
     * Gets the last page.
     *
     * @return the last page, or {@code null} if no pages exist
     */
    public T getLast() {
        return navigatePage(getPagination().getLastUrl());
    }

    /**
     * Determines if there is a last page available for navigation.
     *
     * @return {@code true} if there is a last page; else, {@code false}
     */
    public boolean hasLast() {
        return Objects.nonNull(getPagination().getLastUrl());
    }

    // Helper method to navigate pages
    private T navigatePage(final String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        return connection.execute(
                connection.newRequestBuilder()
                        .url(HttpUrl.parse(url))
                        .build(),
                new BasicParser<>(getType()));
    }

    /** Describes the pagination information for a response. */
    @Builder
    @Data
    public static class Pagination {
        /** The page number associated with the results. */
        private final Integer page;
        /** The total number of pages available. */
        private final Integer pages;
        /** The total number of items available. */
        private final Integer items;
        /** The number of items per page. */
        private final Integer perPage;
        /** The map of URLs used to navigate pages. */
        @Builder.Default
        private final Map<String, String> urls = Collections.emptyMap();

        /**
         * Gets the URL to navigate to the first page.
         *
         * @return the first page URL, or {@code null} if no pages exist
         */
        public String getFirstUrl() {
            return urls.get("first");
        }

        /**
         * Gets the URL for the previous page.
         *
         * @return the previous page URL, or {@code null} if no pages exist
         */
        public String getPreviousUrl() {
            return urls.get("prev");
        }

        /**
         * Gets the URL for the next page.
         *
         * @return the next page URL, or {@code null} if no pages exist
         */
        public String getNextUrl() {
            return urls.get("next");
        }

        /**
         * Gets the URL for the last page.
         *
         * @return the last page URL, or {@code null} if no pages exist
         */
        public String getLastUrl() {
            return urls.get("last");
        }
    }
}
