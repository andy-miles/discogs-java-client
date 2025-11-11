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
package com.amilesend.discogs.connection.auth.oauth;

import com.amilesend.client.connection.ResponseException;
import com.amilesend.client.util.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccessTokenResponseTest {
    @Test
    public void parseBodyResponse_withValidBody_shouldReturnResponse() {
        final String body = "oauth_token=TokenValue&oauth_token_secret=TokenSecret";
        
        final AccessTokenResponse actual = AccessTokenResponse.parseBodyResponse(body);
        
        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals("TokenValue", actual.getAccessToken()),
                () -> assertEquals("TokenSecret", actual.getAccessTokenSecret()));
    }

    @Test
    public void parseBodyResponse_withInvalidBody_shouldThrowException() {
        assertAll(
                () -> assertThrows(ResponseException.class, () -> AccessTokenResponse.parseBodyResponse(null)),
                () -> assertThrows(ResponseException.class,
                        () -> AccessTokenResponse.parseBodyResponse(StringUtils.EMPTY)));
    }
}
