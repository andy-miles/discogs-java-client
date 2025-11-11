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
package com.amilesend.discogs.csv;

import com.amilesend.client.util.Validate;
import com.amilesend.client.util.VisibleForTesting;
import com.amilesend.discogs.csv.type.InventoryHeader;
import com.amilesend.discogs.csv.type.InventoryRecordType;
import com.amilesend.discogs.csv.validation.ValidationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility to read an inventory CSV file and validate its contents based on the specification as described
 * in the <a href="https://www.discogs.com/developers#page:inventory-upload">API Documentation</a>.
 */
@Slf4j
@RequiredArgsConstructor
public class InventoryCsvValidator {
    @NonNull
    private final CSVFormat csvFormat;

    /**
     * Creates a new {@code InventoryCsvValidator} instance with the default CSVFormat configured.
     *
     * @see CSVFormat#RFC4180
     */
    public InventoryCsvValidator() {
        this(CSVFormat.RFC4180.builder()
                .setSkipHeaderRecord(true)
                .get());
    }

    /**
     * Validates an inventory CSV file by:
     * <ul>
     *     <li>Validating that headers are recognized</li>
     *     <li>Required headers are present</li>
     *     <li>Contents adhere to column value constraints</li>
     * </ul>
     *
     * @param csvFile the CSV file to validate
     * @param type the type of operation that the CSV file is for (i.e., new inventory, or updating existing inventory
     *             items).
     * @throws IOException if an error occurred while attempting to read the file
     * @throws ValidationException if the file failed validation. See {@link ValidationException#getErrors()} for
     *                             a detailed description of each validation error
     * @see InventoryRecordType
     */
    public void validate(@NonNull final Path csvFile, @NonNull final InventoryRecordType type)
            throws IOException, ValidationException {
        validateCsvPath(csvFile);
        try (final CSVParser parser = getCSVParser(csvFile)) {
            // Converts and validates the headers
            final List<InventoryHeader> parsedHeaders = toInventoryHeaders(parser.getHeaderNames(), type);
            final List<ValidationException.Descriptor> errors = scan(parser, parsedHeaders);
            if (errors.isEmpty()) {
                return;
            }

            final String msg = new StringBuilder("File [")
                    .append(csvFile.getFileName())
                    .append("] failed validation")
                    .toString();
            throw new ValidationException(msg, errors);
        }
    }

    @VisibleForTesting
    CSVParser getCSVParser(final Path csvFile) throws IOException {
        return csvFormat.parse(new BufferedReader(new FileReader(csvFile.toFile(), StandardCharsets.UTF_8)));
    }

    @VisibleForTesting
    List<ValidationException.Descriptor> scan(
            final CSVParser parser,
            final List<InventoryHeader> parsedHeaders) {
        final List<ValidationException.Descriptor> errors = new LinkedList<>();
        int row = 1;
        // Iterate through the CSV file for record validation in O(n*m) time.
        for (final CSVRecord record : parser) {
            int col = 0;
            for (final InventoryHeader header : parsedHeaders) {
                try {
                    header.getValidator().validate(record.get(header.getHeader()), header, row, col);
                } catch (final ValidationException ex) {
                    errors.addAll(ex.getErrors());
                }
                ++col;
            }
            ++row;
        }

        return errors;
    }

    @VisibleForTesting
    List<InventoryHeader> toInventoryHeaders(
            final List<String> parsedHeaders,
            final InventoryRecordType type) throws ValidationException {
        validateHeaders(parsedHeaders, type);
        return parsedHeaders.stream()
                .map(InventoryHeader::fromValue)
                .collect(Collectors.toList());
    }

    @VisibleForTesting
    void validateRequiredHeaders(final List<String> headers, final InventoryRecordType type)
            throws ValidationException {
        final Set<String> required = InventoryHeader.getRequiredHeaders(type)
                .stream()
                .map(InventoryHeader::getHeader)
                .collect(Collectors.toSet());

        if (!headers.containsAll(required)) {
            throw new ValidationException("Parsed headers must contain all required headers: " + required);
        }
    }

    @VisibleForTesting
    void validateHeaders(final List<String> headers, final InventoryRecordType type) throws ValidationException {
        validateRequiredHeaders(headers, type);

        final List<ValidationException.Descriptor> unrecognized = new ArrayList<>(headers.size());
        for (int i = 0; i < headers.size(); ++i) {
            final String headerValue = headers.get(i);
            if (Objects.nonNull(InventoryHeader.fromValue(headerValue))) {
                continue;
            }

            unrecognized.add(ValidationException.Descriptor.builder()
                    .row(0)
                    .col(i)
                    .value(headerValue)
                    .message("Unrecognized value")
                    .build());
        }

        if (!unrecognized.isEmpty()) {
            throw new ValidationException("Malformed headers (unrecognized header values)", unrecognized);
        }
    }

    @VisibleForTesting
    void validateCsvPath(final Path csvFile) {
        Validate.isTrue(Files.exists(csvFile), "csvFile does not exist: " + csvFile);
        Validate.isTrue(Files.isReadable(csvFile), "csvFile must be readable: " + csvFile);
    }
}
