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
import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Defines the list of releases for a label.
 *
 * @see PaginatedResponseBase
 * @see LabelRelease
 */
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetLabelReleasesResponse extends PaginatedResponseBase<GetLabelReleasesResponse> {
    /**
     * The list of releases.
     *
     * @see LabelRelease
     */
    private final List<LabelRelease> releases;

    @Override
    public Class<GetLabelReleasesResponse> getType() {
        return GetLabelReleasesResponse.class;
    }

    /**
     * Defines a release for a label.
     *
     * @see Resource
     */
    @SuperBuilder
    @Getter
    @ToString(callSuper = true)
    public static class LabelRelease extends Resource<Long, LabelRelease> {
        /** The entry status. */
        private final String status;
        /** The format. */
        private final String format;
        @SerializedName("catno")
        private final String catalogNumber;
        /** The thumbnail image URL. */
        private final String thumb;
        /** The title. */
        private final String title;
        /** The artist name. */
        private final String artist;
        /** The stats associated with the release. */
        private final Stats stats;
        /** The year of the release. */
        private final Integer year;
    }
}
