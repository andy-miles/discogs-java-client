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
package com.amilesend.discogs.model.inventory;

import com.amilesend.discogs.model.PaginatedResponseBase;
import com.amilesend.discogs.model.inventory.type.ExportItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The paginated list of exports.
 *
 * @see PaginatedResponseBase
 * @see ExportItem
 */
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetExportsResponse extends PaginatedResponseBase<GetExportsResponse> {
    /**
     * The list of items.
     *
     * @see ExportItem
     */
    private final List<ExportItem> items;

    @Override
    public Class<GetExportsResponse> getType() {
        return GetExportsResponse.class;
    }
}
