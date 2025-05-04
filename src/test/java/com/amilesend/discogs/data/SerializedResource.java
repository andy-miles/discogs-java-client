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
package com.amilesend.discogs.data;

import lombok.RequiredArgsConstructor;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

@RequiredArgsConstructor
public class SerializedResource {
    private final String resourcePath;

    public InputStream getResource() {
        return new BufferedInputStream(this.getClass().getResourceAsStream(resourcePath));
    }

    public byte[] toGzipCompressedBytes() throws IOException {
        final byte[] uncompressed = getResource().readAllBytes();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(uncompressed.length);
        try(final GZIPOutputStream gos = new GZIPOutputStream(baos)) {
            gos.write(uncompressed);
            gos.flush();
        } finally {
            baos.close();
        }

        return baos.toByteArray();
    }
}
