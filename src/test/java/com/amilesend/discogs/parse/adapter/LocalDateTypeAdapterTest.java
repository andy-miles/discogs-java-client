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
package com.amilesend.discogs.parse.adapter;

import com.amilesend.client.util.StringUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocalDateTypeAdapterTest {
    private LocalDateTypeAdapter adapterUnderTest = new LocalDateTypeAdapter();

    //////////////
    // serialize
    //////////////

    @Test
    public void serialize_withConditionValue_shouldReturnJsonElement() {
        final LocalDate dateTime = LocalDate.of(2025, 3, 12);

        final JsonElement actual =
                adapterUnderTest.serialize(dateTime, mock(Type.class), mock(JsonSerializationContext.class));

        assertEquals("2025-03-12", actual.getAsString());
    }

    ////////////////
    // deserialize
    ////////////////

    @Test
    public void deserialize_withNullJsonElement_thenReturnNull() {
        final JsonElement mockJsonElement = mock(JsonElement.class);
        when(mockJsonElement.isJsonNull()).thenReturn(true);

        final LocalDate actual =
                adapterUnderTest.deserialize(mockJsonElement, mock(Type.class), mock(JsonDeserializationContext.class));

        assertNull(actual);
    }

    @Test
    public void deserialize_withBlankJsonElement_shouldReturnNull() {
        final JsonElement mockJsonElement = mock(JsonElement.class);
        when(mockJsonElement.isJsonNull()).thenReturn(false);
        when(mockJsonElement.getAsString()).thenReturn(StringUtils.EMPTY);

        final LocalDate actual =
                adapterUnderTest.deserialize(mockJsonElement, mock(Type.class), mock(JsonDeserializationContext.class));

        assertNull(actual);
    }

    @Test
    public void deserialize_withValue_shouldReturnValue() {
        final JsonElement mockJsonElement = mock(JsonElement.class);
        when(mockJsonElement.isJsonNull()).thenReturn(false);
        when(mockJsonElement.getAsString()).thenReturn("2025-03-12");

        final LocalDate actual =
                adapterUnderTest.deserialize(mockJsonElement, mock(Type.class), mock(JsonDeserializationContext.class));

        final LocalDate expected = LocalDate.of(2025, 3, 12);
        assertEquals(expected, actual);
    }
}
