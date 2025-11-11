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
import com.amilesend.discogs.model.marketplace.type.Condition;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/** GSON adapter to format and serializes {@link Condition} objects. */
public class ConditionAdapter implements JsonSerializer<Condition>, JsonDeserializer<Condition> {
    @Override
    public JsonElement serialize(
            final Condition condition,
            final Type type,
            final JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(condition.getValue());
    }

    @Override
    public Condition deserialize(
            final JsonElement jsonElement,
            final Type type,
            final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonNull()) {
            return null;
        }

        final String conditionValue = jsonElement.getAsString();
        if (StringUtils.isBlank(conditionValue)) {
            return null;
        }

        return Condition.fromValue(conditionValue);
    }
}
