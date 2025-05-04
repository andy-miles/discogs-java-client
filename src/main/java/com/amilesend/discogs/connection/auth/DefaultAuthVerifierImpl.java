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
import com.amilesend.discogs.model.Api;
import com.amilesend.discogs.model.AuthenticationOptional;
import com.amilesend.discogs.model.AuthenticationRequired;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

/**
 * Helper to verify authentication defined for specific API calls to fail early if not configured for required calls.
 * <p/>
 * Note: This class checks the call stack for classes annotated with the {@link Api} annotation. Next, it
 * checks the method to see if it is annotated with either {@link AuthenticationRequired} or
 * {@link AuthenticationOptional}.  If auth is required, then an {@link AuthException} is thrown; else, an info
 * log statement is recorded.
 *
 * @see AuthVerifier
 * @see Api
 * @see AuthenticationRequired
 * @see AuthenticationOptional
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultAuthVerifierImpl implements AuthVerifier {
    // Accounts for this class (i.e., #checkIfAuthenticated() -> #getApiMethodDescriptor()
    private static final int DEFAULT_SKIP_FRAMES = 2;
    private static final int MAX_STACK_WALK_DEPTH = 20;

    /** The configured stack walker. */
    @NonNull
    private final StackWalker stackWalker;
    /** The logger. */
    private final Logger log;

    /** Creates a new {@link DefaultAuthVerifierImpl} instance with the default settings configured. */
    public DefaultAuthVerifierImpl() {
        this(StackWalker.getInstance(Set.of(RETAIN_CLASS_REFERENCE), MAX_STACK_WALK_DEPTH),
                LoggerFactory.getLogger(DefaultAuthVerifierImpl.class));
    }

    @Override
    public void checkIfAuthenticated(final AuthManager<?> authManager) {
        final MethodDescriptor methodDescriptor = getApiMethodDescriptor();
        final MethodAuthType methodAuthType = methodDescriptor.getType();
        if (methodAuthType == MethodAuthType.NONE || isAuthenticated(authManager)) {
            return;
        }

        final String msg = new StringBuilder("Client authorization is not configured to access ")
                .append(methodDescriptor.getApiClass().getSimpleName())
                .append("::")
                .append(methodDescriptor.getMethodName())
                .toString();
        switch (methodAuthType) {
            case REQUIRED:
                throw new AuthException(msg);
            default: /* fall through */
            case OPTIONAL:
                log.info(msg);
                break;
        }
    }

    private boolean isAuthenticated(final AuthManager<?> authManager) {
        return Optional.ofNullable(authManager)
                .map(AuthManager::isAuthenticated)
                .orElse(false);
    }

    private MethodDescriptor getApiMethodDescriptor() {
        try {
            // This finds the list of stack frames that reference the API class
            final List<StackWalker.StackFrame> apiStackFrames = stackWalker.walk(
                    stream -> stream.skip(DEFAULT_SKIP_FRAMES)
                            .filter(sf -> sf.getDeclaringClass().isAnnotationPresent(Api.class))
                            .collect(Collectors.toList()));
            // This iterates through the frames specific to the API class to find any methods that are annotated
            return apiStackFrames.stream()
                    .filter(sf -> containsAuthenticationAnnotation(sf))
                    .findFirst()
                    .map(sf -> MethodDescriptor.builder()
                            .methodName(sf.getMethodName())
                            .apiClass(sf.getDeclaringClass())
                            .type(getMethodFromStackFrame(sf)
                                    .map(MethodAuthType::from)
                                    .orElse(MethodAuthType.NONE))
                            .build())
                    .orElse(MethodDescriptor.builder()
                            .methodName("???")
                            .apiClass(null)
                            .type(MethodAuthType.NONE)
                            .build());
        } catch (final Exception ex) {
            // This check should not blow up the API invocation (i.e., let the service verify and throw an exception).
            return  MethodDescriptor.builder()
                    .methodName("???")
                    .apiClass(null)
                    .type(MethodAuthType.NONE)
                    .build();
        }
    }

    private boolean containsAuthenticationAnnotation(final StackWalker.StackFrame stackFrame) {
        return getMethodFromStackFrame(stackFrame)
                .map(m -> m.isAnnotationPresent(AuthenticationRequired.class)
                        || m.isAnnotationPresent(AuthenticationOptional.class))
                .orElse(false);
    }

    private Optional<Method> getMethodFromStackFrame(final StackWalker.StackFrame stackFrame) {
        final Class<?> apiClass = stackFrame.getDeclaringClass();
        try {
            return Optional.of(apiClass.getMethod(stackFrame.getMethodName()));
        } catch (final NoSuchMethodException ex) {
            return Optional.empty();
        }
    }

    /** Describes the current API method authorization requirement type. */
    private enum MethodAuthType {
        REQUIRED, OPTIONAL, NONE;

        public static MethodAuthType from(final Method method) {
            if (Objects.isNull(method)) {
                return NONE;
            }

            if (method.isAnnotationPresent(AuthenticationRequired.class)) {
                return REQUIRED;
            }

            if (method.isAnnotationPresent(AuthenticationOptional.class)) {
                return OPTIONAL;
            }

            return NONE;
        }
    }

    /** Describes the calling API method. */
    @Builder
    @Getter
    private static class MethodDescriptor {
        /** The type. */
        private final MethodAuthType type;
        /** The API class. */
        private final Class<?> apiClass;
        /** The API method name. */
        private final String methodName;
    }
}
