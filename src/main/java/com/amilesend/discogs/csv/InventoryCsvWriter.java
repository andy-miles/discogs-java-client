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
package com.amilesend.discogs.csv;

import com.amilesend.client.util.Validate;
import com.amilesend.discogs.csv.type.InventoryHeader;
import com.amilesend.discogs.csv.type.InventoryRecord;
import com.amilesend.discogs.csv.type.InventoryRecordType;
import com.amilesend.discogs.csv.validation.ValidationException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility to write an inventory CSV file used for Discogs inventory management (i.e., creating new inventory items,
 * or update existing inventory items).
 */
@Getter
@EqualsAndHashCode
public class InventoryCsvWriter implements AutoCloseable {
    /** The path of the CSV file to write. */
    private final Path csvFile;
    /** Indicator to append an existing file, or to overwrite and create a new CSV file. */
    private final boolean isAppended;
    /**
     * The list of defined CSV headers to be included in the CSV file.
     *
     * @see InventoryHeader
     */
    private final List<InventoryHeader> headers;
    /** The printer used to format and write to the file. */
    private final CSVPrinter csvPrinter;
    /**
     * The record type (i.e., for new inventory items, or to update existing items).
     *
     * @see InventoryRecordType
     */
    private final InventoryRecordType recordType;

    @Builder
    private InventoryCsvWriter(
            @NonNull final Path csvFile,
            @NonNull final List<InventoryHeader> headers,
            @NonNull final InventoryRecordType recordType,
            final CSVFormat csvFormat,
            final boolean isAppended) throws IOException, ValidationException {
        this.headers = new ArrayList<>(headers);
        this.isAppended = isAppended;
        this.recordType = recordType;

        validateRequiredHeaders(headers, recordType);

        if (isAppended) {
            Validate.isTrue(Files.isRegularFile(csvFile), "CSV file must already exist and be regular");
            Validate.isTrue(Files.isWritable(csvFile), "CSV file must be writable: " + csvFile);
            this.csvFile = csvFile;
        } else {
            Files.deleteIfExists(csvFile);
            Files.createDirectories(csvFile);
            this.csvFile = Files.createFile(csvFile);
        }

        this.csvPrinter = Optional.ofNullable(csvFormat)
                .orElse(CSVFormat.RFC4180)
                .builder()
                .setHeader(toCsvHeaders(this.headers))
                .get()
                .print(this.csvFile, StandardCharsets.UTF_8);
    }

    /**
     * Writes a record to the CSV file.
     *
     * @param record the record to write
     * @throws ValidationException if there is an error with the record
     * @throws IOException if there is an issue writing the record to the file
     */
    public void write(@NonNull final InventoryRecord record) throws ValidationException, IOException {
        record.validate(recordType);
        csvPrinter.printRecord(record.toCsvRow(headers, recordType));
    }

    @Override
    public void close() throws Exception {
        csvPrinter.close();
    }

    @Override
    public String toString() {
        return new StringBuilder("InventoryCsvWriter [")
                .append(recordType.name())
                .append("]: ")
                .append(csvFile)
                .toString();
    }

    private static void validateRequiredHeaders(
            final List<InventoryHeader> headers,
            final InventoryRecordType type) {
        final List<InventoryHeader> required = InventoryHeader.getRequiredHeaders(type);

        if (!headers.containsAll(required)) {
            throw new IllegalArgumentException("Parsed headers must contain all required headers: " + required);
        }
    }

    private static String[] toCsvHeaders(final List<InventoryHeader> headers) {
        final List<String> strHeaders = headers.stream()
                .map(InventoryHeader::getHeader)
                .collect(Collectors.toList());
        final String[] csvHeaders = new String[headers.size()];
        return strHeaders.toArray(csvHeaders);
    }
}
