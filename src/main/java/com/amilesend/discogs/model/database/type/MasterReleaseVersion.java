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
package com.amilesend.discogs.model.database.type;

import com.amilesend.discogs.model.Resource;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Defines a master release version.
 *
 * @see Resource
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class MasterReleaseVersion extends Resource<Long, MasterReleaseVersion> {
    /** The entry status. */
    private final String status;
    /**
     * The stats for the release version.
     *
     * @see Stats
     */
    private final Stats stats;
    /** The thumbnail URL. */
    private final String thumb;
    /** The format. */
    private final String format;
    /** The country. */
    private final String country;
    /** The title. */
    private final String title;
    /** The label. */
    private final String label;
    /** The release date. */
    private final String released;
    /** The list of formats. */
    private final List<String> majorFormats;
    /** The catalog number. */
    @SerializedName("catno")
    private final String catalogNumber;
}
