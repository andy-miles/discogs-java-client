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
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

/** GSON adapter to format and serializes {@link LocalDateTime} objects. */
@Slf4j
public class LocalDateTimeTypeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    @Override
    public JsonElement serialize(
            final LocalDateTime dateTime,
            final Type typeOfSrc,
            final JsonSerializationContext context) {
        return new JsonPrimitive(dateTime.atZone(ZoneId.of("UTC")).format(ISO_OFFSET_DATE_TIME));
    }

    @Override
    public LocalDateTime deserialize(
            final JsonElement jsonElement,
            final Type typeOfT,
            final JsonDeserializationContext context) throws JsonParseException {
        if (jsonElement.isJsonNull()) {
            return null;
        }

        final String dateTimeAsString = jsonElement.getAsString();
        if (StringUtils.isBlank(dateTimeAsString)) {
            return null;
        }

        try {
            return LocalDateTime.parse(dateTimeAsString, ISO_OFFSET_DATE_TIME);
        } catch (final DateTimeParseException ex) {
            throw new JsonParseException("Datetime format does not match ISO_OFFSET_DATE_TIME format", ex);
        }
    }
}
