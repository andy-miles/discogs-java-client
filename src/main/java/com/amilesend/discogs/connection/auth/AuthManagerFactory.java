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
import com.amilesend.client.connection.auth.NoOpAuthManager;
import com.amilesend.discogs.connection.auth.info.KeySecretAuthInfo;
import com.amilesend.discogs.connection.auth.info.OAuthInfo;
import com.amilesend.discogs.connection.auth.info.TokenAuthInfo;
import com.amilesend.discogs.connection.auth.oauth.OAuthReceiver;
import okhttp3.OkHttpClient;

/**
 * Factory used to vend {@link AuthManager} instances used for Discogs.
 *
 * @see AuthManager
 */
public class AuthManagerFactory {
    /**
     * Creates a new auth manager for unauthenticated requests.
     *
     * @return the no-op auth manager
     * @see NoOpAuthManager
     */
    public NoOpAuthManager newUnauthenticatedAuthManager() {
        return new NoOpAuthManager();
    }

    /**
     * Creates a new auth manager for a user-supplied key and secret.
     *
     * @param authInfo the auth info containing the key and secret
     * @return the key-secret-based auth manager
     * @see KeySecretAuthInfo
     * @see KeySecretAuthManager
     */
    public KeySecretAuthManager newUserAuthenticatedAuthManager(final KeySecretAuthInfo authInfo) {
        return new KeySecretAuthManager(authInfo);
    }

    /**
     * Creates a new auth manager for a user-supplied access token and secret.
     *
     * @param authInfo the auth info containing the token and secret
     * @return the token-based auth manager
     * @see TokenAuthInfo
     * @see TokenAuthManager
     */
    public TokenAuthManager newTokenAuthenticatedAuthManager(final TokenAuthInfo authInfo) {
        return new TokenAuthManager(authInfo);
    }

    /**
     * Creates a new OAuth-based auth manager to execute the OAuth flow to acquire the OAuth access token.
     *
     * @param httpClient the configured OkHttp client connection
     * @param appCredentials the application credentials in the form of a key and secret
     * @param userAgent the user agent identifier
     * @param receiverPort the port of the OAuth callback listener is listening on
     * @return the OAuth-based auth manager
     * @see KeySecretAuthInfo
     * @see OAuthManager
     */
    public OAuthManager newOAuthAuthManager(
            final OkHttpClient httpClient,
            final KeySecretAuthInfo appCredentials,
            final String userAgent,
            final int receiverPort) {
        return newOAuthAuthManagerWithExistingToken(httpClient, appCredentials, userAgent, receiverPort, null);
    }

    /**
     * Creates a new OAuth-based auth manager for an existing persisted OAuth token.
     *
     * @param httpClient the configured OkHttp client connection
     * @param appCredentials the application credentials in the form of a key and secret
     * @param userAgent the user agent identifier
     * @param receiverPort the port of the OAuth callback listener is listening on
     * @param authInfo the OAuth token information
     * @return the OAuth-based auth manager
     * @see KeySecretAuthInfo
     * @see OAuthInfo
     * @see OAuthManager
     */
    public OAuthManager newOAuthAuthManagerWithExistingToken(
            final OkHttpClient httpClient,
            final KeySecretAuthInfo appCredentials,
            final String userAgent,
            final int receiverPort,
            final OAuthInfo authInfo) {
        final OAuthReceiver receiver = OAuthReceiver.builder()
                .port(receiverPort)
                .build();
        return OAuthManager.builder()
                .httpClient(httpClient)
                .oAuthReceiver(receiver)
                .appCredentials(appCredentials)
                .userAgent(userAgent)
                .oAuthToken(authInfo)
                .build();
    }
}
