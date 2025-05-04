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

import com.amilesend.discogs.model.PaginatedResponseBase;
import com.amilesend.discogs.model.Resource;
import com.amilesend.discogs.model.database.type.Stats;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Defines the list of releases for an artist.
 *
 * @see PaginatedResponseBase
 * @see ArtistRelease
 */
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetArtistReleasesResponse extends PaginatedResponseBase<GetArtistReleasesResponse> {
    /**
     * The list of releases.
     * @see ArtistRelease
     */
    private final List<ArtistRelease> releases;

    @Override
    public Class<GetArtistReleasesResponse> getType() {
        return GetArtistReleasesResponse.class;
    }

    /**
     * Defines a release for an artist.
     *
     * @see Resource
     */
    @SuperBuilder
    @Getter
    @ToString(callSuper = true)
    public static class ArtistRelease extends Resource<Long, ArtistRelease> {
        /** The entry status. */
        private final String status;
        /** The type (e.g., "release" or "master"). */
        private final String type;
        /** The main release identifier. */
        private final Long mainRelease;
        /** The format. */
        private final String format;
        /** The label. */
        private final String label;
        /** The title. */
        private final String title;
        /** The role. */
        private final String role;
        /** The artist name. */
        private final String artist;
        /** The year of the release. */
        private final Integer year;
        /** The thumbnail image URL. */
        private final String thumb;
        /** The stats associated with the release. */
        private final Stats stats;
    }
}
