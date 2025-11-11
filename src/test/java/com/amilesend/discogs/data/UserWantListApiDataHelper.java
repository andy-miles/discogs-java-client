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
package com.amilesend.discogs.data;

import com.amilesend.discogs.model.type.BasicInformation;
import com.amilesend.discogs.model.wantlist.AddReleaseToWantListResponse;
import com.amilesend.discogs.model.wantlist.GetWantListResponse;
import com.amilesend.discogs.model.wantlist.UpdateReleaseOnWantListResponse;
import com.amilesend.discogs.model.wantlist.type.WantListRelease;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;

import static com.amilesend.discogs.data.DatabaseApiDataHelper.newArtist;
import static com.amilesend.discogs.data.DatabaseApiDataHelper.newFormat;
import static com.amilesend.discogs.data.UserIdentityApiDataHelper.newLabel;
import static com.amilesend.discogs.data.UserIdentityApiDataHelper.newPagination;

@UtilityClass
public class UserWantListApiDataHelper {
    ////////////////////////
    // GetWantListResponse
    ////////////////////////

    public static GetWantListResponse newGetWantListResponse() {
        return GetWantListResponse.builder()
                .wants(List.of(newWantListRelease(1), newWantListRelease(2)))
                .pagination(newPagination())
                .build();
    }

    public static WantListRelease newWantListRelease(final int index) {
        return WantListRelease.builder()
                .instanceId(400L + index)
                .rating(7)
                .basicInformation(newBasicInformation(index))
                .folderId(600L + index)
                .dateAdded(LocalDateTime.of(2000, 11, 12, 14, 20, 9))
                .notes("Notes " + index)
                .build();
    }

    private static BasicInformation newBasicInformation(final int index) {
        final long idValue = 500L + index;
        return BasicInformation.builder()
                .id(idValue)
                .resourceUrl("https://someurl/" + idValue)
                .labels(List.of(newLabel(1), newLabel(2)))
                .formats(List.of(newFormat()))
                .thumb("https://someurl/thumb" + index)
                .coverImage("https://someurl/cover" + index + ".jpg")
                .title("Title " + index)
                .artists(List.of(
                        newArtist(1, "role 1", false),
                        newArtist(2, "role 2", true)))
                .genres(List.of("Rock", "Metal"))
                .styles(List.of("Style 1", "Style 2"))
                .year(2000)
                .build();
    }

    /////////////////////////////////
    // AddReleaseToWantListResponse
    /////////////////////////////////

    public static AddReleaseToWantListResponse newAddReleaseToWantListResponse() {
        return AddReleaseToWantListResponse.builder()
                .instanceId(400L)
                .rating(7)
                .basicInformation(newBasicInformation(1))
                .folderId(600L)
                .dateAdded(LocalDateTime.of(2000, 11, 12, 14, 20, 9))
                .notes("Notes")
                .build();
    }

    ////////////////////////////////////
    // UpdateReleaseOnWantListResponse
    ////////////////////////////////////

    public static UpdateReleaseOnWantListResponse newUpdateReleaseOnWantListResponse() {
        return UpdateReleaseOnWantListResponse.builder()
                .instanceId(403L)
                .rating(9)
                .basicInformation(newBasicInformation(1))
                .folderId(601L)
                .dateAdded(LocalDateTime.of(2015, 11, 12, 14, 20, 9))
                .notes("Notes")
                .build();
    }

    @UtilityClass
    public static class Responses {
        private static final String FOLDER = "/userwant/";

        public static SerializedResource GET_WANT_LIST_RESPONSE =
                new SerializedResource(FOLDER + "GetWantListResponse.json");
        public static SerializedResource ADD_RELEASE_TO_WANT_LIST_RESPONSE =
                new SerializedResource(FOLDER + "AddReleaseToWantListResponse.json");
        public static SerializedResource UPDATE_RELEASE_ON_WANT_LIST_RESPONSE =
                new SerializedResource(FOLDER + "UpdateReleaseOnWantListResponse.json");
    }
}
