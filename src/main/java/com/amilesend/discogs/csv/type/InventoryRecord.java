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
package com.amilesend.discogs.csv.type;

import com.amilesend.client.util.StringUtils;
import com.amilesend.client.util.Validate;
import com.amilesend.discogs.csv.validation.ValidationException;
import com.amilesend.discogs.model.marketplace.type.Condition;
import com.amilesend.discogs.model.marketplace.type.SleeveCondition;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Defines a record for an inventory item as a CSV row. */
@Builder
@Getter
@EqualsAndHashCode
public class InventoryRecord {
    /** The yes value for {@link #acceptOffer}. */
    public static final String YES = "Y";
    /** The no value for {@link #acceptOffer}. */
    public static final String NO = "N";

    /** The release identifier (required). */
    @NonNull
    private final Integer releaseId;
    /** The price (required). */
    private final Double price;
    /**
     * The media condition (required).
     *
     * @see Condition
     */
    private final Condition mediaCondition;
    /**
     * The sleeve condition (optional).
     *
     * @see SleeveCondition
     */
    private final SleeveCondition sleeveCondition;
    /** Free-form comments. */
    private final String comments;
    /**
     * Flag indicator if buyer offers are accepted or not (optional).
     *
     * @see #YES
     * @see #NO
     */
    private final String acceptOffer;
    /** Free-form text field to describe the location (optional). */
    private final String location;
    /** Free-form notes for external identifiers (optional). */
    private final String externalId;
    /** The item weight in grams (optional). */
    private final Integer weight;
    /** The item format quantity (optional). */
    private final Integer formatQuantity;

    /**
     * Gets the list of headers based on the populated fields.
     *
     * @return the list of defined headers
     */
    public List<InventoryHeader> getDefinedHeaders() {
        final List<InventoryHeader> headers = new ArrayList<>(10);
        headers.add(InventoryHeader.RELEASE_ID);

        Optional.ofNullable(price).ifPresent(p -> headers.add(InventoryHeader.PRICE));
        Optional.ofNullable(mediaCondition).ifPresent(mc -> headers.add(InventoryHeader.MEDIA_CONDITION));
        Optional.ofNullable(sleeveCondition)
                .ifPresent(c -> headers.add(InventoryHeader.SLEEVE_CONDITION));
        Optional.ofNullable(comments).ifPresent(c -> headers.add(InventoryHeader.COMMENTS));
        Optional.ofNullable(acceptOffer).ifPresent(o -> headers.add(InventoryHeader.ACCEPT_OFFER));
        Optional.ofNullable(location).ifPresent(l -> headers.add(InventoryHeader.LOCATION));
        Optional.ofNullable(externalId).ifPresent(e -> headers.add(InventoryHeader.EXTERNAL_ID));
        Optional.ofNullable(weight).ifPresent(w -> headers.add(InventoryHeader.WEIGHT));
        Optional.ofNullable(formatQuantity).ifPresent(q -> headers.add(InventoryHeader.FORMAT_QUANTITY));

        return headers;
    }

    /**
     * Validates the record contents based on the defined record type.
     *
     * @param type the record type
     * @throws ValidationException if there is an error with the record
     * @see InventoryRecordType
     */
    public void validate(@NonNull final InventoryRecordType type) throws ValidationException {
        final List<InventoryHeader> definedHeaders = getDefinedHeaders();
        if (!definedHeaders.containsAll(InventoryHeader.getRequiredHeaders(type))) {
            throw new ValidationException("defined values for headers is missing required headers for type: "
                    + type.name());
        }

        final List<ValidationException.Descriptor> errors = new ArrayList<>(definedHeaders.size());
        for (int col = 0; col < definedHeaders.size(); ++col) {
            final InventoryHeader header = definedHeaders.get(col);
            try {
                header.getValidator().validate(getValue(header), header, null, col);
            } catch (final ValidationException ex) {
                errors.addAll(ex.getErrors());
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Record failed validation", errors);
        }
    }

    /**
     * Converts the record to a list of String values that represents the row.
     *
     * @param docCsvHeaders the list of headers to include in the row
     * @param type the record type
     * @return the record represented as a list of strings
     * @see InventoryHeader
     * @see InventoryRecordType
     */
    public List<String> toCsvRow(
            @NonNull final List<InventoryHeader> docCsvHeaders,
            @NonNull final InventoryRecordType type) {
        Validate.isTrue(docCsvHeaders.size() > 1, "docCsvHeaders must have > 1 defined values");
        Validate.isTrue(docCsvHeaders.containsAll(InventoryHeader.getRequiredHeaders(type)),
                "docCsvHeaders is missing required headers for type: " + type.name());

        return docCsvHeaders.stream()
                .map(this::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return getDefinedHeaders().stream()
                .map(this::getValue)
                .collect(Collectors.joining(","));
    }

    private String getValue(final InventoryHeader header) {
        switch (header) {
            case RELEASE_ID:
                return releaseId.toString();
            case PRICE:
                return price.toString();
            case MEDIA_CONDITION:
                return mediaCondition.getValue();
            case COMMENTS:
                return Optional.ofNullable(comments).orElse(StringUtils.EMPTY);
            case SLEEVE_CONDITION:
                return Optional.ofNullable(sleeveCondition)
                        .map(SleeveCondition::getValue)
                        .orElse(StringUtils.EMPTY);
            case ACCEPT_OFFER:
                return Optional.ofNullable(acceptOffer).orElse(StringUtils.EMPTY);
            case LOCATION:
                return Optional.ofNullable(location).orElse(StringUtils.EMPTY);
            case EXTERNAL_ID:
                return Optional.ofNullable(externalId).orElse(StringUtils.EMPTY);
            case WEIGHT:
                return Optional.ofNullable(weight)
                        .map(Object::toString)
                        .orElse(StringUtils.EMPTY);
            case FORMAT_QUANTITY:
                return Optional.ofNullable(formatQuantity)
                        .map(Object::toString)
                        .orElse(StringUtils.EMPTY);
            default:
                throw new IllegalArgumentException("Unrecognized header value: " + header);
        }
    }
}
