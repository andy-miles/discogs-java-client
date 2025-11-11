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
package com.amilesend.discogs.model.collection.type;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/** Describes a field. */
@Builder
@Data
public class Field {
    /** The field name. */
    private final String name;
    /** The list of options associated with the field. Note: Applies to "dropdown" type. */
    private final List<String> options;
    /** The field identifier. */
    private final Long id;
    /** The position. */
    private final Integer position;
    /** The number of lines. Note: Applies to "textarea" type. */
    private final Integer lines;
    /** The type specifier. */
    private final String type;
    /** Public flag indicator. */
    @SerializedName("public")
    private final Boolean _public;
}
