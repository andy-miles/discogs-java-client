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
package com.amilesend.discogs.model.collection.type;

import com.amilesend.discogs.model.Resource;
import com.amilesend.discogs.model.type.BasicInformation;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Describes a release within a user's collection.
 *
 * @see Resource
 */
@SuperBuilder
@ToString(callSuper = true)
@Getter
public class CollectionRelease extends Resource<Long, CollectionRelease> {
    /** The instance identifier. */
    private final Long instanceId;
    /** The rating. */
    private final Integer rating;
    /**
     * The release information.
     *
     * @see BasicInformation
     */
    private final BasicInformation basicInformation;
    /** The associated folder identifier. */
    private final Long folderId;
    /** The timestamp. */
    private final LocalDateTime dateAdded;
    /** The list of associated notes. */
    private final List<Note> notes;
}
