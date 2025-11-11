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
package com.amilesend.discogs.model.collection;

import com.amilesend.client.parse.strategy.GsonExclude;
import com.amilesend.client.util.Validate;
import com.amilesend.discogs.model.BodyBasedRequest;
import com.amilesend.discogs.model.PathParameter;
import com.amilesend.discogs.model.QueryParameter;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import okhttp3.HttpUrl;

import static com.amilesend.discogs.model.QueryParameterBasedRequest.appendIfNotBlank;

/**
 * The request to edit a field for an instance.
 *
 * @see BodyBasedRequest
 */
@Builder
@Data
public class EditInstanceFieldRequest implements BodyBasedRequest {
    /** The username (required). */
    @PathParameter
    @GsonExclude
    private final String username;
    /** The folder identifier (required). */
    @PathParameter
    @GsonExclude
    private final long folderId;
    /** The release identifier (required). */
    @PathParameter
    @GsonExclude
    private final long releaseId;
    /** The instance identifier (required). */
    @PathParameter
    @GsonExclude
    private final long instanceId;
    /** The field identifier (required). */
    @PathParameter
    @GsonExclude
    private final long fieldId;
    /** The value to edit (required). */
    @QueryParameter
    @GsonExclude
    private final String value;

    @Override
    public HttpUrl.Builder populateQueryParameters(@NonNull final HttpUrl.Builder urlBuilder) {
        Validate.notBlank(username, "username must not be blank");
        Validate.isTrue(folderId >= 0L, "folderId must be >= 0");
        Validate.isTrue(releaseId > 0L, "releaseId must be > 0");
        Validate.isTrue(instanceId > 0L, "instanceId must be > 0");
        Validate.isTrue(fieldId > 0L, "fieldId must be > 0");
        Validate.notBlank(value, "value must not be blank");

        return appendIfNotBlank(urlBuilder, "value", value);
    }
}
