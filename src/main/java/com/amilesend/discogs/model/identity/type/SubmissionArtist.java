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
package com.amilesend.discogs.model.identity.type;

import com.amilesend.discogs.model.Resource;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Describes an artist associated with a submission.
 *
 * @see Resource
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class SubmissionArtist extends Resource<Long, SubmissionArtist> {
    /** The data quality descriptor. */
    private final String dataQuality;
    /** The artist name. */
    private final String name;
    /** The list of alternative name variations. */
    @SerializedName("namevariations")
    private final List<String> nameVariations;
    /** The URL for the artist's releases. */
    private final String releasesUrl;
    /** The URL for this artist resource. */
    private final String uri;
}
