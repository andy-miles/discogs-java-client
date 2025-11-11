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
package com.amilesend.discogs.connection.auth.info;

import com.amilesend.client.connection.auth.AuthInfo;
import com.amilesend.discogs.parse.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Defines the base interface for Discogs authorization information.
 *
 * Per the <a href="https://www.discogs.com/developers#page:authentication,header:authentication-discogs-auth-flow">
 * Discogs API documentation</a>, the following table outlines the authorization types:
 * <br/>
 * <table>
 *     <tr>
 *         <th>Type</th>
 *         <th>Rate Limit Tier</th>
 *         <th>Image URLs</th>
 *         <td>Auth as User</td>
 *     </tr>
 *     <tr>
 *         <td>{@link Type#NO_AUTH_LIMITED}</td>
 *         <td>Low</td>
 *         <td>N</td>
 *         <td>N</td>
 *     </tr>
 *     <tr>
 *         <td>{@link Type#KEY_SECRET}</td>
 *         <td>High</td>
 *         <td>Y</td>
 *         <td>N</td>
 *     </tr>
 *     <tr>
 *         <td>{@link Type#OAUTH_1_0_A}</td>
 *         <td>High</td>
 *         <td>Y</td>
 *         <td>Y</td>
 *     </tr>
 *     <tr>
 *         <td>{@link Type#TOKEN}</td>
 *         <td>High</td>
 *         <td>Y</td>
 *         <td>Y</td>
 *     </tr>
 * </table>
 */
public interface DiscogsAuthInfo extends AuthInfo {
    String DEFINED = "**********";

    /**
     * Deserializes the given {@code authInfoJson} string to a new {@code AuthInfo} object.
     *
     * @param authInfoJson the JSON-formatted auth info
     * @return the new {@code AuthInfo} object
     * @throws JsonSyntaxException if there is an error while deserializing the JSON string
     */
    static DiscogsAuthInfo fromJson(final String authInfoJson) {
        final Gson gson = GsonFactory.getGsonForAuthentication();
        return gson.fromJson(authInfoJson, DiscogsAuthInfo.class);
    }

    /**
     * Serializes this {@code AuthInfo} to a JSON formatted string.
     *
     * @return the JSON formatted {@code AuthInfo}
     */
    default String toJson() {
        final Gson gson = GsonFactory.getGsonForAuthentication();
        return gson.toJson(this);
    }

    /**
     * Gets the {@code AuthInfo} type.
     *
     * @return the type
     * @see Type
     */
    Type getType();

    /** Defines the supported authorization types. */
    enum Type {
        /** No credentials. */
        NO_AUTH_LIMITED,
        /** Consumer key and secret pair. */
        KEY_SECRET,
        /** OAuth 1.0a token and secret. */
        OAUTH_1_0_A,
        /** Personal access token. */
        TOKEN;
    }
}
