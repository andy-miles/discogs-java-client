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
package com.amilesend.discogs.api;

import com.amilesend.client.parse.parser.BasicParser;
import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.connection.auth.oauth.OAuthReceiverException;
import com.amilesend.discogs.model.BodyBasedRequest;
import com.amilesend.discogs.model.QueryParameterBasedRequest;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Objects;

import static com.amilesend.discogs.connection.DiscogsConnection.JSON_MEDIA_TYPE;

/** Wraps a {@link OkHttpClient} that manages parsing responses to corresponding POJO types. */
@RequiredArgsConstructor
public abstract class ApiBase {
    /** The connection that wraps the underlying HTTP client. */
    @NonNull
    @Getter
    private final DiscogsConnection connection;

    /**
     * Helper method to get the {@link Gson} instance for the connection.
     *
     * @return the Gson instance
     */
    protected Gson getGsonForConnection() {
        return connection.getGsonFactory().getInstance(connection);
    }

    /**
     * Executes a GET request for the given URL path and expected response type class.
     *
     * @param apiPath the path for the operation relative to the base URL
     * @param responseType the expected response type class
     * @return the deserialized response
     * @param <T> the response type
     */
    protected <T> T executeGet(final String apiPath, final Class<T> responseType) {
        final Request request = connection.newRequestBuilder()
                .url(buildHttpUrl(apiPath, null))
                .build();
        return connection.execute(request, new BasicParser<>(responseType));
    }

    /**
     * Executes a GET request for the given URL path, request, and expected response type class.
     *
     * @param apiPath the path for the operation relative to the base URL
     * @param request the request
     * @param responseType the response type class
     * @return the deserialized response
     * @param <T> the response type
     */
    protected <T> T executeGet(
            final String apiPath,
            final QueryParameterBasedRequest request,
            final Class<T> responseType) throws OAuthReceiverException {
        return connection.execute(
                connection.newRequestBuilder()
                        .url(buildHttpUrl(apiPath, request))
                        .build(),
                new BasicParser<>(responseType));
    }

    /**
     * Executes a PUT request for the given URL path, request, and expected response type class.
     *
     * @param apiPath the path for the operation relative to the base URL
     * @param request the request
     * @param responseType the response type class
     * @return the deserialized response
     * @param <T> the response type
     * @throws OAuthReceiverException
     */
    protected <T> T executePut(
            final String apiPath,
            final BodyBasedRequest request,
            final Class<T> responseType) throws OAuthReceiverException {
        final Request httpRequest = connection.newRequestBuilder()
                .url(buildHttpUrl(apiPath, request))
                .put(RequestBody.create(getGsonForConnection().toJson(request), JSON_MEDIA_TYPE))
                .build();
        return connection.execute(httpRequest, new BasicParser<>(responseType));
    }

    /**
     * Executes a POST request for the given URL path, request, and expected response type class.
     *
     * @param apiPath the path for the operation relative to the base URL
     * @param request the request
     * @param responseType the response type class
     * @return the deserialized response
     * @param <T> the response type
     */
    protected <T> T executePost(
            final String apiPath,
            final BodyBasedRequest request,
            final Class<T> responseType) throws OAuthReceiverException {
        return connection.execute(buildPostRequest(apiPath, request), new BasicParser<>(responseType));
    }

    /**
     * Executes a POST request for the given URL path and request.
     *
     * @param apiPath the path for the operation relative to the base URL
     * @param request the request
     */
    protected void executePost(final String apiPath, final BodyBasedRequest request) {
        try (final Response ignored = connection.execute(buildPostRequest(apiPath, request))) {
        }
    }

    private Request buildPostRequest(final String apiPath, final BodyBasedRequest request) {
        return connection.newRequestBuilder()
                .url(buildHttpUrl(apiPath, request))
                .post(RequestBody.create(getGsonForConnection().toJson(request), JSON_MEDIA_TYPE))
                .build();
    }

    /**
     * Executes a DELETE request for the given URL path, request with no expectation of a response object.
     *
     * @param apiPath the path for the operation relative to the base URL
     * @param request the request
     */
    protected void executeDelete(final String apiPath, final QueryParameterBasedRequest request) {
        try (final Response response = connection.execute(buildDeleteRequest(apiPath, request))) {
        }
    }

    /**
     * Builds a delete request for the given URL path and request.
     *
     * @param apiPath the URL path
     * @param request the query parameter based request
     * @return the HTTP request
     */
    protected Request buildDeleteRequest(final String apiPath, final QueryParameterBasedRequest request) {
        final HttpUrl httpUrl = buildHttpUrl(apiPath, request);

        return connection.newRequestBuilder()
                .url(httpUrl)
                .delete()
                .build();
    }

    /**
     * Builds a new {@link HttpUrl} for the givne path and request
     *
     * @param apiPath the URL path
     * @param request the request
     * @return the URL
     */
    protected HttpUrl buildHttpUrl(final String apiPath, final QueryParameterBasedRequest request) {
        final String urlPath = new StringBuilder(connection.getBaseUrl())
                .append(apiPath)
                .toString();
        final HttpUrl.Builder urlBuilder = HttpUrl.parse(urlPath).newBuilder();
        if (Objects.nonNull(request)) {
            request.populateQueryParameters(urlBuilder);
        }

        return urlBuilder.build();
    }
}
