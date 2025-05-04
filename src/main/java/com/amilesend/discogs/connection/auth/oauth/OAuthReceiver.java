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

import com.amilesend.client.util.StringUtils;
import com.amilesend.client.util.Validate;
import com.amilesend.client.util.VisibleForTesting;
import com.sun.net.httpserver.HttpServer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * A customized OAuth receiver that handles the OAuth token exchange redirect by hosting a HTTP server
 * to capture the auth and/or error code specific to Box.net OAuth flow.
 *
 * This is based on <a href="https://t.ly/4YOIa">LocalServerReceiver</a> defined in the
 * <a href="https://t.ly/Ymraz">google-oauth-java-client</a> library.
 */
@Slf4j
public class OAuthReceiver implements AutoCloseable {
    private static final int DEFAULT_SYSTEM_BACKLOG = 0;

    /** Max allowed port range for listening for the OAuth redirect. */
    @VisibleForTesting
    static final int MAX_PORT_RANGE = 65535;

    @VisibleForTesting
    @Getter(value = AccessLevel.PACKAGE)
    private final OAuthReceiverCallback callback;
    /** The host of the receiver. */
    @Getter
    private final String host;
    /** The path to listen for the redirect. */
    @Getter
    private final String callbackPath;
    /** The port of the receiver to listen on. */
    @Getter
    private final int port;
    private HttpServer server;

    /**
     * Builds a new {@code OAuthReceiver}.
     *
     * @param host host of the receiver (Default: {@literal localhost})
     * @param port optional port of the receiver to listen on
     * @param callbackPath the path to listen for the redirect (Default: {@literal /Callback})
     * @param successLandingPageUrl optional URL for a custom successful landing page
     * @param failureLandingPageUrl optional URL for a custom failure landing page
     */
    @Builder
    protected OAuthReceiver(
            final String host,
            final int port,
            final String callbackPath,
            final String successLandingPageUrl,
            final String failureLandingPageUrl) {
        this.host = StringUtils.isNotBlank(host) ? host : "localhost";
        this.callbackPath = StringUtils.isNotBlank(callbackPath) ? callbackPath : "/Callback";
        validatePort(port);
        this.port = port;
        callback = OAuthReceiverCallback.builder()
                .callbackPath(this.callbackPath)
                .successLandingPageUrl(successLandingPageUrl)
                .failureLandingPageUrl(failureLandingPageUrl)
                .build();
    }

    /**
     * Starts the HTTP server to handle OAuth callbacks.
     *
     * @throws OAuthReceiverException if an error occurred while starting the HTTP server
     */
    public void start() {
        if (server != null) {
            return;
        }

        try {
            server = HttpServer.create(new InetSocketAddress(port), DEFAULT_SYSTEM_BACKLOG);
            server.createContext(callbackPath, callback);
            server.setExecutor(null);
            server.start();
        } catch (final Exception ex) {
            throw new OAuthReceiverException("Error starting OAuthReceiver", ex);
        }
    }

    /**
     * Stops the running HTTP server.
     *
     * @throws OAuthReceiverException if an error occurred while stopping the server
     */
    public void stop() {
        callback.releaseLock();
        if (server != null) {
            try {
                server.stop(0);
            } catch (final Exception ex) {
                throw new OAuthReceiverException("Error stopping OAuthReceiver", ex);
            }
            server = null;
        }
    }

    /**
     * Closes the HTTP server resource.
     *
     * @throws OAuthReceiverException if an error occurred while closing the HTTP server resource
     */
    @Override
    public void close() {
        stop();
    }

    /**
     * Gets the redirect URI based on the running HTTP server resource.
     *
     * @return the redirect URI
     */
    public String getRedirectUri() {
        return new StringBuilder("http://")
                .append(getHost())
                .append(":")
                .append(port)
                .append(callbackPath)
                .toString();
    }

    /**
     * Blocks until the server receives a login result, or the server is stopped by {@link #stop()},
     * to return an authorization code.
     *
     * @return authorization code if login succeeds; may return {@code null} if the server is stopped
     *     by {@link #close()}
     * @throws OAuthReceiverException if the server receives an error code (through an HTTP request parameter
     *     {@code error})
     */
    public String waitForCode() {
        return callback.waitForCode();
    }

    private static void validatePort(final int port) {
        Validate.isTrue(port > 0 && port <= MAX_PORT_RANGE,
                "Invalid port.  Must be between 0 and " + MAX_PORT_RANGE);
    }
}
