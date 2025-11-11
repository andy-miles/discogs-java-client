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
package com.amilesend.discogs.model.lists.type;

import com.amilesend.discogs.model.NamedResource;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Describes a user list.
 *
 * @see NamedResource
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class UserList extends NamedResource<Long, UserList> {
    /** The timestamp that the list was created. */
    private final LocalDateTime dateAdded;
    /** The timestamp of the last change to the list. */
    private final LocalDateTime dateChanged;
    /** The website URI. */
    private final String uri;
    /** Public visibility flag indicator. */
    @SerializedName("public")
    private final Boolean _public;
    /** The list description. */
    private final String description;
    /**
     * The user list items.
     *
     * @see UserListItem
     */
    private final List<UserListItem> items;
}
