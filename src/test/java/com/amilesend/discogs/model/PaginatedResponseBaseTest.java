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
package com.amilesend.discogs.model;

import com.amilesend.client.parse.parser.GsonParser;
import com.amilesend.discogs.connection.DiscogsConnection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import okhttp3.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class PaginatedResponseBaseTest {
    @Mock
    private DiscogsConnection mockConnection;
    private PaginatedStringsResponse responseUnderTest;
    private PaginatedStringsResponse secondPageResponse;

    @BeforeEach
    public void setUp() {
        responseUnderTest = PaginatedStringsResponse.builder()
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .perPage(2)
                        .pages(2)
                        .items(4)
                        .urls(Map.of(
                              "first", "https://someurl/first",
                              "next", "https://someurl/next",
                              "last", "https://somrurl/last"))
                        .build())
                .values(List.of("1", "2"))
                .connection(mockConnection)
                .build();
        secondPageResponse = PaginatedStringsResponse.builder()
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .perPage(2)
                        .pages(2)
                        .items(4)
                        .urls(Map.of(
                                "first", "https://someurl/first",
                                "prev", "https://someurl/prev",
                                "last", "https://somrurl/last"))
                        .build())
                .values(List.of("3", "4"))
                .connection(mockConnection)
                .build();

//        final Request.Builder requestBuilder = new Request.Builder();
        //lenient().doReturn(requestBuilder).when(mockConnection).newRequestBuilder();
        //lenient().doReturn(secondPageResponse).when(mockConnection).execute(any(Request.class), any(GsonParser.class));
    }

    //////////
    // first
    //////////

    @Test
    public void hasFirst_withHavingFirstPage_shouldReturnTrue() {
        assertTrue(responseUnderTest.hasFirst());
    }

    @Test
    public void getFirst_withHavingFirstPage_shouldReturnFirstPage() {
        doReturn(new Request.Builder()).when(mockConnection).newRequestBuilder();
        doReturn(responseUnderTest).when(mockConnection).execute(any(Request.class), any(GsonParser.class));

        final PaginatedStringsResponse actual = responseUnderTest.getFirst();

        assertEquals(responseUnderTest, actual);
    }

    /////////////
    // previous
    /////////////

    @Test
    public void hasPrevious_withHavingPreviousPage_shouldReturnTrue() {
        assertTrue(secondPageResponse.hasFirst());
    }

    @Test
    public void hasPrevious_withoutPreviousPage_shouldReturnFalse() {
        assertFalse(responseUnderTest.hasPrevious());
    }

    @Test
    public void getPrevious_withHavingPreviousPage_shouldReturnFirstPage() {
        doReturn(new Request.Builder()).when(mockConnection).newRequestBuilder();
        doReturn(responseUnderTest).when(mockConnection).execute(any(Request.class), any(GsonParser.class));

        final PaginatedStringsResponse actual = secondPageResponse.getPrevious();

        assertEquals(responseUnderTest, actual);
    }

    @Test
    public void getPrevious_withoutHavingPreviousPage_shouldReturnNull() {
        assertNull(responseUnderTest.getPrevious());
    }

    /////////
    // next
    /////////

    @Test
    public void hasNext_withHavingNextPage_shouldReturnTrue() {
        assertTrue(responseUnderTest.hasNext());
    }

    @Test
    public void hasNext_withoutHavingNextPage_shouldReturnFalse() {
        assertFalse(secondPageResponse.hasNext());
    }

    @Test
    public void getNext_withHavingNextPage_shouldReturnSecondPage() {
        doReturn(new Request.Builder()).when(mockConnection).newRequestBuilder();
        doReturn(secondPageResponse).when(mockConnection).execute(any(Request.class), any(GsonParser.class));

        final PaginatedStringsResponse actual = responseUnderTest.getNext();

        assertEquals(secondPageResponse, actual);
    }

    @Test
    public void getNext_withoutHavingNextPage_shouldReturnNull() {
        assertNull(secondPageResponse.getNext());
    }

    /////////
    // last
    /////////

    @Test
    public void hasLast_withHavingLastPage_shouldReturnTrue() {
        assertTrue(responseUnderTest.hasLast());
    }

    @Test
    public void getLast_withHavingLastPage_shouldReturnLastPage() {
        doReturn(new Request.Builder()).when(mockConnection).newRequestBuilder();
        doReturn(secondPageResponse).when(mockConnection).execute(any(Request.class), any(GsonParser.class));

        final PaginatedStringsResponse actual = responseUnderTest.getLast();

        assertEquals(secondPageResponse, actual);
    }

    @SuperBuilder
    @Getter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class PaginatedStringsResponse extends PaginatedResponseBase<PaginatedStringsResponse> {
        private final List<String> values;

        @Override
        public Class<PaginatedStringsResponse> getType() {
            return PaginatedStringsResponse.class;
        }
    }
}
