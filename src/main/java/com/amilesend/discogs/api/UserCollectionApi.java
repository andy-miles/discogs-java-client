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
import com.amilesend.discogs.model.AuthenticationOptional;
import com.amilesend.discogs.model.AuthenticationRequired;
import com.amilesend.discogs.model.collection.AddToFolderRequest;
import com.amilesend.discogs.model.collection.AddToFolderResponse;
import com.amilesend.discogs.model.collection.ChangeReleaseRatingRequest;
import com.amilesend.discogs.model.collection.CreateFolderRequest;
import com.amilesend.discogs.model.collection.CreateFolderResponse;
import com.amilesend.discogs.model.collection.DeleteFolderRequest;
import com.amilesend.discogs.model.collection.DeleteInstanceRequest;
import com.amilesend.discogs.model.collection.EditInstanceFieldRequest;
import com.amilesend.discogs.model.collection.GetCollectionItemsByFolderRequest;
import com.amilesend.discogs.model.collection.GetCollectionItemsByFolderResponse;
import com.amilesend.discogs.model.collection.GetCollectionItemsByReleaseRequest;
import com.amilesend.discogs.model.collection.GetCollectionItemsByReleaseResponse;
import com.amilesend.discogs.model.collection.GetCollectionValueRequest;
import com.amilesend.discogs.model.collection.GetCollectionValueResponse;
import com.amilesend.discogs.model.collection.GetCustomFieldsRequest;
import com.amilesend.discogs.model.collection.GetCustomFieldsResponse;
import com.amilesend.discogs.model.collection.GetFolderRequest;
import com.amilesend.discogs.model.collection.GetFolderResponse;
import com.amilesend.discogs.model.collection.GetFoldersRequest;
import com.amilesend.discogs.model.collection.GetFoldersResponse;
import com.amilesend.discogs.model.collection.MoveReleaseRequest;
import com.amilesend.discogs.model.collection.RenameFolderRequest;
import com.amilesend.discogs.model.collection.RenameFolderResponse;
import lombok.NonNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * The Discogs User Collection API.
 * <br/>
 * <a href="https://www.discogs.com/developers#page:user-collection">API Documentation</a>
 *
 * @see ApiBase
 */
@Api
public class UserCollectionApi extends ApiBase {
    private static final String API_PATH = "/users/";
    private static final String COLLECTION_SUB_PATH = "/collection";
    private static final String FOLDERS_SUB_PATH = "/folders";
    private static final String RELEASES_SUB_PATH = "/releases";
    private static final String INSTANCES_SUB_PATH = "/instances";
    private static final String FIELDS_SUB_PATH = "/fields";

    /**
     * Creates a new {@code UserCollectionApi} object.
     *
     * @param connection the underlying client connection
     */
    public UserCollectionApi(final DiscogsConnection connection) {
        super(connection);
    }

    /**
     * Gets the list of a folders in a user's collection. Note: Authentication is optional. Non-authenticated
     * requests will only be able to see a user's "All" folder (i.e., the user's public collection).
     *
     * @param request the request
     * @return the response
     * @see GetFoldersRequest
     * @see GetFoldersResponse
     */
    @AuthenticationOptional
    public GetFoldersResponse getFolders(@NonNull final GetFoldersRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FOLDERS_SUB_PATH)
                .toString();
        return executeGet(subPath, request, GetFoldersResponse.class);
    }

    /**
     * Creates a new folder within a user's collection.
     *
     * @param request the request
     * @return the response
     * @see CreateFolderRequest
     * @see CreateFolderResponse
     */
    @AuthenticationRequired
    public CreateFolderResponse createFolder(@NonNull final CreateFolderRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FOLDERS_SUB_PATH)
                .toString();
        return executePost(subPath, request, CreateFolderResponse.class);
    }

    /**
     * Gets a folder from a user's collection. Note: Authentication is optional. If
     * {@link GetFolderRequest#getFolderId()} is not {@code 0}, then user authentication is required.
     *
     * @param request the request
     * @return the response
     * @see GetFolderRequest
     * @see GetFolderResponse
     */
    @AuthenticationOptional
    public GetFolderResponse getFolder(@NonNull final GetFolderRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FOLDERS_SUB_PATH)
                .append("/")
                .append(request.getFolderId())
                .toString();
        return executeGet(subPath, request, GetFolderResponse.class);
    }

    /**
     * Renames a folder within a user's collection. Note: Authentication is required.
     *
     * @param request the request
     * @return the response
     * @see RenameFolderRequest
     * @see RenameFolderResponse
     */
    @AuthenticationRequired
    public RenameFolderResponse renameFolder(@NonNull final RenameFolderRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FOLDERS_SUB_PATH)
                .append("/")
                .append(request.getFolderId())
                .toString();
        return executePost(subPath, request, RenameFolderResponse.class);
    }

    /**
     * Deletes a folder from a user's collection. Note: Authentication is required.
     *
     * @param request the request
     * @see DeleteFolderRequest
     */
    @AuthenticationRequired
    public void deleteFolder(@NonNull final DeleteFolderRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FOLDERS_SUB_PATH)
                .append("/")
                .append(request.getFolderId())
                .toString();
        executeDelete(subPath, request);
    }

    /**
     * Gets the paginated list of user collection items by release. Note: Authentication is required is the owner's
     * collection is private.
     *
     * @param request the request
     * @return the response
     * @see GetCollectionItemsByReleaseRequest
     * @see GetCollectionItemsByReleaseResponse
     */
    @AuthenticationOptional
    public GetCollectionItemsByReleaseResponse getCollectionItems(
            @NonNull final GetCollectionItemsByReleaseRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(RELEASES_SUB_PATH)
                .append("/")
                .append(request.getReleaseId())
                .toString();
        return executeGet(subPath, request, GetCollectionItemsByReleaseResponse.class);
    }

    /**
     * Gets a paginated list of items in a user collection folder.
     *
     * @param request the request
     * @return the response
     * @see GetCollectionItemsByFolderRequest
     * @see GetCollectionItemsByFolderResponse
     */
    @AuthenticationOptional
    public GetCollectionItemsByFolderResponse getCollectionItems(
            @NonNull final GetCollectionItemsByFolderRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FOLDERS_SUB_PATH)
                .append("/")
                .append(request.getFolderId())
                .append(RELEASES_SUB_PATH)
                .toString();
        return executeGet(subPath, request, GetCollectionItemsByFolderResponse.class);
    }

    /**
     * Adds a release to a user's collection folder. Note: Authentication required.
     *
     * @param request the request
     * @return the response
     * @see AddToFolderRequest
     * @see AddToFolderResponse
     */
    @AuthenticationRequired
    public AddToFolderResponse addToFolder(@NonNull final AddToFolderRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FOLDERS_SUB_PATH)
                .append("/")
                .append(request.getFolderId())
                .append(RELEASES_SUB_PATH)
                .append("/")
                .append(request.getReleaseId())
                .toString();
        return executePost(subPath, request, AddToFolderResponse.class);
    }

    /**
     * Changes the release rating.
     *
     * @param request the request
     * @see ChangeReleaseRatingRequest
     */
    @AuthenticationRequired
    public void changeReleaseRating(@NonNull final ChangeReleaseRatingRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FOLDERS_SUB_PATH)
                .append("/")
                .append(request.getFolderId())
                .append(RELEASES_SUB_PATH)
                .append("/")
                .append(request.getReleaseId())
                .append(INSTANCES_SUB_PATH)
                .append("/")
                .append(request.getInstanceId())
                .toString();
        executePost(subPath, request);
    }

    /**
     * Moves an instance to another folder.
     *
     * @param request the request
     * @see MoveReleaseRequest
     */
    @AuthenticationRequired
    public void moveRelease(@NonNull final MoveReleaseRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FOLDERS_SUB_PATH)
                .append("/")
                .append(request.getFolderId())
                .append(RELEASES_SUB_PATH)
                .append("/")
                .append(request.getReleaseId())
                .append(INSTANCES_SUB_PATH)
                .append("/")
                .append(request.getInstanceId())
                .toString();
        executePost(subPath, request);
    }

    /**
     * Deletes an instance from a folder.
     *
     * @param request the request
     * @see DeleteInstanceRequest
     */
    @AuthenticationRequired
    public void deleteInstance(@NonNull final DeleteInstanceRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FOLDERS_SUB_PATH)
                .append("/")
                .append(request.getFolderId())
                .append(RELEASES_SUB_PATH)
                .append("/")
                .append(request.getReleaseId())
                .append(INSTANCES_SUB_PATH)
                .append("/")
                .append(request.getInstanceId())
                .toString();
        executeDelete(subPath, request);
    }

    /**
     * Gets the list of custom fields for a user. Note: Authentication is optional.  If a collection is private,
     * then authentication as the owner is required.  Non-authenticated requests can only retrieve fields with public
     * fields.
     *
     * @param request the request
     * @return the response
     * @see GetCustomFieldsRequest
     * @see GetCustomFieldsResponse
     */
    @AuthenticationOptional
    public GetCustomFieldsResponse getCustomFields(@NonNull final GetCustomFieldsRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FIELDS_SUB_PATH)
                .toString();
        return executeGet(subPath, request, GetCustomFieldsResponse.class);
    }

    /**
     * Edits a field for an instance.
     *
     * @param request the request
     * @see EditInstanceFieldRequest
     */
    @AuthenticationRequired
    public void editInstanceField(@NonNull final EditInstanceFieldRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append(FOLDERS_SUB_PATH)
                .append("/")
                .append(request.getFolderId())
                .append(RELEASES_SUB_PATH)
                .append("/")
                .append(request.getReleaseId())
                .append(INSTANCES_SUB_PATH)
                .append("/")
                .append(request.getInstanceId())
                .append(FIELDS_SUB_PATH)
                .append("/")
                .append(request.getFieldId())
                .toString();
        executePost(subPath, request);
    }

    /**
     * Gets the value of a user's collection. Note: Must be authenticated as the collection owner.
     *
     * @param request the request
     * @return the response
     * @see GetCollectionValueRequest
     * @see GetCollectionValueResponse
     */
    @AuthenticationRequired
    public GetCollectionValueResponse getCollectionValue(@NonNull final GetCollectionValueRequest request) {
        final String subPath = new StringBuilder(API_PATH)
                .append(URLEncoder.encode(request.getUsername(), StandardCharsets.UTF_8))
                .append(COLLECTION_SUB_PATH)
                .append("/value")
                .toString();
        return executeGet(subPath, request, GetCollectionValueResponse.class);
    }
}
