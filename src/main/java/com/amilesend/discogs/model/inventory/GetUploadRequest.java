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
package com.amilesend.discogs.model.inventory;

import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.PathParameter;
import com.amilesend.discogs.model.QueryParameterBasedRequest;
import lombok.Builder;
import lombok.Data;
import okhttp3.HttpUrl;

/**
 * The request to get information for a specific upload.
 *
 * @see QueryParameterBasedRequest
 */
@Builder
@Data
public class GetUploadRequest implements QueryParameterBasedRequest {
    /** The upload identifier (required). */
    @PathParameter
    private final int uploadId;

    @Override
    public HttpUrl.Builder populateQueryParameters(final HttpUrl.Builder urlBuilder) {
        Validate.isTrue(uploadId > 0, "exportId must be > 0");
        return urlBuilder;
    }
}
