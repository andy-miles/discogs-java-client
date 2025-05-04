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
package com.amilesend.discogs;

import com.amilesend.client.util.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiscogsTest {
    ///////////////////////////////
    // newUnauthenticatedInstance
    /// ////////////////////////////

    @Test
    public void newUnauthenticatedInstance_withValidUserAgent_shouldReturnClient() {
        assertNotNull(Discogs.newUnauthenticatedInstance("UserAgent/1.0"));
    }

    @Test
    public void newUnauthenticatedInstance_withInvalidUserAgent_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Discogs.newUnauthenticatedInstance("Malformed")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Discogs.newUnauthenticatedInstance(StringUtils.EMPTY)),
                () -> assertThrows(NullPointerException.class,
                        () -> Discogs.newUnauthenticatedInstance(null)));
    }

    //////////////////////////////////////
    // newKeySecretAuthenticatedInstance
    //////////////////////////////////////

    @Test
    public void newKeySecretAuthenticatedInstance_withValidKeySecretAndUserAgent_shouldReturnClient() {
        assertNotNull(Discogs.newKeySecretAuthenticatedInstance("Key", "Secret", "UserAgent/1.0"));
    }

    @Test
    public void newKeySecretAuthenticatedInstance_withInvalidKeySecret_shouldThrowException() {
        final String userAgent = "UserAgent/1.0";
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Discogs.newKeySecretAuthenticatedInstance(StringUtils.EMPTY, "Secret", userAgent)),
                () -> assertThrows(NullPointerException.class,
                        () -> Discogs.newKeySecretAuthenticatedInstance(null, "Secret", userAgent)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Discogs.newKeySecretAuthenticatedInstance("Key", StringUtils.EMPTY, userAgent)),
                () -> assertThrows(NullPointerException.class,
                        () -> Discogs.newKeySecretAuthenticatedInstance("Key", null, userAgent)));
    }

    @Test
    public void newKeySecretAuthenticatedInstance_withInvalidUserAgent_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Discogs.newKeySecretAuthenticatedInstance("Key", "Secret", "Malformed")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Discogs.newKeySecretAuthenticatedInstance("Key", "Secret", StringUtils.EMPTY)),
                () -> assertThrows(NullPointerException.class,
                        () -> Discogs.newKeySecretAuthenticatedInstance("Key", "Secret", null)));
    }

    //////////////////////////////////
    // newTokenAuthenticatedInstance
    //////////////////////////////////

    @Test
    public void newTokenAuthenticatedInstance_withValidTokenAndUserAgent_shouldReturnClient() {
        assertNotNull(Discogs.newTokenAuthenticatedInstance("Token", "UserAgent/1.0"));
    }

    @Test
    public void newTokenAuthenticatedInstance_withInvalidToken_shouldThrowException() {
        final String userAgent = "UserAgent/1.0";
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Discogs.newTokenAuthenticatedInstance(StringUtils.EMPTY, userAgent)),
                () -> assertThrows(NullPointerException.class,
                        () -> Discogs.newTokenAuthenticatedInstance(null, userAgent)));
    }

    @Test
    public void newTokenAuthenticatedInstance_withInvalidUserAgent_shouldThrowException() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Discogs.newTokenAuthenticatedInstance("Token", "Malformed")),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Discogs.newTokenAuthenticatedInstance("Token", StringUtils.EMPTY)),
                () -> assertThrows(NullPointerException.class,
                        () -> Discogs.newTokenAuthenticatedInstance("Token", null)));
    }
}