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
 * Defines a consumer/personal access token authorization type.
 *
 * @see DiscogsAuthInfo
 */
@Getter
@EqualsAndHashCode
public class KeySecretAuthInfo implements DiscogsAuthInfo {
    /** Consumer key/secret type. */
    private final Type type = Type.KEY_SECRET;
    /** The key. */
    private final String key;
    /** The secret. */
    private final String secret;

    /**
     * Creates a new {@code ConsumerAuthInfo} object.
     *
     * @param key the consumer key
     * @param secret the associated consumer secret
     */
    public KeySecretAuthInfo(final String key, final String secret) {
        this.key = Optional.ofNullable(key)
                .filter(StringUtils::isNotBlank)
                .orElseThrow(() -> new IllegalArgumentException("key must not be blank"));
        this.secret = Optional.ofNullable(secret)
                .filter(StringUtils::isNotBlank)
                .orElseThrow(() -> new IllegalArgumentException("secret must not be blank"));
    }

    // Don't expose the credentials. Only indicate if it's defined or not.
    @Override
    public String toString() {
        return DEFINED;
    }
}
