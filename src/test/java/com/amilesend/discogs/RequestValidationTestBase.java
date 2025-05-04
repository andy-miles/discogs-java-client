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
package com.amilesend.discogs;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RequestValidationTestBase {
    @Mock
    protected HttpUrl.Builder mockHttpUrlBuilder;

    protected void validateQueryParameter(final String expectedKey, final String expectedValue) {
        verify(mockHttpUrlBuilder).addQueryParameter(eq(expectedKey), eq(expectedValue));
    }

    protected void validateQueryParameter(final String expectedKey) {
        verify(mockHttpUrlBuilder).addQueryParameter(eq(expectedKey), anyString());
    }
}
