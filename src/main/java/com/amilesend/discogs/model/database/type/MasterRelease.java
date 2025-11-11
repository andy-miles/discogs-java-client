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

import com.amilesend.discogs.model.type.ReleaseContentBase;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Defines a master release.
 *
 * @see ReleaseContentBase
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class MasterRelease extends ReleaseContentBase {
    /** The main release identifier. */
    private final Long mainRelease;
    /** The most recent release identifier. */
    private final Long mostRecentRelease;
    /** The URL for versions. */
    private final String versionsUrl;
    /** The URL for the main release. */
    private final String mainReleaseUrl;
    /** The URL for the most recent release. */
    private final String mostRecentReleaseUrl;
}
