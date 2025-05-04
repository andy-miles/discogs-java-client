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

import com.amilesend.client.connection.auth.AuthManager;
import com.amilesend.discogs.connection.auth.info.KeySecretAuthInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import okhttp3.Request;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.amilesend.client.connection.Connection.Headers.AUTHORIZATION;

/**
 * Authorization manager for key + secret pairs.
 *
 * @see AuthManager
 * @see KeySecretAuthInfo
 */
@RequiredArgsConstructor
public class KeySecretAuthManager implements AuthManager<KeySecretAuthInfo> {
    /** The user's key and secret. */
    @NonNull
    final KeySecretAuthInfo authInfo;

    @Override
    public KeySecretAuthInfo getAuthInfo() {
        return authInfo;
    }

    @Override
    public Request.Builder addAuthentication(@NonNull final Request.Builder requestBuilder) {
        return requestBuilder.addHeader(AUTHORIZATION, getAuthorizationHeaderValue(getAuthInfo()));
    }

    private static String getAuthorizationHeaderValue(final KeySecretAuthInfo authInfo) {
        return new StringBuilder("Discogs key=")
                .append(URLEncoder.encode(authInfo.getKey(), StandardCharsets.UTF_8))
                .append(", secret=")
                .append(URLEncoder.encode(authInfo.getSecret(), StandardCharsets.UTF_8))
                .toString();
    }
}
