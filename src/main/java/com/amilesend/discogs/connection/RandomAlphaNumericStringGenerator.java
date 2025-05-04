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

import java.security.SecureRandom;

/** Utility class to generate random alphanumeric values. */
public class RandomAlphaNumericStringGenerator {
    private static final String ALPHA_NUMERICS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final SecureRandom random;

    /** Creates a new generator object */
    public RandomAlphaNumericStringGenerator() {
        random = new SecureRandom();
    }

    /**
     * Creates anew generator object with the given seed.
     *
     * @param seed the seed
     */
    public RandomAlphaNumericStringGenerator(final byte[] seed) {
        random = new SecureRandom(seed);
    }

    /**
     * Gets the next random string with the given length
     *
     * @param length the length of the random string
     * @return the random string
     */
    public String next(final int length) {
        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            sb.append(ALPHA_NUMERICS.charAt(random.nextInt(ALPHA_NUMERICS.length())));
        }

        return sb.toString();
    }
}
