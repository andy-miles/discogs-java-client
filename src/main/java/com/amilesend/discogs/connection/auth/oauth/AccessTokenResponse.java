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
import com.amilesend.discogs.connection.auth.info.OAuthInfo;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

import static com.amilesend.discogs.connection.auth.oauth.OAuthReceiverCallback.queryToMap;

/**
 * Describes the response during the OAuth flow when requesting the access token that is used during the handshake
 * to request permission from the user that explicitly granted the application access.
 */
@Builder
@EqualsAndHashCode
@Getter
public class AccessTokenResponse {
    /** The access token. */
    private final String accessToken;
    /** The access token secret. */
    private final String accessTokenSecret;

    /**
     * Parses a service response message body to extract and build an {@code AccessTokenResponse}.
     *
     * @param response the response body contents
     * @return the access token response
     */
    public static AccessTokenResponse parseBodyResponse(final String response) {
        if (StringUtils.isBlank(response)) {
            throw new ResponseException("RequestTokenResponse is blank");
        }

        final Map<String, String> attributes = queryToMap(response);

        return AccessTokenResponse.builder()
                .accessToken(attributes.get("oauth_token"))
                .accessTokenSecret(attributes.get("oauth_token_secret"))
                .build();
    }

    public OAuthInfo toOAuthInfo() {
        return new OAuthInfo(accessToken, accessTokenSecret);
    }
}
