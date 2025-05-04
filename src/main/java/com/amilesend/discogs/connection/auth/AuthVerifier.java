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

/** Interface for client-side auth verification for required API calls. */
public interface AuthVerifier {
    /**
     * Checks to see if the given invocation has proper authorization configured prior to invoking the API.
     *
     * @param authManager the configured authorization manager instance for the client connection
     */
    void checkIfAuthenticated(final AuthManager<?> authManager);
}
