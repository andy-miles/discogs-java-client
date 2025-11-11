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
package com.amilesend.discogs.model.marketplace.type;

import com.amilesend.discogs.model.Resource;
import com.amilesend.discogs.model.database.type.Image;
import com.amilesend.discogs.model.database.type.Stats;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Describes a release for a listing.
 *
 * @see Resource
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class ListingRelease extends Resource<Long, ListingRelease> {
    /** The listing's catalog number. */
    private final String catalogNumber;
    /** The release year. */
    private final Integer year;
    /** The description. */
    private final String description;
    /** The artist name. */
    private final String artist;
    /** The title. */
    private final String title;
    /** The release format. */
    private final String format;
    /** The thumbnail image URL. */
    private final String thumbnail;
    /** The label. */
    private final String label;
    /** The associated status for the release. */
    private final Stats stats;
    /** The associated images. */
    private List<Image> images;
}
