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
package com.amilesend.discogs;

import com.amilesend.client.connection.http.OkHttpClientBuilder;
import com.amilesend.client.util.Validate;
import com.amilesend.discogs.api.DatabaseApi;
import com.amilesend.discogs.api.InventoryExportApi;
import com.amilesend.discogs.api.InventoryUploadApi;
import com.amilesend.discogs.api.MarketplaceApi;
import com.amilesend.discogs.api.UserCollectionApi;
import com.amilesend.discogs.api.UserIdentityApi;
import com.amilesend.discogs.api.UserListsApi;
import com.amilesend.discogs.api.UserWantListApi;
import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.connection.DiscogsConnectionBuilder;
import com.amilesend.discogs.connection.auth.AuthManagerFactory;
import com.amilesend.discogs.connection.auth.OAuthManager;
import com.amilesend.discogs.connection.auth.info.KeySecretAuthInfo;
import com.amilesend.discogs.connection.auth.info.OAuthInfo;
import com.amilesend.discogs.connection.auth.info.TokenAuthInfo;
import com.amilesend.discogs.parse.GsonFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;

import java.util.Objects;
import java.util.Optional;

import static com.amilesend.discogs.connection.DiscogsConnection.DEFAULT_BASE_URL;

/**
 * A helper class to vend API classes that are associated with a {@link DiscogsConnection} to the Discogs service.
 *
 * @see DiscogsConnection
 */
@RequiredArgsConstructor
public class Discogs {
    /** The HTTP client connection. */
    @NonNull
    @Getter
    private final DiscogsConnection connection;

    /**
     * Creates a new unauthenticated {@code Discogs} instance.
     *
     * @param userAgent the user agent for your application
     * @return the Discogs instance
     */
    public static Discogs newUnauthenticatedInstance(final String userAgent) {
        Validate.notBlank(userAgent, "userAgent must not be blank");

        return new Discogs(new DiscogsConnectionBuilder()
                .httpClient(new OkHttpClientBuilder().build())
                .authManager(new AuthManagerFactory().newUnauthenticatedAuthManager())
                .baseUrl(DEFAULT_BASE_URL)
                .gsonFactory(new GsonFactory())
                .isGzipContentEncodingEnabled(true)
                .userAgent(userAgent)
                .build());
    }

    /**
     * Creates a new user-provided key-secret authenticated {@code Discogs} instance.
     *
     * @param key the key
     * @param secret the secret
     * @param userAgent the user agent for your application
     * @return the Discogs instance
     */
    public static Discogs newKeySecretAuthenticatedInstance(
            final String key,
            final String secret,
            final String userAgent) {
        Validate.notBlank(key, "key must not be blank");
        Validate.notBlank(secret, "secret must not be blank");
        Validate.notBlank(userAgent, "userAgent must not be blank");

        return new Discogs(new DiscogsConnectionBuilder()
                .httpClient(new OkHttpClientBuilder().build())
                .authManager(new AuthManagerFactory()
                        .newUserAuthenticatedAuthManager(new KeySecretAuthInfo(key, secret)))
                .baseUrl(DEFAULT_BASE_URL)
                .gsonFactory(new GsonFactory())
                .isGzipContentEncodingEnabled(true)
                .userAgent(userAgent)
                .build());
    }

    /**
     * Creates a new user-provided personal token based authenticated {@code Discogs} instance.
     *
     * @param token the personal access token
     * @param userAgent the user agent for your application
     * @return the Discogs instance
     */
    public static Discogs newTokenAuthenticatedInstance(final String token, final String userAgent) {
        Validate.notBlank(token, "token must not be blank");
        Validate.notBlank(userAgent, "userAgent must not be blank");

        return new Discogs(new DiscogsConnectionBuilder()
                .httpClient(new OkHttpClientBuilder().build())
                .authManager(new AuthManagerFactory()
                        .newTokenAuthenticatedAuthManager(new TokenAuthInfo(token)))
                .baseUrl(DEFAULT_BASE_URL)
                .gsonFactory(new GsonFactory())
                .isGzipContentEncodingEnabled(true)
                .userAgent(userAgent)
                .build());
    }

    /**
     * Creates a new OAuth authenticated {@code Discogs} instance. This will initiate the OAuth flow to fetch the
     * OAuth token for the user's behalf.
     *
     * @param appKeySecret the application key and secret
     * @param userAgent the user agent for your application
     * @param oAuthReceiverPort the port for listening on the OAuth redirect callback
     * @return the Discogs instance
     */
    public static Discogs newOAuthInstance(
            final KeySecretAuthInfo appKeySecret,
            final String userAgent,
            final int oAuthReceiverPort) {
        return newOAuthInstance(appKeySecret, userAgent, oAuthReceiverPort, null);
    }

    /**
     * Creates a new OAuth authenticated {@code Discogs} instance. This will initiate the OAuth flow to fetch the
     * OAuth token for the user's behalf.
     *
     * @param appKeySecret the application key and secret
     * @param userAgent the user agent for your application
     * @param oAuthReceiverPort the port for listening on the OAuth redirect callback
     * @param oAuthToken the existing user OAuth token
     * @return the Discogs instance
     */
    public static Discogs newOAuthInstance(
            final KeySecretAuthInfo appKeySecret,
            final String userAgent,
            final int oAuthReceiverPort,
            final OAuthInfo oAuthToken) {
        final OkHttpClient httpClient = new OkHttpClientBuilder().build();
        final OAuthManager authManager = Objects.isNull(oAuthToken)
                ? new AuthManagerFactory().newOAuthAuthManager(
                        httpClient,
                        appKeySecret,
                        userAgent,
                        oAuthReceiverPort)
                : new AuthManagerFactory().newOAuthAuthManagerWithExistingToken(
                        httpClient,
                        appKeySecret,
                        userAgent,
                        oAuthReceiverPort,
                        oAuthToken);
        return new Discogs(new DiscogsConnectionBuilder()
                .userAgent(userAgent)
                .gsonFactory(new GsonFactory())
                .authManager(authManager)
                .baseUrl(DEFAULT_BASE_URL)
                .isGzipContentEncodingEnabled(true)
                .httpClient(httpClient)
                .build());
    }

    /**
     * Gets a new {@link UserIdentityApi} instance for the client connection.
     *
     * @return the user identity API interface implementation
     * @see UserIdentityApi
     */
    public UserIdentityApi getUserIdentityApi() {
        return new UserIdentityApi(connection);
    }

    /**
     * Gets a new {@link DatabaseApi} instance for the client connection.
     *
     * @return the database API interface implementation
     * @see DatabaseApi
     */
    public DatabaseApi getDatabaseApi() {
        return new DatabaseApi(connection);
    }

    /**
     * Gets a new {@link MarketplaceApi} instance for the client connection.
     *
     * @return the marketplace API interface implementation
     * @see MarketplaceApi
     */
    public MarketplaceApi getMarketplaceApi() {
        return new MarketplaceApi(connection);
    }

    /**
     * Gets a new {@link UserCollectionApi} instance for the client connection.
     *
     * @return the user collection API interface implementation
     * @see UserIdentityApi
     */
    public UserCollectionApi getUserCollectionApi() {
        return new UserCollectionApi(connection);
    }

    /**
     * Gets a new {@link UserWantListApi} instance for the client connection.
     *
     * @return the user want list API interface implementation
     * @see UserWantListApi
     */
    public UserWantListApi getUserWantListApi() {
        return new UserWantListApi(connection);
    }

    /**
     * Gets a new {@link UserListsApi} instance for the client connection.
     *
     * @return the user lists API interface implementation
     * @see UserListsApi
     */
    public UserListsApi getUserListsApi() {
        return new UserListsApi(connection);
    }

    /**
     * Gets a new {@link InventoryExportApi} instance for the client connection.
     *
     * @return the inventory export API interface implementation
     * @see InventoryExportApi
     */
    public InventoryExportApi getInventoryExportApi() {
        return new InventoryExportApi(connection);
    }

    /**
     * Gets a new {@link InventoryUploadApi} instance for the client connection.
     *
     * @return the inventory upload API interface implementation
     * @see InventoryUploadApi
     */
    public InventoryUploadApi getInventoryUploadApi() {
        return new InventoryUploadApi(connection);
    }
}
