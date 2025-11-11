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
package com.amilesend.discogs.connection.auth;

import com.amilesend.client.connection.auth.AuthException;
import com.amilesend.client.util.StringUtils;
import com.amilesend.discogs.connection.auth.info.KeySecretAuthInfo;
import com.amilesend.discogs.connection.auth.info.OAuthInfo;
import com.amilesend.discogs.connection.auth.oauth.AccessTokenResponse;
import com.amilesend.discogs.connection.auth.oauth.OAuthReceiver;
import com.amilesend.discogs.connection.auth.oauth.OAuthReceiverException;
import com.amilesend.discogs.connection.auth.oauth.RequestTokenResponse;
import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.event.Level;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

import static com.amilesend.client.connection.Connection.Headers.AUTHORIZATION;
import static com.amilesend.client.connection.Connection.Headers.CONTENT_TYPE;
import static com.amilesend.client.connection.Connection.Headers.USER_AGENT;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OAuthManagerTest {
    private static final OAuthInfo AUTH_INFO = new OAuthInfo("Token", "TokenSecret");
    private static final KeySecretAuthInfo APP_AUTH_INFO = new KeySecretAuthInfo("KeyName", "KeyValue");
    private static final String USER_AGENT_VALUE = "TestUserAgent/1.0";

    @Mock
    private OkHttpClient mockHttpClient;
    @Mock
    private OAuthReceiver mockOAuthReceiver;

    private OAuthManager authManagerUnderTest;

    @BeforeEach
    public void setUp() {
        authManagerUnderTest = spy(OAuthManager.builder()
                .httpClient(mockHttpClient)
                .oAuthReceiver(mockOAuthReceiver)
                .appCredentials(APP_AUTH_INFO)
                .userAgent(USER_AGENT_VALUE)
                .build());
    }

    ////////
    // ctor
    ////////

    @Test
    public void ctor_withNullRequiredDependencies_shouldThrowException() {
        assertAll(
                () -> assertThrows(
                        NullPointerException.class,
                        () -> OAuthManager.builder()
                                .httpClient(null)
                                .oAuthReceiver(mockOAuthReceiver)
                                .appCredentials(APP_AUTH_INFO)
                                .userAgent(USER_AGENT_VALUE)
                                .build()),
                () -> assertThrows(
                        NullPointerException.class,
                        () -> OAuthManager.builder()
                                .httpClient(mockHttpClient)
                                .oAuthReceiver(null)
                                .appCredentials(APP_AUTH_INFO)
                                .userAgent(USER_AGENT_VALUE)
                                .build()),
                () -> assertThrows(
                        NullPointerException.class,
                        () -> OAuthManager.builder()
                                .httpClient(mockHttpClient)
                                .oAuthReceiver(mockOAuthReceiver)
                                .appCredentials(null)
                                .userAgent(USER_AGENT_VALUE)
                                .build()));
    }

    ////////////////
    // getAuthInfo
    ////////////////

    @Test
    public void getAuthInfo_withTokenSet_shouldReturnToken() {
        authManagerUnderTest = OAuthManager.builder()
                .httpClient(mockHttpClient)
                .oAuthReceiver(mockOAuthReceiver)
                .appCredentials(APP_AUTH_INFO)
                .userAgent(USER_AGENT_VALUE)
                .oAuthToken(AUTH_INFO)
                .build();

        assertEquals(AUTH_INFO, authManagerUnderTest.getAuthInfo());
    }

    @Test
    public void getAuthInfo_withNoTokenSet_shouldAuthenticate() {
        doReturn(AUTH_INFO).when(authManagerUnderTest).authenticate();

        final OAuthInfo actual = authManagerUnderTest.getAuthInfo();

        assertAll(
                () -> assertEquals(AUTH_INFO, actual),
                () -> verify(authManagerUnderTest).authenticate());
    }

    @Test
    public void getAuthInfo_withOAuthReceiverException_shouldThrowException() {
        doThrow(new OAuthReceiverException("Exception")).when(authManagerUnderTest).authenticate();

        assertThrows(OAuthReceiverException.class, () -> authManagerUnderTest.getAuthInfo());
    }

    //////////////////////
    // addAuthentication
    //////////////////////

    @Test
    public void addAuthentication_withRequestBuilder_shouldAddAuthHeaders() {
        doReturn(AUTH_INFO).when(authManagerUnderTest).getAuthInfo();
        final Request.Builder requestBuilder = new Request.Builder();

        final Request actual = authManagerUnderTest.addAuthentication(requestBuilder)
                .url("http://SomeUrl")
                .build();

        assertAll(
                () -> validateAuthHeaderForRequest(actual.headers()),
                () -> verify(authManagerUnderTest).getAuthInfo(),
                () -> verify(authManagerUnderTest).toAuthHeaderForRequestSigning(eq(APP_AUTH_INFO), eq(AUTH_INFO)));
    }

    private static void validateAuthHeaderForRequest(final Headers headers) {
        final String actual = headers.get(AUTHORIZATION);
        assertAll(
                () -> assertTrue(actual.contains("OAuth oauth_consumer_key=\"KeyName\"")),
                () -> assertTrue(actual.contains("oauth_nonce=")),
                () -> assertTrue(actual.contains("oauth_signature_method=\"PLAINTEXT\"")),
                () -> assertTrue(actual.contains("oauth_timestamp=")),
                () -> assertTrue(actual.contains("oauth_token=\"Token\"")),
                () -> assertTrue(actual.contains("oauth_signature=\"KeyValue&TokenSecret\"")));
    }

    @Test
    public void addAuthentication_withNullRequestBuilder_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> authManagerUnderTest.addAuthentication(null));
    }

    @Test
    public void addAuthentication_withOAuthReceiverException_shouldThrowException() {
        doThrow(new OAuthReceiverException("Exception")).when(authManagerUnderTest).getAuthInfo();

        assertThrows(OAuthReceiverException.class, () -> authManagerUnderTest.addAuthentication(new Request.Builder()));
    }

    /////////////////
    // authenticate
    /////////////////

    @Test
    public void authenticate_withValidRequests_shouldReturnOAuthInfo() {
        when(mockOAuthReceiver.getRedirectUri()).thenReturn("RedirectUriValue");
        final RequestTokenResponse mockTokenResponse = mock(RequestTokenResponse.class);
        doReturn(mockTokenResponse)
                .when(authManagerUnderTest)
                .fetchRequestToken(any(KeySecretAuthInfo.class), anyString());
        doReturn("AuthVerifierValue")
                .when(authManagerUnderTest)
                .fetchAuthVerifier(any(RequestTokenResponse.class));
        final AccessTokenResponse accessTokenResponse = AccessTokenResponse.builder()
                .accessToken(AUTH_INFO.getToken())
                .accessTokenSecret(AUTH_INFO.getSecret())
                .build();
        doReturn(accessTokenResponse)
                .when(authManagerUnderTest)
                .fetchAccessToken(any(KeySecretAuthInfo.class), any(RequestTokenResponse.class), anyString());

        assertEquals(AUTH_INFO, authManagerUnderTest.authenticate());
    }

    @Test
    public void authenticate_withOAuthReceiverException_shouldThrowException() {
        when(mockOAuthReceiver.getRedirectUri()).thenReturn("RedirectUriValue");
        doThrow(new OAuthReceiverException("Exception"))
                .when(authManagerUnderTest)
                .fetchRequestToken(any(KeySecretAuthInfo.class), anyString());

        assertThrows(OAuthReceiverException.class, () -> authManagerUnderTest.authenticate());
    }

    //////////////////////
    // fetchAuthVerifier
    //////////////////////

    @Test
    public void fetchAuthVerifier_withRequestTokenResponse_shouldReturnAuthVerifier() {
        doNothing().when(authManagerUnderTest).browse(anyString());
        when(mockOAuthReceiver.waitForCode()).thenReturn("AuthVerifierValue");
        final RequestTokenResponse requestTokenResponse = RequestTokenResponse.builder()
                .token("Token")
                .secret("TokenSecret")
                .build();

        final String actual = authManagerUnderTest.fetchAuthVerifier(requestTokenResponse);

        assertAll(
                () -> assertEquals("AuthVerifierValue", actual),
                () -> verify(mockOAuthReceiver).start(),
                () -> verify(authManagerUnderTest).browse(isA(String.class)),
                () -> verify(mockOAuthReceiver).waitForCode(),
                () -> verify(mockOAuthReceiver).stop());
    }

    @Test
    public void fetchAuthVerifier_withOAuthReceiverException_shouldThrowException() {
        doNothing().when(authManagerUnderTest).browse(anyString());
        when(mockOAuthReceiver.waitForCode()).thenThrow(new OAuthReceiverException("Exception"));
        final RequestTokenResponse requestTokenResponse = RequestTokenResponse.builder()
                .token("Token")
                .secret("TokenSecret")
                .build();

        assertAll(
                () -> assertThrows(OAuthReceiverException.class,
                        () -> authManagerUnderTest.fetchAuthVerifier(requestTokenResponse)),
                () -> verify(mockOAuthReceiver).start(),
                () -> verify(mockOAuthReceiver).stop());
    }

    //////////////////////
    // fetchRequestToken
    //////////////////////

    @Test
    public void fetchRequestToken_withValidRequest_shouldReturnResponse() {
        doReturn("TokenBodyValue").when(authManagerUnderTest).fetchToken(any(Request.class));

        try (final MockedStatic<RequestTokenResponse> responseMockedStatic = mockStatic(RequestTokenResponse.class)) {
            final RequestTokenResponse expected = mock(RequestTokenResponse.class);
            responseMockedStatic.when(() -> RequestTokenResponse.parseBodyResponse(anyString())).thenReturn(expected);

            final RequestTokenResponse actual =
                    authManagerUnderTest.fetchRequestToken(APP_AUTH_INFO, "CallbackUrl");

            assertEquals(expected, actual);
        }
    }

    @Test
    public void fetchRequestToken_shouldSendValidRequest() {
        doReturn("TokenBodyValue").when(authManagerUnderTest).fetchToken(any(Request.class));

        try (final MockedStatic<RequestTokenResponse> responseMockedStatic = mockStatic(RequestTokenResponse.class)) {
            responseMockedStatic.when(() -> RequestTokenResponse.parseBodyResponse(anyString()))
                    .thenReturn(mock(RequestTokenResponse.class));

            authManagerUnderTest.fetchRequestToken(APP_AUTH_INFO, "CallbackUrl");

            final ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
            verify(authManagerUnderTest).fetchToken(requestCaptor.capture());
            final Request actual = requestCaptor.getValue();
            final Headers actualHeaders = actual.headers();
            assertAll(
                    () -> assertEquals("https://api.discogs.com/oauth/request_token",
                            actual.url().toString()),
                    () -> validateAuthHeaderForRequestToken(actualHeaders, "CallbackUrl"),
                    () -> assertEquals(USER_AGENT_VALUE, actualHeaders.get(USER_AGENT)),
                    () -> assertEquals("application/x-www-form-urlencoded",
                            actualHeaders.get(CONTENT_TYPE)));
        }
    }

    private static void validateAuthHeaderForRequestToken(final Headers headers, final String callbackUrl) {
        final String actual = headers.get(AUTHORIZATION);
        assertAll(
                () -> assertTrue(actual.contains("OAuth oauth_consumer_key=\"KeyName\"")),
                () -> assertTrue(actual.contains("oauth_nonce=")),
                () -> assertTrue(actual.contains("oauth_signature_method=\"PLAINTEXT\"")),
                () -> assertTrue(actual.contains("oauth_timestamp=")),
                () -> assertTrue(actual.contains("oauth_consumer_key=\"KeyName\"")),
                () -> assertTrue(actual.contains("oauth_signature=\"KeyValue&\"")),
                () -> assertTrue(actual.contains("oauth_callback=")),
                () -> assertTrue(actual.contains(callbackUrl)));
    }

    /////////////////////
    // fetchAccessToken
    /////////////////////

    @Test
    public void fetchAccessToken_withValidRequest_shouldReturnResponse() {
        doReturn("TokenBodyValue").when(authManagerUnderTest).fetchToken(any(Request.class));

        try (final MockedStatic<AccessTokenResponse> responseMockedStatic = mockStatic(AccessTokenResponse.class)) {
            final AccessTokenResponse expected = mock(AccessTokenResponse.class);
            responseMockedStatic.when(() -> AccessTokenResponse.parseBodyResponse(anyString())).thenReturn(expected);
            final RequestTokenResponse requestTokenResponse = RequestTokenResponse.builder()
                    .token(AUTH_INFO.getToken())
                    .callbackConfirmed(Boolean.TRUE)
                    .secret(AUTH_INFO.getSecret())
                    .build();

            final AccessTokenResponse actual = authManagerUnderTest.fetchAccessToken(
                    APP_AUTH_INFO,
                    requestTokenResponse,
                    "AuthVerifierValue");

            assertEquals(expected, actual);
        }
    }

    @Test
    public void fetchAccessToken_shouldSendValidRequest() {
        doReturn("TokenBodyValue").when(authManagerUnderTest).fetchToken(any(Request.class));

        try (final MockedStatic<AccessTokenResponse> responseMockedStatic = mockStatic(AccessTokenResponse.class)) {
            responseMockedStatic.when(() -> AccessTokenResponse.parseBodyResponse(anyString()))
                    .thenReturn(mock(AccessTokenResponse.class));
            final RequestTokenResponse requestTokenResponse = RequestTokenResponse.builder()
                    .token(AUTH_INFO.getToken())
                    .callbackConfirmed(Boolean.TRUE)
                    .secret(AUTH_INFO.getSecret())
                    .build();

            authManagerUnderTest.fetchAccessToken(
                    APP_AUTH_INFO,
                    requestTokenResponse,
                    "AuthVerifierValue");

            final ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
            verify(authManagerUnderTest).fetchToken(requestCaptor.capture());
            final Request actual = requestCaptor.getValue();
            final Headers actualHeaders = actual.headers();
            assertAll(
                    () -> assertEquals("https://api.discogs.com/oauth/access_token",
                            actual.url().toString()),
                    () -> validateAuthHeaderForAccessToken(actualHeaders),
                    () -> assertEquals(USER_AGENT_VALUE, actualHeaders.get(USER_AGENT)),
                    () -> assertEquals("application/x-www-form-urlencoded",
                            actualHeaders.get(CONTENT_TYPE)));
        }
    }

    private static void validateAuthHeaderForAccessToken(final Headers headers) {
        final String actual = headers.get(AUTHORIZATION);
        assertAll(
                () -> assertTrue(actual.contains("OAuth oauth_consumer_key=\"KeyName\"")),
                () -> assertTrue(actual.contains("oauth_nonce=")),
                () -> assertTrue(actual.contains("oauth_signature_method=\"PLAINTEXT\"")),
                () -> assertTrue(actual.contains("oauth_timestamp=")),
                () -> assertTrue(actual.contains("oauth_token=\"Token\"")),
                () -> assertTrue(actual.contains("oauth_signature=\"KeyValue&TokenSecret\"")),
                () -> assertTrue(actual.contains("oauth_verifier=\"AuthVerifierValue\"")));
    }

    ///////////////
    // fetchToken
    ///////////////

    @Test
    @SneakyThrows
    public void fetchToken_withSuccessfulRequest_shouldReturnResponse() {
        final ResponseBody mockResponseBody = mock(ResponseBody.class);
        when(mockResponseBody.string()).thenReturn("ExpectedResponseBodyValue");
        final Response mockResponse = mock(Response.class);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponse.isSuccessful()).thenReturn(Boolean.TRUE);
        final Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        final Request mockRequest = mock(Request.class);

        final String actual = authManagerUnderTest.fetchToken(mockRequest);

        assertEquals("ExpectedResponseBodyValue", actual);
    }

    @Test
    @SneakyThrows
    public void fetchToken_withUnsuccessfulRequest_shouldThrowException() {
        final Response mockResponse = mock(Response.class);
        when(mockResponse.isSuccessful()).thenReturn(Boolean.FALSE);
        final Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        final Request mockRequest = mock(Request.class);

        assertThrows(AuthException.class, () -> authManagerUnderTest.fetchToken(mockRequest));
    }

    @Test
    @SneakyThrows
    public void fetchToken_withIOException_shouldThrowException() {
        final Call mockCall = mock(Call.class);
        when(mockCall.execute()).thenThrow(new IOException("Exception"));
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        final Request mockRequest = mock(Request.class);

        final Throwable actual =
                assertThrows(AuthException.class, () -> authManagerUnderTest.fetchToken(mockRequest));

        assertInstanceOf(IOException.class, actual.getCause());
    }

    ///////////
    // browse
    ///////////

    @Test
    @SneakyThrows
    public void browse_withSupportedDesktop_shouldInvokeBrowse() {
        doNothing().when(authManagerUnderTest).printAndLog(anyString(), any(Level.class));
        final Desktop mockDesktop = mock(Desktop.class);
        when(mockDesktop.isSupported(any(Desktop.Action.class))).thenReturn(true);

        try (final MockedStatic<Desktop> desktopMockedStatic = mockStatic(Desktop.class)) {
            desktopMockedStatic.when(() -> Desktop.isDesktopSupported()).thenReturn(true);
            desktopMockedStatic.when(() -> Desktop.getDesktop()).thenReturn(mockDesktop);

            authManagerUnderTest.browse("http://TestUrlValue");

            final ArgumentCaptor<URI> uriArgumentCaptor = ArgumentCaptor.forClass(URI.class);
            verify(mockDesktop).browse(uriArgumentCaptor.capture());
            assertEquals("http://TestUrlValue", uriArgumentCaptor.getValue().toString());
        }
    }

    @Test
    @SneakyThrows
    public void browse_withBrowseNotSupported_shouldNotInvokeBrowse() {
        doNothing().when(authManagerUnderTest).printAndLog(anyString(), any(Level.class));
        final Desktop mockDesktop = mock(Desktop.class);
        when(mockDesktop.isSupported(any(Desktop.Action.class))).thenReturn(false);

        try (final MockedStatic<Desktop> desktopMockedStatic = mockStatic(Desktop.class)) {
            desktopMockedStatic.when(() -> Desktop.isDesktopSupported()).thenReturn(true);
            desktopMockedStatic.when(() -> Desktop.getDesktop()).thenReturn(mockDesktop);

            authManagerUnderTest.browse("http://TestUrlValue");

            verify(mockDesktop, never()).browse(any(URI.class));
        }
    }

    @Test
    @SneakyThrows
    public void browse_withDesktopNotSupported_shouldNotInvokeBrowse() {
        doNothing().when(authManagerUnderTest).printAndLog(anyString(), any(Level.class));
        final Desktop mockDesktop = mock(Desktop.class);

        try (final MockedStatic<Desktop> desktopMockedStatic = mockStatic(Desktop.class)) {
            desktopMockedStatic.when(() -> Desktop.isDesktopSupported()).thenReturn(false);
            desktopMockedStatic.when(() -> Desktop.getDesktop()).thenReturn(mockDesktop);

            authManagerUnderTest.browse("http://TestUrlValue");

            verify(mockDesktop, never()).browse(any(URI.class));
        }
    }

    @Test
    @SneakyThrows
    public void browse_withInternalError_shouldLogException() {
        browse_withException_shouldLogException(new InternalError("Error"));
    }

    @Test
    @SneakyThrows
    public void browse_withIOException_shouldLogException() {
        browse_withException_shouldLogException(new IOException("Exception"));
    }

    @SneakyThrows
    private void browse_withException_shouldLogException(final Throwable expected) {
        doNothing().when(authManagerUnderTest).printAndLog(anyString(), any(Level.class));
        doNothing().when(authManagerUnderTest).printAndLog(anyString(), any(Level.class), any(Throwable.class));
        final Desktop mockDesktop = mock(Desktop.class);
        when(mockDesktop.isSupported(any(Desktop.Action.class))).thenReturn(true);
        doThrow(expected).when(mockDesktop).browse(any(URI.class));

        try (final MockedStatic<Desktop> desktopMockedStatic = mockStatic(Desktop.class)) {
            desktopMockedStatic.when(() -> Desktop.isDesktopSupported()).thenReturn(true);
            desktopMockedStatic.when(() -> Desktop.getDesktop()).thenReturn(mockDesktop);

            authManagerUnderTest.browse("http://TestUrlValue");

            assertAll(
                    () -> verify(mockDesktop).browse(isA(URI.class)),
                    () -> verify(authManagerUnderTest).printAndLog(anyString(), eq(Level.WARN), eq(expected)));
        }
    }

    @Test
    public void browse_withInvalidUrl_shouldThrowException() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> authManagerUnderTest.browse(null)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> authManagerUnderTest.browse(StringUtils.EMPTY)));
    }
}
