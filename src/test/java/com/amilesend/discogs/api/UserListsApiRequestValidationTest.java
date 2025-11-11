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
package com.amilesend.discogs.api;

import com.amilesend.client.util.StringUtils;
import com.amilesend.discogs.RequestValidationTestBase;
import com.amilesend.discogs.model.lists.GetUserListRequest;
import com.amilesend.discogs.model.lists.GetUserListsRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserListsApiRequestValidationTest extends RequestValidationTestBase {

    ////////////////////////
    // GetUserListsRequest
    ////////////////////////

    @Test
    public void getUserListsRequest_withInvalidRequest_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> GetUserListsRequest.builder()
                                .username(null)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> GetUserListsRequest.builder()
                                .username(StringUtils.EMPTY)
                                .build()
                                .populateQueryParameters(mockHttpUrlBuilder)));
    }

    ///////////////////////
    // GetUserListRequest
    ///////////////////////

    @Test
    public void getUserListRequest_withInvalidRequest_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> GetUserListRequest.builder()
                        .listId(-1L)
                        .build()
                        .populateQueryParameters(mockHttpUrlBuilder));
    }
}
