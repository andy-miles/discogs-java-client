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

import com.amilesend.discogs.model.NamedResource;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Defines a catalog entity resource.
 *
 * @see NamedResource
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class CatalogEntity extends NamedResource<Long, CatalogEntity> {
    /** The catalog number. */
    @SerializedName("catno")
    private final String catalogNumber;
    /** The entity type. */
    private final String entityType;
    /** The entity type name. */
    private final String entityTypeName;
}
