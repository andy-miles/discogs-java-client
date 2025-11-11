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
package com.amilesend.discogs.api;

import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.model.Api;
import com.amilesend.discogs.model.AuthenticationRequired;
import com.amilesend.discogs.model.database.DeleteUserReleaseRequest;
import com.amilesend.discogs.model.database.GetArtistInformationRequest;
import com.amilesend.discogs.model.database.GetArtistInformationResponse;
import com.amilesend.discogs.model.database.GetArtistReleasesRequest;
import com.amilesend.discogs.model.database.GetArtistReleasesResponse;
import com.amilesend.discogs.model.database.GetCommunityReleaseRatingRequest;
import com.amilesend.discogs.model.database.GetCommunityReleaseRatingResponse;
import com.amilesend.discogs.model.database.GetLabelInformationRequest;
import com.amilesend.discogs.model.database.GetLabelInformationResponse;
import com.amilesend.discogs.model.database.GetLabelReleasesRequest;
import com.amilesend.discogs.model.database.GetLabelReleasesResponse;
import com.amilesend.discogs.model.database.GetMasterReleaseRequest;
import com.amilesend.discogs.model.database.GetMasterReleaseResponse;
import com.amilesend.discogs.model.database.GetMasterReleaseVersionsRequest;
import com.amilesend.discogs.model.database.GetMasterReleaseVersionsResponse;
import com.amilesend.discogs.model.database.GetReleaseRequest;
import com.amilesend.discogs.model.database.GetReleaseResponse;
import com.amilesend.discogs.model.database.GetUserReleaseRatingRequest;
import com.amilesend.discogs.model.database.GetUserReleaseRatingResponse;
import com.amilesend.discogs.model.database.SearchRequest;
import com.amilesend.discogs.model.database.SearchResponse;
import com.amilesend.discogs.model.database.UpdateUserReleaseRatingRequest;
import com.amilesend.discogs.model.database.UpdateUserReleaseRatingResponse;
import lombok.NonNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * The Discogs Database API.
 * <br/>
 * <a href="https://www.discogs.com/developers#page:database">API Documentation</a>
 * <br/>
 * Note: The GET Release Stats API is broken and does not provide meaningful data.  It is therefore omitted from this
 * API interface. See <a href="https://www.discogs.com/forum/thread/865093?message_id=8630743#8630743"> this
 * forum post</a> for context.
 *
 * @see ApiBase
 */
@Api
public class DatabaseApi extends ApiBase {
    private static final String RELEASES_API_PATH = "/releases/";
    private static final String MASTERS_API_PATH = "/masters/";
    private static final String LABELS_API_PATH = "/labels/";
    private static final String ARTISTS_API_PATH = "/artists/";

    /**
     * Creates a new {@code DatabaseApi} object.
     *
     * @param connection the underlying client connection
     */
    public DatabaseApi(final DiscogsConnection connection) {
        super(connection);
    }

    /**
     * Gets a release.
     *
     * @param request the request
     * @return the release
     * @see GetReleaseRequest
     * @see GetReleaseResponse
     */
    public GetReleaseResponse getRelease(@NonNull final GetReleaseRequest request) {
        final String subPath = new StringBuilder(RELEASES_API_PATH)
                .append(request.getReleaseId())
                .toString();
        return executeGet(subPath, request, GetReleaseResponse.class);
    }

    /**
     * Gets a release rating for a user.
     *
     * @param request the request
     * @return the response
     * @see GetUserReleaseRatingRequest
     * @see GetUserReleaseRatingResponse
     */
    public GetUserReleaseRatingResponse getUserReleaseRating(@NonNull final GetUserReleaseRatingRequest request) {
        final String subPath = new StringBuilder(RELEASES_API_PATH)
                .append(request.getReleaseId())
                .append("/rating/")
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .toString();
        return executeGet(subPath, request, GetUserReleaseRatingResponse.class);
    }

    /**
     * Updates the release rating for a user.
     *
     * @param request the request
     * @return the response
     * @see UpdateUserReleaseRatingRequest
     * @see UpdateUserReleaseRatingResponse
     */
    @AuthenticationRequired
    public UpdateUserReleaseRatingResponse updateUserReleaseRating(
            @NonNull final UpdateUserReleaseRatingRequest request) {
        final String subPath = new StringBuilder(RELEASES_API_PATH)
                .append(request.getReleaseId())
                .append("/rating/")
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .toString();
        return executePut(subPath, request, UpdateUserReleaseRatingResponse.class);
    }

    /**
     * Deletes a release rating for a user.
     *
     * @param request the request
     */
    @AuthenticationRequired
    public void deleteUserReleaseRating(@NonNull final DeleteUserReleaseRequest request) {
        final String subPath = new StringBuilder(RELEASES_API_PATH)
                .append(request.getReleaseId())
                .append("/rating/")
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .toString();
        executeDelete(subPath, request);
    }

    /**
     * Gets the community rating for a given release.
     *
     * @param request the request
     * @return the response
     * @see GetCommunityReleaseRatingRequest
     * @see GetCommunityReleaseRatingResponse
     */
    public GetCommunityReleaseRatingResponse getCommunityReleaseRating(
            @NonNull final GetCommunityReleaseRatingRequest request) {
        final String subPath = new StringBuilder(RELEASES_API_PATH)
                .append(request.getReleaseId())
                .append("/rating")
                .toString();
        return executeGet(subPath, request, GetCommunityReleaseRatingResponse.class);
    }

    /**
     * Gets the master release for the given identifier.
     *
     * @param request the request
     * @return the response
     * @see GetMasterReleaseRequest
     * @see GetMasterReleaseResponse
     */
    public GetMasterReleaseResponse getMasterRelease(@NonNull final GetMasterReleaseRequest request) {
        final String subPath = new StringBuilder(MASTERS_API_PATH)
                .append(request.getMasterId())
                .toString();
        return executeGet(subPath, request, GetMasterReleaseResponse.class);
    }

    /**
     * Gets the master release versions.
     *
     * @param request the request
     * @return the master release versions
     * @see GetMasterReleaseVersionsRequest
     * @see GetMasterReleaseVersionsResponse
     */
    public GetMasterReleaseVersionsResponse getMasterReleaseVersions(
            @NonNull final GetMasterReleaseVersionsRequest request) {
        final String subPath = new StringBuilder(MASTERS_API_PATH)
                .append(request.getMasterId())
                .append("/versions")
                .toString();
        return executeGet(subPath, request, GetMasterReleaseVersionsResponse.class);
    }

    /**
     * Gets information about a specific artists
     *
     * @param request the request
     * @return the response
     * @see GetArtistInformationRequest
     * @see GetArtistInformationResponse
     */
    public GetArtistInformationResponse getArtistInformation(@NonNull final GetArtistInformationRequest request) {
        final String subPath = new StringBuilder(ARTISTS_API_PATH)
                .append(request.getArtistId())
                .toString();
        return executeGet(subPath, request, GetArtistInformationResponse.class);
    }

    /**
     * Gets the list of releases and masters associated with an artist.
     *
     * @param request the request
     * @return the paginated response
     * @see GetArtistReleasesRequest
     * @see GetArtistReleasesResponse
     */
    public GetArtistReleasesResponse getArtistReleases(@NonNull final GetArtistReleasesRequest request) {
        final String subPath = new StringBuilder(ARTISTS_API_PATH)
                .append(request.getArtistId())
                .append("/releases")
                .toString();
        return executeGet(subPath, request, GetArtistReleasesResponse.class);
    }

    /**
     * Gets information about a specific abel.
     *
     * @param request the request
     * @return the response
     * @see GetLabelInformationRequest
     * @see GetMasterReleaseResponse
     */
    public GetLabelInformationResponse getLabelInformation(@NonNull final GetLabelInformationRequest request) {
        final String subPath = new StringBuilder(LABELS_API_PATH)
                .append(request.getLabelId())
                .toString();
        return executeGet(subPath, request, GetLabelInformationResponse.class);
    }

    /**
     * Gets the paginated list of releases for a label.
     *
     * @param request the request
     * @return the paginated response
     * @see GetLabelReleasesRequest
     * @see GetLabelReleasesResponse
     */
    public GetLabelReleasesResponse getLabelReleases(@NonNull final GetLabelReleasesRequest request) {
        final String subPath = new StringBuilder(LABELS_API_PATH)
                .append(request.getLabelId())
                .append("/releases")
                .toString();
        return executeGet(subPath, request, GetLabelReleasesResponse.class);
    }

    /**
     * Searches for masters, releases, artists, and labels.
     *
     * @param request the request
     * @return the search response
     * @see SearchRequest
     * @see SearchResponse
     */
    @AuthenticationRequired
    public SearchResponse search(@NonNull final SearchRequest request) {
        return executeGet("/database/search", request, SearchResponse.class);
    }
}
