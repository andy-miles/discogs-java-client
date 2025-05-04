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
package com.amilesend.discogs.model.database.type;

import com.amilesend.discogs.model.Resource;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Describes an artist resource.
 *
 * @see Resource
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class ArtistInformation extends Resource<Long, ArtistInformation> {
    /** The artist name. */
    private final String name;
    /** The artist website URI. */
    private final String uri;
    /** The releases website URI. */
    private final String releasesUrl;
    /**
     * The list of images associated with the artist.
     *
     * @see Image
     */
    private final List<Image> images;
    /** The artist profile description. */
    private final String profile;
    /** The list of associated URLs. */
    private final List<String> urls;
    /** The list of name variations (e.g., Artist/band name in different languages). */
    @SerializedName("namevariations")
    private final List<String> nameVariations;
    /** List of aliases. */
    private final List<Alias> aliases;
    /** List of members associated with the artist (i.e., band/group). */
    private final List<Member> members;
    /** The data quality status. */
    private final String dataQuality;
}
