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

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

/** Describes a stat for a master/release. */
@Builder
@Data
public class Stat {
    /** The number of members with the master/release/version in their collection. */
    private final Integer inCollection;
    @SerializedName("in_wantlist")
    /** The number of members with the master/release/version in their want list. */
    private final Integer inWantList;
}
