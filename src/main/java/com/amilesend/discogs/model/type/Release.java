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
package com.amilesend.discogs.model.type;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

/**
 * Describes a release.
 *
 * @see ReleaseContentBase
 */
@SuperBuilder
@Getter
@ToString(callSuper = true)
public class Release extends ReleaseContentBase {
    /** The artist name that is used for sorting this release. */
    private final String artistsSort;
    /**
     * The associated community information.
     *
     * @see Community
     */
    private final Community community;
    /**
     * The list of companies (i.e., record labels).
     *
     * @see CatalogEntity
     */
    private final List<CatalogEntity> companies;
    /** The country of release. */
    private final String country;
    /** The list of extra artists. */
    @SerializedName("extraartists")
    private final List<Artist> extraArtists;
    /** The quantity of formats for the release. */
    private final Integer formatQuantity;
    /**
     * The list of formats.
     *
     * @see Format
     */
    private final List<Format> formats;
    /**
     * The list of release identifiers (e.g., barcodes)
     *
     * @see ReleaseIdentifier
     */
    private final List<ReleaseIdentifier> identifiers;
    @SerializedName("blocked_from_sale")
    private final Boolean isBlockedFromSale;
    private final Boolean isOffensive;
    /**
     * List of associated labels.
     *
     * @see CatalogEntity
     */
    private final List<CatalogEntity> labels;
    /** The associated master identifier. */
    private final Long masterId;
    /** The associated master URL. */
    private final String masterUrl;
    /** Notes. */
    private final String notes;
    /** The release date. */
    private final LocalDate released;
    /** The release date formatted as DD Mon yyyy. */
    private final String releasedFormatted;
    /**
     * List of associated series.
     *
     * @see CatalogEntity
     */
    private final List<CatalogEntity> series;
    /** Release entry status. */
    private final String status;
}
