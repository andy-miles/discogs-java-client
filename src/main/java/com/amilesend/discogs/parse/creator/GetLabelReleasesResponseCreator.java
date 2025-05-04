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
package com.amilesend.discogs.parse.creator;

import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.model.database.GetLabelReleasesResponse;
import com.google.gson.InstanceCreator;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Type;

/**
 * A custom {@link InstanceCreator} implementation that injects the {@link DiscogsConnection} to the
 * resource type so that method operations can be performed on the {@link GetLabelReleasesResponse}
 * resource.
 *
 * @see InstanceCreator
 * @see GetLabelReleasesResponse
 */
@RequiredArgsConstructor
public class GetLabelReleasesResponseCreator implements InstanceCreator<GetLabelReleasesResponse> {
    /** The current client connection instance. */
    private final DiscogsConnection connection;

    @Override
    public GetLabelReleasesResponse createInstance(final Type type) {
        return GetLabelReleasesResponse.builder().connection(connection).build();
    }
}
