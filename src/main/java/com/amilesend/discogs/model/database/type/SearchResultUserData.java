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

/** User data associated with a search result. */
@Builder
@Data
public class SearchResultUserData {
    /** Flag indicator that the search result is in the user's want list. */
    @SerializedName("in_wantlist")
    private final Boolean inWantList;
    /** Flag indicator that the search reuslt is in the user's collection. */
    private final Boolean inCollection;
}
