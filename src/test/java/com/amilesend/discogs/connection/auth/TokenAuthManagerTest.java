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
package com.amilesend.discogs.connection.auth;

import com.amilesend.discogs.connection.auth.info.TokenAuthInfo;
import okhttp3.Request;
import org.junit.jupiter.api.Test;

import static com.amilesend.client.connection.Connection.Headers.AUTHORIZATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TokenAuthManagerTest {
    private static final TokenAuthInfo AUTH_INFO = new TokenAuthInfo("TokenValue");

    private TokenAuthManager authManagerUnderTest = new TokenAuthManager(AUTH_INFO);

    @Test
    public void ctor_withNullAuthInfo_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> new TokenAuthManager(null));
    }

    @Test
    public void getAuthInfo_withAuthInfo_shouldReturnAuthInfo() {
        assertEquals(AUTH_INFO, authManagerUnderTest.getAuthInfo());
    }

    @Test
    public void addAuthentication_withValidRequestBuilder_shouldAddAuthorizationHeader() {
        final Request.Builder builder = new Request.Builder();

        authManagerUnderTest.addAuthentication(builder);

        assertEquals("Discogs token=TokenValue", builder.getHeaders$okhttp().get(AUTHORIZATION));
    }

    @Test
    public void addAuthentication_withNullAuthInfo_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> authManagerUnderTest.addAuthentication(null));
    }
}
