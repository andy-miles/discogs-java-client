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
package com.amilesend.discogs.model.type;

import com.amilesend.discogs.model.database.type.Rating;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/** Describes a community. */
@Builder
@Data
public class Community {
    /**
     * The list of contributors
     *
     * @see CommunityMember
     */
    private final List<CommunityMember> contributors;
    /** The data quality description. */
    private final String dataQuality;
    /** The count of how many members have a resource. */
    private final Integer have;
    /**
     * The community rating fo a release.
     *
     * @see Rating
     */
    private final Rating rating;
    /** The status. */
    private final String status;
    /**
     * The original submitter for the resource.
     *
     * @see CommunityMember
     */
    private final CommunityMember submitter;
    /** The count of now many members want a give resource. */
    private final Integer want;
}
