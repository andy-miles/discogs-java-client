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
package com.amilesend.discogs.model.database;

import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.PaginatedRequestBase;
import com.amilesend.discogs.model.PathParameter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import okhttp3.HttpUrl;

/**
 * The request to retrieve the paginated list of releases for a label.
 *
 * @see PaginatedRequestBase
 */
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetLabelReleasesRequest extends PaginatedRequestBase {
    /** The label identifier (required). */
    @PathParameter
    private final long labelId;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        Validate.isTrue(labelId > 0L, "labelId must be > 0");
        return super.populateQueryParameters(urlBuilder);
    }
}
