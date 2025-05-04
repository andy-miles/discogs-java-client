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
package com.amilesend.discogs.model.inventory.type;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

/** Describes an upload to a user's inventory. */
@SuperBuilder
@Getter
@ToString
public class UploadItem {
    /** The export status. */
    private final String status;
    /** Information about the processed CSV upload. */
    private final String results;
    /** The export request timestamp. */
    private final LocalDateTime createdTs;
    /** The time of completion timestamp. */
    private final LocalDateTime finishedTs;
    /** The filename of the export. */
    private final String filename;
    /** The upload type. */
    private final String type;
    /** The export identifier. */
    private final Long id;

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final UploadItem that = (UploadItem) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
