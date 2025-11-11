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
package com.amilesend.discogs.model.type;

import com.amilesend.discogs.model.Resource;
import com.amilesend.discogs.model.database.type.Image;
import com.amilesend.discogs.model.database.type.TrackInformation;
import com.amilesend.discogs.model.database.type.Video;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder
@Getter
@ToString(callSuper = true)
public class ReleaseContentBase extends Resource<Long, Release> {
    /**
     * The list of associated artists.
     *
     * @see Artist
     */
    private final List<Artist> artists;

    /** Data quality descriptor. */
    private final String dataQuality;
    /** The timestamp that this release entry was added. */
    private final LocalDateTime dateAdded;
    /** The timestamp that this release entry was last. */
    private final LocalDateTime dateChanged;
    /** The estimated relevance weight. */
    private final Integer estimatedWeight;
    /** List of applicable genres. */
    private final List<String> genres;
    /**
     * List of associated images.
     *
     * @see Image
     */
    private final List<Image> images;
    /** The lowest price. */
    private final Double lowestPrice;
    /** Number of items available for sale. */
    private final Integer numForSale;
    /** List of musical styles. */
    private final List<String> styles;
    /** The URL of the thumbnail image. */
    private final String thumb;
    /**
     * The list of tracks associated with the release.
     *
     * @see TrackInformation
     */
    @SerializedName("tracklist")
    private final List<TrackInformation> trackList;
    /** The release title. */
    private final String title;
    /** The website uri for this release. */
    private final String uri;
    /**
     * The list of associated videos.
     *
     * @see Video
     */
    private final List<Video> videos;
    /** The release year. */
    private final Integer year;
}
