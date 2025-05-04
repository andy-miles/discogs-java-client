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
package com.amilesend.discogs.connection;

import com.amilesend.client.connection.ConnectionBuilder;
import com.amilesend.client.util.StringUtils;
import com.amilesend.client.util.Validate;
import com.amilesend.discogs.parse.GsonFactory;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.amilesend.discogs.connection.DiscogsConnection.DEFAULT_BASE_URL;

/**
 * Factory used to create new {@link DiscogsConnection} instances.
 */
public class DiscogsConnectionBuilder
        extends ConnectionBuilder<DiscogsConnectionBuilder, GsonFactory, DiscogsConnection> {
    private static final Pattern USER_AGENT_PATTERN = Pattern.compile("[A-Za-z0-9]+/(\\d(\\.\\d)+)");

    @Override
    public DiscogsConnection build() {
        validateAttributes();
        validateUserAgent(getUserAgent());

        return DiscogsConnection.builder()
                .httpClient(getHttpClient())
                .gsonFactory(getGsonFactory())
                .authManager(getAuthManager())
                .baseUrl(Optional.ofNullable(getBaseUrl()).orElse(DEFAULT_BASE_URL))
                .userAgent(getUserAgent())
                .isGzipContentEncodingEnabled(isGzipContentEncodingEnabled())
                .build();
    }

    @Override
    protected void validateAttributes() {
        Validate.notNull(getHttpClient(), "httpClient must not be null");
        Validate.notNull(getGsonFactory(), "gsonFactory must not be null");
        Validate.notNull(getAuthManager(), "authManager must not be null");
        if (StringUtils.isNotBlank(getBaseUrl())) {
            Validate.isTrue(getBaseUrl().length() < MAX_BASE_URL_STR_LENGTH,
                    "baseUrl length must be less than " + MAX_BASE_URL_STR_LENGTH);
        }
        Validate.notBlank(getUserAgent(), "userAgent must not be blank");
    }

    private static void validateUserAgent(final String userAgent) {
        Validate.isTrue(
                USER_AGENT_PATTERN.matcher(userAgent).matches(),
                "UserAgent must match expected format (e.g., \"MyApp/1.0\"");
    }
}
