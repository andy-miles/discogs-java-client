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
package com.amilesend.discogs.connection.auth.info;

import com.amilesend.client.util.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Optional;

/**
 * Defines the authorization information that contains an access token.
 *
 * @see DiscogsAuthInfo
 */
@EqualsAndHashCode
@Getter
public class TokenAuthInfo implements DiscogsAuthInfo {
    /** The auth info type. */
    private final Type type = Type.TOKEN;
    /** The personal access token. */
    private final String token;

    /**
     * Creates a new {@code TokenAuthInfo} object.
     *
     * @param token the personal access token.
     */
    public TokenAuthInfo(final String token) {
        this.token = Optional.ofNullable(token)
                .filter(StringUtils::isNotBlank)
                .orElseThrow(() -> new IllegalArgumentException("token must not be blank"));
    }

    // Don't expose the auth token and secret.
    @Override
    public String toString() {
        return DEFINED;
    }
}
