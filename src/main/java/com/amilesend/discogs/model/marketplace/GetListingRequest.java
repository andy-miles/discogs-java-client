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

import com.amilesend.client.util.StringUtils;
import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.PathParameter;
import com.amilesend.discogs.model.QueryParameter;
import com.amilesend.discogs.model.QueryParameterBasedRequest;
import com.amilesend.discogs.model.type.Currency;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import okhttp3.HttpUrl;

import java.util.Optional;

import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotBlank;

/**
 * The request to retrieve a specific listing.
 *
 * @see QueryParameterBasedRequest
 */
@Builder
@Data
public class GetListingRequest implements QueryParameterBasedRequest {
    /** The listing identifier (required). */
    @PathParameter
    private final long listingId;
    /** The currency (optional). */
    @QueryParameter
    private final Currency currency;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        Validate.isTrue(listingId > 0L, "listingId must be > 0");
        return appendIfNotBlank(
                urlBuilder,
                "curr_abbr",
                Optional.ofNullable(currency)
                        .map(Currency::name)
                        .orElse(StringUtils.EMPTY));
    }
}
