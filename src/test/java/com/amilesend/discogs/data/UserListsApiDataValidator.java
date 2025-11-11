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
package com.amilesend.discogs.data;

import com.amilesend.discogs.model.lists.GetUserListResponse;
import com.amilesend.discogs.model.lists.GetUserListsResponse;
import com.amilesend.discogs.model.lists.type.UserList;
import com.amilesend.discogs.model.lists.type.UserListItem;
import lombok.experimental.UtilityClass;

import java.util.Objects;

import static com.amilesend.discogs.data.DataValidatorHelper.validateListOf;
import static com.amilesend.discogs.data.DataValidatorHelper.validateNamedResource;
import static com.amilesend.discogs.data.DataValidatorHelper.validatePaginatedResponseBase;
import static com.amilesend.discogs.data.DataValidatorHelper.validateResource;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UtilityClass
public class UserListsApiDataValidator {
    /////////////////////////
    // GetUserListsResponse
    /////////////////////////

    public static void validateGetUserListsResponse(
            final GetUserListsResponse expected,
            final GetUserListsResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validatePaginatedResponseBase(expected, actual),
                () -> validateListOf(
                        expected.getLists(),
                        actual.getLists(),
                        UserListsApiDataValidator::validateUserList));
    }

    private static void validateUserList(final UserList expected, final UserList actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateNamedResource(expected, actual),
                () -> assertEquals(expected.getDateAdded(), actual.getDateAdded()),
                () -> assertEquals(expected.getDateChanged(), actual.getDateChanged()),
                () -> assertEquals(expected.getUri(), actual.getUri()),
                () -> assertEquals(expected.get_public(), actual.get_public()),
                () -> assertEquals(expected.getDescription(), actual.getDescription()),
                () -> validateListOf(
                        expected.getItems(),
                        actual.getItems(),
                        UserListsApiDataValidator::validateUserListItem));
    }

    private static void validateUserListItem(final UserListItem expected, final UserListItem actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getComment(), actual.getComment()),
                () -> assertEquals(expected.getDisplayTitle(), actual.getDisplayTitle()),
                () -> assertEquals(expected.getUri(), actual.getUri()),
                () -> assertEquals(expected.getType(), actual.getType()));
    }

    ////////////////////////
    // GetUserListResponse
    ////////////////////////

    public static void validateGetUserListResponse(
            final GetUserListResponse expected,
            final GetUserListResponse actual) {
        validateUserList(expected, actual);
    }
}
