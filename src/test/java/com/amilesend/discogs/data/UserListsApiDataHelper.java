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
package com.amilesend.discogs.data;

import com.amilesend.discogs.model.PaginatedResponseBase;
import com.amilesend.discogs.model.lists.GetUserListResponse;
import com.amilesend.discogs.model.lists.GetUserListsResponse;
import com.amilesend.discogs.model.lists.type.UserList;
import com.amilesend.discogs.model.lists.type.UserListItem;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@UtilityClass
public class UserListsApiDataHelper {
    /////////////////////////
    // GetUserListsResponse
    /////////////////////////

    public static GetUserListsResponse newGetUserListsResponse() {
        return GetUserListsResponse.builder()
                .lists(List.of(newUserList(1), newUserList(2), newUserList(3)))
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .page(1)
                        .pages(2)
                        .perPage(50)
                        .items(86)
                        .urls(Map.of(
                                "last", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50",
                                "next", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50"))
                        .build())
                .build();
    }

    private static UserList newUserList(final int index) {
        final long indexValue = 10L + index;
        return UserList.builder()
                .id(indexValue)
                .resourceUrl("https://someurl/" + indexValue)
                .name("User List Name " + indexValue)
                .dateAdded(LocalDateTime.of(2021, 12, 1, 14, 30, 00))
                .dateChanged(LocalDateTime.of(2021, 12, 1, 14, 30, 00))
                .uri("https://someurl/" + indexValue)
                ._public(Boolean.FALSE)
                .description("Description " + indexValue)
                .items(List.of(newUserListItem(1), newUserListItem(2), newUserListItem(3)))
                .build();
    }

    private static UserListItem newUserListItem(final int index) {
        final long indexValue = 20L+ index;
        return UserListItem.builder()
                .id(indexValue)
                .resourceUrl("https://someurl/" + indexValue)
                .comment("Comment " + indexValue)
                .displayTitle("Title " + indexValue)
                .uri("https://someurl/" + indexValue)
                .type("Type " + index)
                .build();
    }

    ////////////////////////
    // GetUserListResponse
    ////////////////////////

    public static GetUserListResponse newGetUserListResponse() {
        return GetUserListResponse.builder()
                .id(14L)
                .resourceUrl("https://someurl/14")
                .name("User List Name")
                .dateAdded(LocalDateTime.of(2021, 12, 1, 14, 30, 00))
                .dateChanged(LocalDateTime.of(2021, 12, 1, 14, 30, 00))
                .uri("https://someurl/14")
                ._public(Boolean.FALSE)
                .description("Description")
                .items(List.of(newUserListItem(1), newUserListItem(2), newUserListItem(3)))
                .build();
    }

    @UtilityClass
    public static class Responses {
        private static final String FOLDER = "/userlists/";

        public static SerializedResource GET_USER_LISTS_RESPONSE =
                new SerializedResource(FOLDER + "GetUserListsResponse.json");
        public static SerializedResource GET_USER_LIST_RESPONSE =
                new SerializedResource(FOLDER + "GetUserListResponse.json");
    }
}
