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

import com.amilesend.discogs.csv.validation.ValidationException;
import com.amilesend.discogs.model.marketplace.type.Condition;
import com.amilesend.discogs.model.marketplace.type.SleeveCondition;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InventoryRecordTest {

    //////////////////////
    // getDefinedHeaders
    //////////////////////

    @Test
    public void getDefinedHeaders_withAllAttributesDefined_shouldReturnAllDefinedHeaders() {
        final InventoryRecord record = newValidInventoryCsvRecord();

        final List<InventoryHeader> actual = record.getDefinedHeaders();

        final List<InventoryHeader> expected = getAllInventoryHeaders();
        assertEquals(expected, actual);
    }

    @Test
    public void getDefinedHeaders_withOnlyReleaseIdDefined_shouldReturnOneDefinedHeader() {
        final InventoryRecord record = InventoryRecord.builder().releaseId(1).build();

        final List<InventoryHeader> actual = record.getDefinedHeaders();

        final List<InventoryHeader> expected = List.of(InventoryHeader.RELEASE_ID);
        assertEquals(expected, actual);
    }

    /////////////
    // validate
    /////////////

    @Test
    @SneakyThrows
    public void validate_withValidRecord_shouldDoNothing() {
        final InventoryRecord record = newValidInventoryCsvRecord();
        record.validate(InventoryRecordType.NEW);
    }

    @Test
    @SneakyThrows
    public void validate_withInvalidRecord_shouldThrowException() {
        final InventoryRecord record = InventoryRecord.builder()
                .releaseId(1)
                .price(9.99D)
                .mediaCondition(Condition.GOOD)
                .sleeveCondition(SleeveCondition.GOOD)
                .comments("Comments")
                .acceptOffer("SomeWrongValue") // <--
                .location("Location")
                .externalId("ExternalId")
                .weight(1)
                .formatQuantity(10)
                .build();

        final ValidationException thrown = assertThrows(ValidationException.class,
                () -> record.validate(InventoryRecordType.NEW));

        final List<ValidationException.Descriptor> errors = thrown.getErrors();
        assertEquals(1, errors.size());
        final ValidationException.Descriptor error = errors.get(0);
        assertAll(
                () -> assertEquals("SomeWrongValue", error.getValue()),
                () -> assertEquals(InventoryHeader.ACCEPT_OFFER, error.getHeader()),
                () -> assertEquals(5, error.getCol()),
                () -> assertEquals("[accept_offer] value must be \"Y\" or \"N\"", error.getMessage()));
    }

    @Test
    @SneakyThrows
    public void validate_withMissingRequiredHeaders_shouldThrowException() {
        final InventoryRecord record = InventoryRecord.builder()
                .releaseId(1)
                .price(9.99D)
                // Missing media condition
                .sleeveCondition(SleeveCondition.GOOD)
                .comments("Comments")
                .acceptOffer("N")
                .location("Location")
                .externalId("ExternalId")
                .weight(1)
                .formatQuantity(10)
                .build();

        final ValidationException thrown = assertThrows(ValidationException.class,
                () -> record.validate(InventoryRecordType.NEW));

        assertEquals("defined values for headers is missing required headers for type: NEW", thrown.getMessage());
    }

    @Test
    @SneakyThrows
    public void validate_withNullType_shouldThrowException() {
        final InventoryRecord record = newValidInventoryCsvRecord();
        assertThrows(NullPointerException.class, () -> record.validate(null));
    }

    /////////////
    // toCsvRow
    /////////////

    @Test
    @SneakyThrows
    public void toCsvRow_withValidDocHeadersAndType_shouldReturnAttributeList() {
        final InventoryRecord record = newValidInventoryCsvRecord();
        final List<InventoryHeader> headersToInclude = getAllInventoryHeaders();
        final List<String> expected = List.of(
                "1",
                "9.99",
                "Good (G)",
                "Good (G)",
                "Comments",
                "N",
                "Location",
                "ExternalId",
                "1",
                "10");

        final List<String> actual = record.toCsvRow(headersToInclude, InventoryRecordType.NEW);

        assertEquals(expected, actual);
    }

    @Test
    @SneakyThrows
    public void toCsvRow_withInvalidRecordType_shouldThrowException() {
        final InventoryRecord record = newValidInventoryCsvRecord();
        final List<InventoryHeader> headers = getAllInventoryHeaders();

        assertThrows(NullPointerException.class, () -> record.toCsvRow(headers, null));
    }

    @Test
    @SneakyThrows
    public void toCsvRow_withInvalidDocHeaders_shouldThrowException() {
        final InventoryRecord record = newValidInventoryCsvRecord();
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> record.toCsvRow(null, InventoryRecordType.NEW)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> record.toCsvRow(
                                Collections.singletonList(InventoryHeader.RELEASE_ID),
                                InventoryRecordType.NEW)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> record.toCsvRow(
                                List.of(
                                        InventoryHeader.RELEASE_ID,
                                        InventoryHeader.PRICE),
                                InventoryRecordType.NEW)));
    }

    /////////////
    // toString
    /////////////

    @Test
    @SneakyThrows
    public void toString_withValidRecord_shouldReturnString() {
        final InventoryRecord record = newValidInventoryCsvRecord();
        final String expected = "1,9.99,Good (G),Good (G),Comments,N,Location,ExternalId,1,10";

        final String actual = record.toString();

        assertEquals(expected, actual);
    }

    private static InventoryRecord newValidInventoryCsvRecord() {
        return InventoryRecord.builder()
                .releaseId(1)
                .price(9.99D)
                .mediaCondition(Condition.GOOD)
                .sleeveCondition(SleeveCondition.GOOD)
                .comments("Comments")
                .acceptOffer("N")
                .location("Location")
                .externalId("ExternalId")
                .weight(1)
                .formatQuantity(10)
                .build();
    }

    private static List<InventoryHeader> getAllInventoryHeaders() {
        return List.of(
                InventoryHeader.RELEASE_ID,
                InventoryHeader.PRICE,
                InventoryHeader.MEDIA_CONDITION,
                InventoryHeader.SLEEVE_CONDITION,
                InventoryHeader.COMMENTS,
                InventoryHeader.ACCEPT_OFFER,
                InventoryHeader.LOCATION,
                InventoryHeader.EXTERNAL_ID,
                InventoryHeader.WEIGHT,
                InventoryHeader.FORMAT_QUANTITY);
    }
}
