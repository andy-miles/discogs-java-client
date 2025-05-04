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

import com.amilesend.client.connection.auth.AuthException;
import com.amilesend.client.connection.auth.AuthManager;
import com.amilesend.client.util.StringUtils;
import com.amilesend.client.util.Validate;
import com.amilesend.client.util.VisibleForTesting;
import com.amilesend.discogs.connection.RandomAlphaNumericStringGenerator;
import com.amilesend.discogs.connection.auth.info.KeySecretAuthInfo;
import com.amilesend.discogs.connection.auth.info.OAuthInfo;
import com.amilesend.discogs.connection.auth.oauth.AccessTokenResponse;
import com.amilesend.discogs.connection.auth.oauth.OAuthReceiver;
import com.amilesend.discogs.connection.auth.oauth.RequestTokenResponse;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.event.Level;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import static com.amilesend.client.connection.Connection.Headers.AUTHORIZATION;
import static com.amilesend.client.connection.Connection.Headers.CONTENT_TYPE;
import static com.amilesend.client.connection.Connection.Headers.USER_AGENT;

/**
 * Defines the auth manager for OAuth-authenticated requests.
 *
 * @see AuthManager
 * @see OAuthInfo
 */
@Slf4j
public class OAuthManager implements AuthManager<OAuthInfo> {
    private static final String REQUEST_TOKEN_URL = "https://api.discogs.com/oauth/request_token";
    private static final String AUTHORIZE_URL = "https://www.discogs.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://api.discogs.com/oauth/access_token";
    private static final String APPLICATION_FORM_URL_ENCODED_CONTENT_TYPE =
            "application/x-www-form-urlencoded";
    private static final RandomAlphaNumericStringGenerator RANDOM_STRING_GENERATOR =
            new RandomAlphaNumericStringGenerator();
    private static final int NONCE_LENGTH = 12;

    private final ReentrantLock lock = new ReentrantLock();
    private final OkHttpClient httpClient;
    private final OAuthReceiver oAuthReceiver;
    private final KeySecretAuthInfo appCredentials;
    private final String requestTokenUrl;
    private final String authorizeUrl;
    private final String accessTokenUrl;
    private final String userAgent;
    private final AtomicReference<OAuthInfo> oAuthInfo = new AtomicReference<>();

    @Builder
    private OAuthManager(
            @NonNull final OkHttpClient httpClient,
            @NonNull final OAuthReceiver oAuthReceiver,
            @NonNull final KeySecretAuthInfo appCredentials,
            final String requestTokenUrl,
            final String authorizeUrl,
            final String accessTokenUrl,
            final OAuthInfo oAuthToken,
            final String userAgent) {
        Validate.notBlank(userAgent, "userAgent must not be blank");

        this.httpClient = httpClient;
        this.oAuthReceiver = oAuthReceiver;
        this.appCredentials = appCredentials;
        this.requestTokenUrl = Optional.ofNullable(requestTokenUrl)
                .filter(StringUtils::isNotBlank)
                .orElse(REQUEST_TOKEN_URL);
        this.authorizeUrl = Optional.ofNullable(authorizeUrl)
                .filter(StringUtils::isNotBlank)
                .orElse(AUTHORIZE_URL);
        this.accessTokenUrl = Optional.ofNullable(accessTokenUrl)
                .filter(StringUtils::isNotBlank)
                .orElse(ACCESS_TOKEN_URL);
        this.userAgent = userAgent;
        this.oAuthInfo.set(oAuthToken);
    }

    @Override
    public OAuthInfo getAuthInfo() {
        OAuthInfo info = oAuthInfo.get();
        if (Objects.nonNull(info)) {
            return info;
        }

        lock.lock();
        try {
            info = authenticate();
            oAuthInfo.set(info);
            return info;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Request.Builder addAuthentication(@NonNull final Request.Builder requestBuilder) {
        final OAuthInfo authInfo = getAuthInfo();
        final String authHeaderValue = toAuthHeaderForRequestSigning(appCredentials, authInfo);
        return requestBuilder.addHeader(AUTHORIZATION, authHeaderValue);
    }

    @VisibleForTesting
    OAuthInfo authenticate() {
        final RequestTokenResponse requestToken = fetchRequestToken(appCredentials, oAuthReceiver.getRedirectUri());
        final String authVerifier = fetchAuthVerifier(requestToken);
        final AccessTokenResponse accessTokenResponse = fetchAccessToken(appCredentials, requestToken, authVerifier);
        return accessTokenResponse.toOAuthInfo();
    }

    @VisibleForTesting
    String fetchAuthVerifier(final RequestTokenResponse requestTokenResponse) {
        oAuthReceiver.start();
        try {
            browse(getAuthCodeUri(requestTokenResponse.getToken()));
            return oAuthReceiver.waitForCode();
        } finally {
            oAuthReceiver.stop();
        }
    }

    @VisibleForTesting
    RequestTokenResponse fetchRequestToken(final KeySecretAuthInfo appCredentials, final String callbackUrl) {
        final Request request = new Request.Builder()
                .url(requestTokenUrl)
                .addHeader(AUTHORIZATION, toAuthHeaderValueForRequestToken(appCredentials, callbackUrl))
                .addHeader(USER_AGENT, userAgent)
                .addHeader(CONTENT_TYPE, APPLICATION_FORM_URL_ENCODED_CONTENT_TYPE)
                .get()
                .build();
        final String requestTokenBodyValue = fetchToken(request);
        return RequestTokenResponse.parseBodyResponse(requestTokenBodyValue);
    }

    @VisibleForTesting
    AccessTokenResponse fetchAccessToken(
            final KeySecretAuthInfo appCredentials,
            final RequestTokenResponse requestTokenResponse,
            final String authVerifier) {
        final Request request = new Request.Builder()
                .url(accessTokenUrl)
                .addHeader(AUTHORIZATION,
                        toAuthHeaderValueForAccessToken(appCredentials, requestTokenResponse, authVerifier))
                .addHeader(USER_AGENT, userAgent)
                .addHeader(CONTENT_TYPE, APPLICATION_FORM_URL_ENCODED_CONTENT_TYPE)
                .post(newEmptyRequestBody())
                .build();
        final String accessTokenBodyValue = fetchToken(request);
        return AccessTokenResponse.parseBodyResponse(accessTokenBodyValue);
    }

    @VisibleForTesting
    String fetchToken(final Request request) {
        try {
            try (final Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new AuthException("Unsuccessful token request: " + response);
                }

                return response.body().string();
            }
        } catch (final AuthException ex) {
            throw ex;
        } catch (final Exception ex) {
            throw new AuthException("Error fetching token: " + ex.getMessage(), ex);
        }
    }

    @VisibleForTesting
    void browse(final String url) {
        Validate.notBlank(url, "url must not be blank");

        // Ask user to open in their browser using copy-paste
        printAndLog("Please open the following address in your browser: " + url, Level.INFO);
        try {
            if (!Desktop.isDesktopSupported()) {
                return;
            }

            final Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                return;
            }

            printAndLog("Attempting to open that address in the default browser now...", Level.INFO);
            desktop.browse(URI.create(url));
        } catch (final IOException | InternalError ex) {
            printAndLog("Unable to open browser", Level.WARN, ex);
        }
    }

    @VisibleForTesting
    void printAndLog(final String msg, final Level level) {
        printAndLog(msg, level, null);
    }

    @VisibleForTesting
    void printAndLog(final String msg, final Level level, final Throwable cause) {
        if (Objects.isNull(cause)) {
            log.atLevel(level).log(msg);
        } else {
            log.atLevel(level).log(msg, cause);
        }

        System.out.println(msg);
    }

    @VisibleForTesting
    String getAuthCodeUri(final String requestToken) {
        return new StringBuilder(authorizeUrl)
                .append("?oauth_token=")
                .append(URLEncoder.encode(requestToken, StandardCharsets.UTF_8))
                .toString();
    }

    @VisibleForTesting
    String toAuthHeaderValueForRequestToken(
            final KeySecretAuthInfo appCredentials,
            final String callbackUrl) {
        return toAuthHeaderCommon(appCredentials)
                .append("oauth_consumer_key=\"").append(appCredentials.getKey()).append("\", ")
                .append("oauth_signature=\"").append(appCredentials.getSecret()).append("&\", ")
                .append("oauth_callback=\"").append(callbackUrl).append("\"")
                .toString();
    }

    @VisibleForTesting
    String toAuthHeaderValueForAccessToken(
            final KeySecretAuthInfo appCredentials,
            final RequestTokenResponse requestTokenResponse,
            final String authVerifier) {
        return toAuthHeaderCommon(appCredentials)
                .append("oauth_token=\"").append(requestTokenResponse.getToken()).append("\", ")
                .append("oauth_signature=\"").append(appCredentials.getSecret())
                .append("&").append(requestTokenResponse.getSecret()).append("\", ")
                .append("oauth_verifier=\"").append(authVerifier).append("\"")
                .toString();
    }

    @VisibleForTesting
    String toAuthHeaderForRequestSigning(
            final KeySecretAuthInfo appCredentials,
            final OAuthInfo userTokenInfo) {
        return toAuthHeaderCommon(appCredentials)
                .append("oauth_token=\"").append(userTokenInfo.getToken()).append("\", ")
                .append("oauth_signature=\"").append(appCredentials.getSecret())
                .append("&").append(userTokenInfo.getSecret()).append("\"")
                .toString();
    }

    private static StringBuilder toAuthHeaderCommon(final KeySecretAuthInfo appCredentials) {
        return new StringBuilder("OAuth ")
                .append("oauth_consumer_key=\"").append(appCredentials.getKey()).append("\", ")
                .append("oauth_nonce=\"").append(generateNewNonce()).append("\", ")
                .append("oauth_signature_method=\"PLAINTEXT\", ")
                .append("oauth_timestamp=\"").append(System.currentTimeMillis()).append("\", ");
    }

    private static String generateNewNonce() {
        return RANDOM_STRING_GENERATOR.next(NONCE_LENGTH);
    }

    private static RequestBody newEmptyRequestBody() {
        return RequestBody.create(new byte[0]);
    }
}
