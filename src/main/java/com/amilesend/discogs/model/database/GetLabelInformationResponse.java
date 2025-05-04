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
package com.amilesend.discogs.model.database;

import com.amilesend.discogs.model.NamedResource;
import com.amilesend.discogs.model.database.type.Image;
import com.amilesend.discogs.model.database.type.LabelResource;
import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Describes the response containing label information.
 *
 * @see NamedResource
 */
@SuperBuilder
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetLabelInformationResponse extends NamedResource<Long, GetLabelInformationResponse> {
    /** The website URI. */
    private final String uri;
    /** The api URL for the associated releases. */
    private final String releasesUrl;
    /** List of images associated with the label. */
    private final List<Image> images;
    /** Label contact information. */
    private final String contactInfo;
    /** Profile description. */
    private final String profile;
    /** Parent label information. */
    private final LabelResource parentLabel;
    /** Data quality status. */
    private final String dataQuality;
    /** List of label website URLs. */
    private final List<String> urls;
    /** List of sub-labels associated with this label. */
    @SerializedName("sublabels")
    private final List<LabelResource> subLabels;
}