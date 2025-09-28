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
package com.amilesend.discogs.connection.auth.oauth;

import com.amilesend.client.connection.ResponseException;
import com.amilesend.client.util.StringUtils;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

import static com.amilesend.discogs.connection.auth.oauth.OAuthReceiverCallback.queryToMap;

/**
 * Describes the response during the OAuth flow when requesting the request token that is used during the handshake
 * to request permission from the user to grant the application access.
 */
@Builder
@EqualsAndHashCode
@Getter
public class RequestTokenResponse {
    /** The request token. */
    private final String token;
    /** The associated token secret. */
    private final String secret;
    /** Indicates if the client callback was confirmed. */
    private final Boolean callbackConfirmed;

    /**
     * Parses a service response message body to extract and build a {@code RequestTokenResponse}.
     *
     * @param response the response body contents
     * @return the request token response
     */
    public static RequestTokenResponse parseBodyResponse(final String response) {
        if (StringUtils.isBlank(response)) {
            throw new ResponseException("RequestTokenResponse is blank");
        }

        final Map<String, String> attributes = queryToMap(response);

        return RequestTokenResponse.builder()
                .token(attributes.get("oauth_token"))
                .secret(attributes.get("oauth_token_secret"))
                .callbackConfirmed(Boolean.valueOf(attributes.get("oauth_callback_confirmed")))
                .build();
    }
}
