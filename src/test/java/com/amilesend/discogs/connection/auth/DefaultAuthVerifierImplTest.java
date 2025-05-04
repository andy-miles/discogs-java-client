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
package com.amilesend.discogs.connection.auth;

import com.amilesend.client.connection.auth.AuthException;
import com.amilesend.client.connection.auth.AuthManager;
import com.amilesend.client.connection.auth.NoOpAuthManager;
import com.amilesend.discogs.connection.auth.info.KeySecretAuthInfo;
import com.amilesend.discogs.connection.auth.info.TokenAuthInfo;
import com.amilesend.discogs.model.Api;
import com.amilesend.discogs.model.AuthenticationOptional;
import com.amilesend.discogs.model.AuthenticationRequired;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Set;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultAuthVerifierImplTest {
    @Mock
    private Logger mockLogger;
    private DefaultAuthVerifierImpl authVerifierUnderTest;

    @BeforeEach
    public void setUp() {
        final StackWalker stackWalker = StackWalker.getInstance(Set.of(RETAIN_CLASS_REFERENCE), TestApi.SKIP_FRAMES);
        authVerifierUnderTest = new DefaultAuthVerifierImpl(stackWalker, mockLogger);
    }

    @Test
    public void checkIfAuthenticated_withExceptionFromStackWalker_shouldDoNothing() {
        final StackWalker mockStackWalker = mock(StackWalker.class);
        doThrow(new IllegalStateException("Exception")).when(mockStackWalker).walk(any());
        authVerifierUnderTest = new DefaultAuthVerifierImpl(mockStackWalker, mockLogger);
        final AuthManager<?> authManager =
                new TokenAuthManager(new TokenAuthInfo("KeyName"));
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        testApi.noAuthApiCall();

        verifyNoInteractions(mockLogger);
    }

    //////////////////////////
    // No auth required call
    //////////////////////////

    @Test
    public void checkIfAuthenticated_withNoOpAuthManagerAndNoAuthApiCall_shouldDoNothing() {
        final AuthManager<?> authManager = new NoOpAuthManager();
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        testApi.noAuthApiCall();

        verifyNoInteractions(mockLogger);
    }

    @Test
    public void checkIfAuthenticated_withKeySecretAuthManagerAndNoAuthApiCall_shouldDoNothing() {
        final AuthManager<?> authManager =
                new KeySecretAuthManager(new KeySecretAuthInfo("KeyName", "KeyValue"));
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        testApi.noAuthApiCall();

        verifyNoInteractions(mockLogger);
    }

    @Test
    public void checkIfAuthenticated_withTokenAuthManagerAndNoAuthApiCall_shouldDoNothing() {
        final AuthManager<?> authManager = new TokenAuthManager(new TokenAuthInfo("KeyName"));
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        testApi.noAuthApiCall();

        verifyNoInteractions(mockLogger);
    }

    @Test
    public void checkIfAuthenticated_withOAuthManagerAndNoAuthApiCAll_shouldDoNothing() {
        final TestApi testApi = new TestApi(mock(OAuthManager.class), authVerifierUnderTest);

        testApi.noAuthApiCall();

        verifyNoInteractions(mockLogger);
    }

    ///////////////////////
    // Auth required call
    ///////////////////////

    @Test
    public void checkIfAuthenticated_withNoOpAuthManagerAndAuthRequiredApiCall_shouldThrowException() {
        final AuthManager<?> authManager = new NoOpAuthManager();
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        assertAll(
                () -> assertThrows(AuthException.class, () -> testApi.authRequiredApiCall()),
                () -> verifyNoInteractions(mockLogger));
    }

    @Test
    public void checkIfAuthenticated_withKeySecretAuthManagerAndAuthRequiredApiCall_shouldDoNothing() {
        final AuthManager<?> authManager =
                new KeySecretAuthManager(new KeySecretAuthInfo("KeyName", "KeyValue"));
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        testApi.authRequiredApiCall();

        verifyNoInteractions(mockLogger);
    }

    @Test
    public void checkIfAuthenticated_withTokenAuthManagerAndAuthRequiredApiCall_shouldDoNothing() {
        final AuthManager<?> authManager = new TokenAuthManager(new TokenAuthInfo("KeyName"));
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        testApi.authRequiredApiCall();

        verifyNoInteractions(mockLogger);
    }

    @Test
    public void checkIfAuthenticated_withOAuthManagerAndAuthRequiredApiCall_shouldDoNothing() {
        final OAuthManager authManager = mock(OAuthManager.class);
        when(authManager.isAuthenticated()).thenReturn(true);
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        testApi.authRequiredApiCall();

        verifyNoInteractions(mockLogger);
    }

    @Test
    public void checkIfAuthenticated_withNonAuthenticatedOAuthManagerAndAuthRequiredApiCall_shouldThrowException() {
        final OAuthManager authManager = mock(OAuthManager.class);
        when(authManager.isAuthenticated()).thenReturn(false);
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        assertAll(
                () -> assertThrows(AuthException.class, () -> testApi.authRequiredApiCall()),
                () -> verifyNoInteractions(mockLogger));
    }

    ///////////////////////
    // Auth optional call
    ///////////////////////

    @Test
    public void checkIfAuthenticated_withNoOpAuthManagerAndAuthOptionalApiCall_shouldLogMessage() {
        final AuthManager<?> authManager = new NoOpAuthManager();
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        testApi.authOptionalApiCall();

        verify(mockLogger).info(isA(String.class));
    }

    @Test
    public void checkIfAuthenticated_withKeySecretAuthManagerAndAuthOptionalApiCall_shouldDoNothing() {
        final AuthManager<?> authManager =
                new KeySecretAuthManager(new KeySecretAuthInfo("KeyName", "KeyValue"));
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        testApi.authOptionalApiCall();

        verifyNoInteractions(mockLogger);
    }

    @Test
    public void checkIfAuthenticated_withTokenAuthManagerAndAuthOptionalApiCall_shouldDoNothing() {
        final AuthManager<?> authManager = new TokenAuthManager(new TokenAuthInfo("KeyName"));
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        testApi.authOptionalApiCall();

        verifyNoInteractions(mockLogger);
    }

    @Test
    public void checkIfAuthenticated_withOAuthManagerAndAuthOptionalApiCall_shouldDoNothing() {
        final OAuthManager authManager = mock(OAuthManager.class);
        when(authManager.isAuthenticated()).thenReturn(true);
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        testApi.authOptionalApiCall();

        verifyNoInteractions(mockLogger);
    }

    @Test
    public void checkIfAuthenticated_withNonAuthenticatedOAuthManagerAndAuthOptionalApiCall_shouldLogMessage() {
        final OAuthManager authManager = mock(OAuthManager.class);
        when(authManager.isAuthenticated()).thenReturn(false);
        final TestApi testApi = new TestApi(authManager, authVerifierUnderTest);

        assertAll(
                () -> assertThrows(AuthException.class, () -> testApi.authRequiredApiCall()),
                () -> verifyNoInteractions(mockLogger));
    }

    @Api
    @RequiredArgsConstructor
    class TestApi {
        static final int SKIP_FRAMES = 3;

        private final AuthManager<?> authManager;
        private final AuthVerifier authVerifier;

        public void noAuthApiCall() {
            executeApiCall();
        }

        @AuthenticationRequired
        public void authRequiredApiCall() {
            executeApiCall();
        }

        @AuthenticationOptional
        public void authOptionalApiCall() {
            executeApiCall();
        }

        private void executeApiCall() {
            authVerifier.checkIfAuthenticated(authManager);
        }
    }
}
