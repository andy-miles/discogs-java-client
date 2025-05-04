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
package com.amilesend.discogs.model.inventory.type;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.nio.file.Path;

/** Information about a downloaded file. */
@SuperBuilder
@Data
public class DownloadInformation {
    /** The filename of the file. */
    private final String fileName;
    /** The file size in bytes. */
    private final long sizeBytes;
    /** The amount of bytes actually downloaded. */
    private final long downloadedBytes;
    /** The Path to the downloaded file. */
    private final Path downloadPath;
}
