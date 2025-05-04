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

import com.amilesend.discogs.model.database.GetArtistReleasesResponse;
import com.amilesend.discogs.model.database.GetLabelInformationResponse;
import com.amilesend.discogs.model.database.GetLabelReleasesResponse;
import com.amilesend.discogs.model.database.GetMasterReleaseResponse;
import com.amilesend.discogs.model.database.GetMasterReleaseVersionsResponse;
import com.amilesend.discogs.model.database.SearchResponse;
import com.amilesend.discogs.model.database.type.ArtistInformation;
import com.amilesend.discogs.model.database.type.MasterReleaseVersion;
import com.amilesend.discogs.model.database.type.Member;
import com.amilesend.discogs.model.database.type.SearchResult;
import com.amilesend.discogs.model.database.type.TrackInformation;
import com.amilesend.discogs.model.type.Artist;
import com.amilesend.discogs.model.type.CatalogEntity;
import com.amilesend.discogs.model.type.Release;
import com.amilesend.discogs.model.type.ReleaseContentBase;
import lombok.experimental.UtilityClass;

import java.util.Objects;

import static com.amilesend.discogs.data.DataValidatorHelper.validateListOf;
import static com.amilesend.discogs.data.DataValidatorHelper.validateNamedResource;
import static com.amilesend.discogs.data.DataValidatorHelper.validatePaginatedResponseBase;
import static com.amilesend.discogs.data.DataValidatorHelper.validateResource;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UtilityClass
public class DatabaseApiDataValidator {

    ///////////////////////
    // GetReleaseResponse
    ///////////////////////

    public static void validateRelease(final Release expected, final Release actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateReleaseContentBase(expected, actual),
                () -> assertEquals(expected.getArtistsSort(), actual.getArtistsSort()),
                () -> assertEquals(expected.getCommunity(), actual.getCommunity()),
                () -> validateListOf(
                        expected.getCompanies(),
                        actual.getCompanies(),
                        DatabaseApiDataValidator::validateCatalogEntity),
                () -> assertEquals(expected.getCountry(), actual.getCountry()),
                () -> validateListOf(
                        expected.getExtraArtists(),
                        actual.getExtraArtists(),
                        DatabaseApiDataValidator::validateArtist),
                () -> assertEquals(expected.getFormatQuantity(), actual.getFormatQuantity()),
                () -> assertEquals(expected.getFormats(), actual.getFormats()),
                () -> assertEquals(expected.getIdentifiers(), actual.getIdentifiers()),
                () -> assertEquals(expected.getIsBlockedFromSale(), actual.getIsBlockedFromSale()),
                () -> assertEquals(expected.getIsOffensive(), actual.getIsOffensive()),
                () -> validateListOf(
                        expected.getLabels(),
                        actual.getLabels(),
                        DatabaseApiDataValidator::validateCatalogEntity),
                () -> assertEquals(expected.getMasterId(), actual.getMasterId()),
                () -> assertEquals(expected.getMasterUrl(), actual.getMasterUrl()),
                () -> assertEquals(expected.getNotes(), actual.getNotes()),
                () -> assertEquals(expected.getReleased(), actual.getReleased()),
                () -> assertEquals(expected.getReleasedFormatted(), actual.getReleasedFormatted()),
                () -> validateListOf(
                        expected.getSeries(),
                        actual.getSeries(),
                        DatabaseApiDataValidator::validateCatalogEntity),
                () -> assertEquals(expected.getStatus(), actual.getStatus()));
    }

    private static void validateReleaseContentBase(final ReleaseContentBase expected, final ReleaseContentBase actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> validateListOf(
                        expected.getArtists(),
                        actual.getArtists(),
                        DatabaseApiDataValidator::validateArtist),
                () -> assertEquals(expected.getDataQuality(), actual.getDataQuality()),
                () -> assertEquals(expected.getDateAdded(), actual.getDateAdded()),
                () -> assertEquals(expected.getDateChanged(), actual.getDateChanged()),
                () -> assertEquals(expected.getEstimatedWeight(), actual.getEstimatedWeight()),
                () -> assertEquals(expected.getGenres(), actual.getGenres()),
                () -> assertEquals(expected.getImages(), actual.getImages()),
                () -> assertEquals(expected.getLowestPrice(), actual.getLowestPrice()),
                () -> assertEquals(expected.getNumForSale(), actual.getNumForSale()),
                () -> assertEquals(expected.getStyles(), actual.getStyles()),
                () -> assertEquals(expected.getThumb(), actual.getThumb()),
                () -> validateListOf(
                        expected.getTrackList(),
                        actual.getTrackList(),
                        DatabaseApiDataValidator::validateTrackInformation),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> assertEquals(expected.getUri(), actual.getUri()),
                () -> assertEquals(expected.getVideos(), actual.getVideos()),
                () -> assertEquals(expected.getYear(), actual.getYear()));
    }

    static void validateArtist(final Artist expected, final Artist actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getAnv(), actual.getAnv()),
                () -> assertEquals(expected.getJoin(), actual.getJoin()),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getRole(), actual.getRole()),
                () -> assertEquals(expected.getTracks(), actual.getTracks()),
                () -> assertEquals(expected.getThumbnailUrl(), actual.getThumbnailUrl()));
    }

    private static void validateTrackInformation(final TrackInformation expected, final TrackInformation actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> assertEquals(expected.getPosition(), actual.getPosition()),
                () -> assertEquals(expected.getDuration(), actual.getDuration()),
                () -> assertEquals(expected.getTrack(), actual.getTrack()),
                () -> assertEquals(expected.getType(), actual.getType()),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> validateListOf(
                        expected.getExtraArtists(),
                        actual.getExtraArtists(),
                        DatabaseApiDataValidator::validateArtist));
    }

    static void validateCatalogEntity(final CatalogEntity expected, final CatalogEntity actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getCatalogNumber(), actual.getCatalogNumber()),
                () -> assertEquals(expected.getEntityType(), actual.getEntityType()),
                () -> assertEquals(expected.getEntityTypeName(), actual.getEntityTypeName()),
                () -> assertEquals(expected.getThumbnailUrl(), actual.getThumbnailUrl()));
    }

    /////////////////////////////
    // GetMasterReleaseResponse
    /////////////////////////////

    public static void validateGetMasterReleaseResponse(
            final GetMasterReleaseResponse expected,
            final GetMasterReleaseResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> validateListOf(
                        expected.getArtists(),
                        actual.getArtists(),
                        DatabaseApiDataValidator::validateArtist),
                () -> assertEquals(expected.getDataQuality(), actual.getDataQuality()),
                () -> assertEquals(expected.getDateAdded(), actual.getDateAdded()),
                () -> assertEquals(expected.getDateChanged(), actual.getDateChanged()),
                () -> assertEquals(expected.getEstimatedWeight(), actual.getEstimatedWeight()),
                () -> assertEquals(expected.getGenres(), actual.getGenres()),
                () -> assertEquals(expected.getImages(), actual.getImages()),
                () -> assertEquals(expected.getLowestPrice(), actual.getLowestPrice()),
                () -> assertEquals(expected.getNumForSale(), actual.getNumForSale()),
                () -> assertEquals(expected.getStyles(), actual.getStyles()),
                () -> assertEquals(expected.getThumb(), actual.getThumb()),
                () -> validateListOf(
                        expected.getTrackList(),
                        actual.getTrackList(),
                        DatabaseApiDataValidator::validateTrackInformation),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> assertEquals(expected.getUri(), actual.getUri()),
                () -> assertEquals(expected.getVideos(), actual.getVideos()),
                () -> assertEquals(expected.getYear(), actual.getYear()),
                () -> assertEquals(expected.getMainRelease(), actual.getMainRelease()),
                () -> assertEquals(expected.getMostRecentRelease(), actual.getMostRecentRelease()),
                () -> assertEquals(expected.getVersionsUrl(), actual.getVersionsUrl()),
                () -> assertEquals(expected.getMainReleaseUrl(), actual.getMainReleaseUrl()),
                () -> assertEquals(expected.getMostRecentReleaseUrl(), actual.getMostRecentReleaseUrl()));
    }

    /////////////////////////////////////
    // GetMasterReleaseVersionsResponse
    /////////////////////////////////////

    public static void validateGetMasterReleaseVersionsResponse(
            final GetMasterReleaseVersionsResponse expected,
            final GetMasterReleaseVersionsResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        validateListOf(
                expected.getVersions(),
                actual.getVersions(),
                DatabaseApiDataValidator::validateMasterReleaseVersion);
    }

    private static void validateMasterReleaseVersion(
            final MasterReleaseVersion expected,
            final MasterReleaseVersion actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getStatus(), actual.getStatus()),
                () -> assertEquals(expected.getStats(), actual.getStats()),
                () -> assertEquals(expected.getThumb(), actual.getThumb()),
                () -> assertEquals(expected.getFormat(), actual.getFormat()),
                () -> assertEquals(expected.getCountry(), actual.getCountry()),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> assertEquals(expected.getLabel(), actual.getLabel()),
                () -> assertEquals(expected.getReleased(), actual.getReleased()),
                () -> assertEquals(expected.getMajorFormats(), actual.getMajorFormats()),
                () -> assertEquals(expected.getCatalogNumber(), actual.getCatalogNumber()));
    }

    //////////////////////
    // ArtistInformation
    //////////////////////

    public static void validateArtistInformation(
            final ArtistInformation expected,
            final ArtistInformation actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.getUri(), actual.getUri()),
                () -> assertEquals(expected.getReleasesUrl(), actual.getReleasesUrl()),
                () -> assertEquals(expected.getImages(), actual.getImages()),
                () -> assertEquals(expected.getProfile(), actual.getProfile()),
                () -> assertEquals(expected.getUrls(), actual.getUrls()),
                () -> assertEquals(expected.getNameVariations(), actual.getNameVariations()),
                () -> validateListOf(
                        expected.getAliases(),
                        actual.getAliases(),
                        DataValidatorHelper::validateNamedResource),
                () -> validateListOf(
                        expected.getMembers(),
                        actual.getMembers(),
                        DatabaseApiDataValidator::validateMember),
                () -> assertEquals(expected.getDataQuality(), actual.getDataQuality()));
    }

    private static void validateMember(final Member expected, final Member actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateNamedResource(expected, actual),
                () -> assertEquals(expected.getActive(), actual.getActive()));
    }

    //////////////////////////////
    // GetArtistReleasesResponse
    //////////////////////////////

    public static void validateGetArtistReleasesResponse(
            final GetArtistReleasesResponse expected,
            final GetArtistReleasesResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validatePaginatedResponseBase(expected, actual),
                () -> validateListOf(
                        expected.getReleases(),
                        actual.getReleases(),
                        DatabaseApiDataValidator::validateArtistRelease));
    }

    private static void validateArtistRelease(
            final GetArtistReleasesResponse.ArtistRelease expected,
            final GetArtistReleasesResponse.ArtistRelease actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getStatus(), actual.getStatus()),
                () -> assertEquals(expected.getType(), actual.getType()),
                () -> assertEquals(expected.getMainRelease(), actual.getMainRelease()),
                () -> assertEquals(expected.getFormat(), actual.getFormat()),
                () -> assertEquals(expected.getLabel(), actual.getLabel()),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> assertEquals(expected.getRole(), actual.getRole()),
                () -> assertEquals(expected.getArtist(), actual.getArtist()),
                () -> assertEquals(expected.getYear(), actual.getYear()),
                () -> assertEquals(expected.getThumb(), actual.getThumb()),
                () -> assertEquals(expected.getStats(), actual.getStats()));
    }

    ////////////////////////////////
    // GetLabelInformationResponse
    ////////////////////////////////

    public static void validateGetLabelInformationResponse(
            final GetLabelInformationResponse expected,
            final GetLabelInformationResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateNamedResource(expected, actual),
                () -> assertEquals(expected.getUri(), actual.getUri()),
                () -> assertEquals(expected.getReleasesUrl(), actual.getReleasesUrl()),
                () -> assertEquals(expected.getImages(), actual.getImages()),
                () -> assertEquals(expected.getContactInfo(), actual.getContactInfo()),
                () -> assertEquals(expected.getProfile(), actual.getProfile()),
                () -> validateNamedResource(expected.getParentLabel(), actual.getParentLabel()),
                () -> assertEquals(expected.getDataQuality(), actual.getDataQuality()),
                () -> assertEquals(expected.getUrls(), actual.getUrls()),
                () -> validateListOf(
                        expected.getSubLabels(),
                        actual.getSubLabels(),
                        DataValidatorHelper::validateNamedResource));
    }

    /////////////////////////////
    // GetLabelReleasesResponse
    /////////////////////////////

    public static void validateGetLabelReleasesResponse(
            final GetLabelReleasesResponse expected,
            final GetLabelReleasesResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validatePaginatedResponseBase(expected, actual),
                () -> validateListOf(
                        expected.getReleases(),
                        actual.getReleases(),
                        DatabaseApiDataValidator::validateLabelRelease));
    }

    private static void validateLabelRelease(
            final GetLabelReleasesResponse.LabelRelease expected,
            final GetLabelReleasesResponse.LabelRelease actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getStatus(), actual.getStatus()),
                () -> assertEquals(expected.getFormat(), actual.getFormat()),
                () -> assertEquals(expected.getCatalogNumber(), actual.getCatalogNumber()),
                () -> assertEquals(expected.getThumb(), actual.getThumb()),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> assertEquals(expected.getArtist(), actual.getArtist()),
                () -> assertEquals(expected.getStats(), actual.getStats()),
                () -> assertEquals(expected.getYear(), actual.getYear()));
    }

    ///////////////////
    // SearchResponse
    ///////////////////

    public static void validateSearchResponse(
            final SearchResponse expected,
            final SearchResponse actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validatePaginatedResponseBase(expected, actual),
                () -> validateListOf(
                        expected.getResults(),
                        actual.getResults(),
                        DatabaseApiDataValidator::validateSearchResult));
    }

    private static void validateSearchResult(
            final SearchResult expected,
            final SearchResult actual) {
        if (Objects.isNull(expected)) {
            assertNull(actual);
            return;
        }

        assertAll(
                () -> validateResource(expected, actual),
                () -> assertEquals(expected.getStyle(), actual.getStyle()),
                () -> assertEquals(expected.getBarcode(), actual.getBarcode()),
                () -> assertEquals(expected.getThumb(), actual.getThumb()),
                () -> assertEquals(expected.getTitle(), actual.getTitle()),
                () -> assertEquals(expected.getCountry(), actual.getCountry()),
                () -> assertEquals(expected.getFormat(), actual.getFormat()),
                () -> assertEquals(expected.getUri(), actual.getUri()),
                () -> assertEquals(expected.getCommunity(), actual.getCommunity()),
                () -> assertEquals(expected.getLabel(), actual.getLabel()),
                () -> assertEquals(expected.getCatalogNumber(), actual.getCatalogNumber()),
                () -> assertEquals(expected.getYear(), actual.getYear()),
                () -> assertEquals(expected.getGenre(), actual.getGenre()),
                () -> assertEquals(expected.getType(), actual.getType()),
                () -> assertEquals(expected.getUserData(), actual.getUserData()),
                () -> assertEquals(expected.getMasterId(), actual.getMasterId()),
                () -> assertEquals(expected.getMasterUrl(), actual.getMasterUrl()),
                () -> assertEquals(expected.getCoverImage(), actual.getCoverImage()),
                () -> assertEquals(expected.getFormatQuantity(), actual.getFormatQuantity()),
                () -> assertEquals(expected.getFormats(), actual.getFormats()));
    }
}
