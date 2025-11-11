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

import com.amilesend.client.util.StringUtils;
import com.amilesend.discogs.connection.DiscogsConnection;
import com.amilesend.discogs.model.PaginatedResponseBase;
import com.amilesend.discogs.model.database.GetArtistInformationResponse;
import com.amilesend.discogs.model.database.GetArtistReleasesResponse;
import com.amilesend.discogs.model.database.GetCommunityReleaseRatingResponse;
import com.amilesend.discogs.model.database.GetLabelInformationResponse;
import com.amilesend.discogs.model.database.GetLabelReleasesResponse;
import com.amilesend.discogs.model.database.GetMasterReleaseResponse;
import com.amilesend.discogs.model.database.GetMasterReleaseVersionsResponse;
import com.amilesend.discogs.model.database.GetUserReleaseRatingResponse;
import com.amilesend.discogs.model.database.SearchResponse;
import com.amilesend.discogs.model.database.UpdateUserReleaseRatingResponse;
import com.amilesend.discogs.model.database.type.Alias;
import com.amilesend.discogs.model.database.type.Image;
import com.amilesend.discogs.model.database.type.LabelResource;
import com.amilesend.discogs.model.database.type.MasterReleaseVersion;
import com.amilesend.discogs.model.database.type.Member;
import com.amilesend.discogs.model.database.type.Rating;
import com.amilesend.discogs.model.database.type.SearchResult;
import com.amilesend.discogs.model.database.type.SearchResultFormat;
import com.amilesend.discogs.model.database.type.SearchResultStat;
import com.amilesend.discogs.model.database.type.SearchResultUserData;
import com.amilesend.discogs.model.database.type.SearchType;
import com.amilesend.discogs.model.database.type.Stat;
import com.amilesend.discogs.model.database.type.Stats;
import com.amilesend.discogs.model.database.type.TrackInformation;
import com.amilesend.discogs.model.database.type.Video;
import com.amilesend.discogs.model.type.Artist;
import com.amilesend.discogs.model.type.CatalogEntity;
import com.amilesend.discogs.model.type.Community;
import com.amilesend.discogs.model.type.CommunityMember;
import com.amilesend.discogs.model.type.Format;
import com.amilesend.discogs.model.type.Release;
import com.amilesend.discogs.model.type.ReleaseIdentifier;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@UtilityClass
public class DatabaseApiDataHelper {

    ///////////////////////
    // GetReleaseResponse
    ///////////////////////

    public static Release newRelease() {
        return newRelease(0);
    }

    public static Release newRelease(final int index) {
        final String identifier = String.valueOf(1234567 + index);
        return Release.builder()
                .id(1234567L + index)
                .status("Accepted")
                .year(2000)
                .resourceUrl("https://api.discogs.com/releases/" + identifier)
                .uri("https://www.discogs.com/release/" + identifier + "-Test-Album")
                .artists(List.of(newArtist(1, null, true)))
                .artistsSort("Artist1")
                .labels(List.of(newLabel()))
                .series(Collections.emptyList())
                .companies(List.of(newCompany()))
                .formats(List.of(newFormat()))
                .dataQuality("Needs Vote")
                .community(newCommunity())
                .formatQuantity(1)
                .dateAdded(LocalDateTime.of(2009, 06, 26, 19, 24, 42, 0))
                .dateChanged(LocalDateTime.of(2024, 12, 9, 19, 0, 25, 0))
                .numForSale(40)
                .lowestPrice(3D)
                .masterId(12345678L)
                .masterUrl("https://api.discogs.com/masters/12345678")
                .title("Album Title")
                .country("US")
                .released(LocalDate.of(2000, 7, 7))
                .notes("Album Notes")
                .releasedFormatted("07 Jul 2000")
                .identifiers(List.of(
                        ReleaseIdentifier.builder()
                                .type("Barcode")
                                .value("0 9876-43212-2 8")
                                .description("Text")
                                .build(),
                        ReleaseIdentifier.builder()
                                .type("Barcode")
                                .value("098764321228")
                                .description("Reader")
                                .build()))
                .videos(List.of(Video.builder()
                        .uri("https://video.com/video")
                        .title("Artist - Album")
                        .description("Music Video")
                        .duration(0)
                        .embed(true)
                        .build()))
                .genres(List.of("Rock"))
                .styles(List.of("Heavy Metal", "Prog Rock", "Progressive Metal"))
                .trackList(List.of(newTrackInfo(1), newTrackInfo(2)))
                .extraArtists(List.of(
                        newArtist(2, "A&R", false),
                        newArtist(3, "Art Direction", false)))
                .images(List.of(newImage(true), newImage(false)))
                .thumb("https://i.discogs.com/ReleaseThumbnail.jpeg")
                .estimatedWeight(85)
                .isBlockedFromSale(false)
                .isOffensive(false)
                .build();
    }

    static Artist newArtist(final int index, final String role, final boolean isThumbnailIncluded) {
        return Artist.builder()
                .name("Artist" + index)
                .id(Long.parseLong("567890" + index))
                .anv(StringUtils.EMPTY)
                .join(StringUtils.EMPTY)
                .role(Objects.nonNull(role) ? role : StringUtils.EMPTY)
                .tracks(StringUtils.EMPTY)
                .resourceUrl("https://api.discogs.com/artists/567890" + index)
                .thumbnailUrl(isThumbnailIncluded ? "https://i.discogs.com/ArtistThumbnail" + index + ".jpeg" : null)
                .build();
    }

    private static CatalogEntity newLabel() {
        return CatalogEntity.builder()
                .name("Record Label")
                .catalogNumber("4 987423-1")
                .entityType("1")
                .entityTypeName("Label")
                .id(3456L)
                .resourceUrl("https://api.discogs.com/labels/3456")
                .thumbnailUrl("https://i.discogs.com/RecordLabelThumbnail.jpeg")
                .build();
    }

    private static CatalogEntity newCompany() {
        return CatalogEntity.builder()
                .name("Recording Corporation")
                .catalogNumber(StringUtils.EMPTY)
                .entityType("13")
                .entityTypeName("Phonographic Copyright (p)")
                .id(34567L)
                .resourceUrl("https://api.discogs.com/labels/34567")
                .build();
    }

    static Format newFormat() {
        return Format.builder()
                .name("CD")
                .qty(1)
                .descriptions(List.of("Album"))
                .build();
    }

    private static Community newCommunity() {
        return Community.builder()
                .have(2492)
                .want(247)
                .rating(Rating.builder()
                        .count(240)
                        .average(4.34D)
                        .build())
                .submitter(newCommunityMember(1))
                .contributors(List.of(newCommunityMember(1), newCommunityMember(2)))
                .dataQuality("Needs Vote")
                .status("Accepted")
                .build();
    }

    private static CommunityMember newCommunityMember(final int index) {
        return CommunityMember.builder()
                .username("UserName" + index)
                .resourceUrl("https://api.discogs.com/users/UserName" + index)
                .build();
    }

    private static TrackInformation newTrackInfo(final int num) {
        return TrackInformation.builder()
                .position(String.valueOf(num))
                .type("track")
                .title("Track" + num)
                .extraArtists(List.of(newArtist(1, "Music By Artist", false)))
                .duration("5:00")
                .build();
    }

    private static Image newImage(final boolean isPrimary) {
        final String typeStr = isPrimary ? "primary" : "secondary";
        return Image.builder()
                .type(typeStr)
                .uri("https://i.discogs.com/" + typeStr + ".jpeg")
                .resourceUrl("https://i.discogs.com/" + typeStr + ".jpeg")
                .uri150("https://i.discogs.com/" + typeStr + "150.jpeg")
                .width(600)
                .height(480)
                .build();
    }

    /////////////////////////////////
    // GetUserReleaseRatingResponse
    /////////////////////////////////

    public static GetUserReleaseRatingResponse newGetUserReleaseRatingResponse() {
        return GetUserReleaseRatingResponse.builder()
                .username("User")
                .releaseId(12345678L)
                .rating(3)
                .build();
    }

    ////////////////////////////////////
    // UpdateUserReleaseRatingResponse
    ////////////////////////////////////

    public static UpdateUserReleaseRatingResponse newUpdateUserReleaseRatingResponse() {
        return UpdateUserReleaseRatingResponse.builder()
                .username("User")
                .releaseId(12345678L)
                .rating(3)
                .build();
    }

    //////////////////////////////////////
    // GetCommunityReleaseRatingResponse
    //////////////////////////////////////

    public static GetCommunityReleaseRatingResponse newGetCommunityReleaseRatingResponse() {
        return GetCommunityReleaseRatingResponse.builder()
                .releaseId(12345678L)
                .rating(Rating.builder()
                        .average(5.0D)
                        .count(100)
                        .build())
                .build();
    }

    /////////////////////////////
    // GetMasterReleaseResponse
    /////////////////////////////

    public static GetMasterReleaseResponse newGetMasterReleaseResponse() {
        return GetMasterReleaseResponse.builder()
                .id(52086L)
                .mainRelease(1827596L)
                .mostRecentRelease(31573201L)
                .resourceUrl("https://api.discogs.com/masters/52086")
                .uri("https://www.discogs.com/master/52086-Dream-Theater-Images-And-Words")
                .versionsUrl("https://api.discogs.com/masters/52086/versions")
                .mainReleaseUrl("https://api.discogs.com/releases/1827596")
                .mostRecentReleaseUrl("https://api.discogs.com/releases/31573201")
                .numForSale(722)
                .lowestPrice(0.9D)
                .images(List.of(
                        Image.builder()
                                .type("primary")
                                .uri(StringUtils.EMPTY)
                                .resourceUrl(StringUtils.EMPTY)
                                .uri150(StringUtils.EMPTY)
                                .width(600)
                                .height(585)
                                .build(),
                        Image.builder()
                                .type("secondary")
                                .uri(StringUtils.EMPTY)
                                .resourceUrl(StringUtils.EMPTY)
                                .uri150(StringUtils.EMPTY)
                                .width(600)
                                .height(470)
                                .build()))
                .genres(List.of("Rock"))
                .styles(List.of("Heavy Metal", "Prog Rock", "Progressive Metal"))
                .year(1992)
                .trackList(List.of(
                        TrackInformation.builder()
                                .position("1")
                                .type("track")
                                .title("Pull Me Under")
                                .extraArtists(List.of(
                                        Artist.builder()
                                                .name("Dream Theater")
                                                .anv(StringUtils.EMPTY)
                                                .join(StringUtils.EMPTY)
                                                .role("Music By [Images]")
                                                .tracks(StringUtils.EMPTY)
                                                .id(260935L)
                                                .resourceUrl("https://api.discogs.com/artists/260935")
                                                .build(),
                                        Artist.builder()
                                                .name("Kevin Moore")
                                                .anv("Moore")
                                                .join(StringUtils.EMPTY)
                                                .role("Words By")
                                                .tracks(StringUtils.EMPTY)
                                                .id(130493L)
                                                .resourceUrl("https://api.discogs.com/artists/130493")
                                                .build()))
                                .duration("8:11")
                                .build(),
                        TrackInformation.builder()
                                .position("2")
                                .type("track")
                                .title("Another Day")
                                .extraArtists(List.of(
                                        Artist.builder()
                                                .name("Dream Theater")
                                                .anv(StringUtils.EMPTY)
                                                .join(StringUtils.EMPTY)
                                                .role("Music By [Images]")
                                                .tracks(StringUtils.EMPTY)
                                                .id(260935L)
                                                .resourceUrl("https://api.discogs.com/artists/260935")
                                                .build(),
                                        Artist.builder()
                                                .name("Jay Beckenstein")
                                                .anv(StringUtils.EMPTY)
                                                .join(StringUtils.EMPTY)
                                                .role("Soprano Saxophone")
                                                .tracks(StringUtils.EMPTY)
                                                .id(261358L)
                                                .resourceUrl("https://api.discogs.com/artists/261358")
                                                .build(),
                                        Artist.builder()
                                                .name("John Petrucci")
                                                .anv("Petrucci")
                                                .join(StringUtils.EMPTY)
                                                .role("Words By")
                                                .tracks(StringUtils.EMPTY)
                                                .id(273938L)
                                                .resourceUrl("https://api.discogs.com/artists/273938")
                                                .build()))
                                .duration("4:22")
                                .build()))
                .artists(List.of(
                        Artist.builder()
                                .name("Dream Theater")
                                .anv(StringUtils.EMPTY)
                                .join(StringUtils.EMPTY)
                                .role(StringUtils.EMPTY)
                                .tracks(StringUtils.EMPTY)
                                .id(260935L)
                                .resourceUrl("https://api.discogs.com/artists/260935")
                                .build()))
                .title("Images And Words")
                .dataQuality("Correct")
                .videos(List.of(
                        Video.builder()
                                .uri("https://www.youtube.com/watch?v\u003dmipc-JxrhRk")
                                .title("Dream Theater - Pull Me Under [OFFICIAL VIDEO]")
                                .description("Buy/stream our new album \u0027A View From The Top Of The World\u0027, out now!")
                                .duration(290)
                                .embed(true)
                                .build()))
                .build();
    }

    /////////////////////////////////////
    // GetMasterReleaseVersionsResponse
    /////////////////////////////////////

    public static GetMasterReleaseVersionsResponse newGetMasterReleaseVersionsResponse(final DiscogsConnection connection) {
        return GetMasterReleaseVersionsResponse.builder()
                .connection(connection)
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .page(1)
                        .pages(2)
                        .perPage(50)
                        .items(86)
                        .urls(Map.of(
                                "last", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50",
                                "next", "https://api.discogs.com/masters/52086/versions?page\u003d2\u0026per_page\u003d50"))
                        .build())
                .versions(List.of(
                        MasterReleaseVersion.builder()
                                .id(8924382L)
                                .label("ATCO Records")
                                .country("US")
                                .title("Images And Words")
                                .majorFormats(List.of("Cassette"))
                                .format("Album")
                                .catalogNumber("92148-4, 7 92148-4")
                                .released("1992")
                                .status("Accepted")
                                .resourceUrl("https://api.discogs.com/releases/8924382")
                                .thumb("https://i.discogs.com/-dpPoJu4-qn2Kh1CzjUbLuZyx340FxW_GmQhZZ1bRGI/ODItMTQ3MTU3NTMy/NC0zOTg2LmpwZWc.jpeg")
                                .stats(Stats.builder()
                                        .community(Stat.builder()
                                                .inWantList(111)
                                                .inCollection(385)
                                                .build())
                                        .user(Stat.builder()
                                                .inWantList(0)
                                                .inCollection(0)
                                                .build())
                                        .build())
                                .build()))
                .build();
    }


    /////////////////////////////////
    // GetArtistInformationResponse
    /////////////////////////////////

    public static GetArtistInformationResponse newGetArtistInformationResponse() {
        return GetArtistInformationResponse.builder()
                .name("Dream Theater")
                .id(260935L)
                .resourceUrl("https://api.discogs.com/artists/260935")
                .uri("https://www.discogs.com/artist/260935-Dream-Theater")
                .releasesUrl("https://api.discogs.com/artists/260935/releases")
                .images(List.of(
                        Image.builder()
                                .type("primary")
                                .uri("https://i.discogs.com/KV8WwalZL1qvW75I3335VYJLMHESj2CseyqMo5TpEec/LTgzOTIuanBlZw.jpeg")
                                .resourceUrl("https://i.discogs.com/KV8WwalZL1qvW75I3335VYJLMHESj2CseyqMo5TpEec/LTgzOTIuanBlZw.jpeg")
                                .uri150("https://i.discogs.com/e8aFZsy_ZDMLzmO3aG1PW8PJLC0FsnUkeio5bNh6ypk/LTgzOTIuanBlZw.jpeg")
                                .width(600)
                                .height(387)
                                .build(),
                        Image.builder()
                                .type("secondary")
                                .uri("https://i.discogs.com/ORaY0YnIfrTv9HamYqu65WkZ_wzn_dWEB6lRWajPdyg/LTE4OTUuanBlZw.jpeg")
                                .resourceUrl("https://i.discogs.com/ORaY0YnIfrTv9HamYqu65WkZ_wzn_dWEB6lRWajPdyg/LTE4OTUuanBlZw.jpeg")
                                .uri150("https://i.discogs.com/m72qTinZEGXRChbdMWbW-M0io0rwTye_IAv0F2bsUWU/LTE4OTUuanBlZw.jpeg")
                                .width(600)
                                .height(555)
                                .build()))
                .profile("American progressive metal band, formed in Boston, Massachusetts, USA in 1985.")
                .urls(List.of("https://dreamtheater.net/", "https://www.facebook.com/dreamtheater"))
                .nameVariations(List.of("Dream Theatre", "ドリーム・シアター"))
                .aliases(List.of(Alias.builder()
                        .id(1405184L)
                        .name("Majesty (10)")
                        .resourceUrl("https://api.discogs.com/artists/1405184")
                        .thumbnailUrl("https://i.discogs.com/8hDfbNwB5Bq1m4GLm99uwOkOFa9rzqe3LnyyY0zC5Tk/NC5qcGVn.jpeg")
                        .build()))
                .members(List.of(
                        Member.builder()
                                .id(130493L)
                                .name("Kevin Moore")
                                .resourceUrl("https://api.discogs.com/artists/130493")
                                .active(false)
                                .thumbnailUrl("https://i.discogs.com/K0eCFqa93xBSeBFhi3ixl_nrBVpsZYqwCRodR7oRVv4/LTY1MDcuanBlZw.jpeg")
                                .build(),
                        Member.builder()
                                .id(273937L)
                                .name("Mike Portnoy")
                                .resourceUrl("https://api.discogs.com/artists/273937")
                                .active(true)
                                .thumbnailUrl("https://i.discogs.com/QXxhAAOpJz6aoq0pfFCH6Xau0SviV6QA-m379BVAYh4/LTk4MzAuanBlZw.jpeg")
                                .build(),
                        Member.builder()
                                .id(273938L)
                                .name("John Petrucci")
                                .resourceUrl("https://api.discogs.com/artists/273938")
                                .active(true)
                                .thumbnailUrl("https://i.discogs.com/etSqsGLf6y1m-470MmXPKHsOGW3V9OHJhD8O5BN0CR8/LTk2OTEuanBlZw.jpeg")
                                .build(),
                        Member.builder()
                                .id(277596L)
                                .name("James LaBrie")
                                .resourceUrl("https://api.discogs.com/artists/277596")
                                .active(true)
                                .thumbnailUrl("https://i.discogs.com/4010wgiXPt6ohr_NNYmI2ncu6lbc08VRGdIZY97FxRY/LTMwMzYuanBlZw.jpeg")
                                .build(),
                        Member.builder()
                                .id(335835L)
                                .name("Jordan Rudess")
                                .resourceUrl("https://api.discogs.com/artists/335835")
                                .active(true)
                                .thumbnailUrl("https://i.discogs.com/uIwRUrn9v0WCGKEOI95OA_QuTdDHTMsddWgQS6Kj3bI/LTY4NTguanBlZw.jpeg")
                                .build(),
                        Member.builder()
                                .id(359443L)
                                .name("Derek Sherinian")
                                .resourceUrl("https://api.discogs.com/artists/359443")
                                .active(false)
                                .thumbnailUrl("https://i.discogs.com/yqFq7f2ChdgDnzmnp6y_jo0cF858XAm0ykdZhSwYzP8/LTgzODguanBlZw.jpeg")
                                .build(),
                        Member.builder()
                                .id(499846L)
                                .name("John Myung")
                                .resourceUrl("https://api.discogs.com/artists/499846")
                                .active(true)
                                .thumbnailUrl("https://i.discogs.com/ZhvmulHBx97KnVZyWmAkF7mDNdfih6MwG7DKfqCyyL8/LTg0MTcuanBlZw.jpeg")
                                .build(),
                        Member.builder()
                                .id(499848L)
                                .name("Charlie Dominici")
                                .resourceUrl("https://api.discogs.com/artists/499848")
                                .active(false)
                                .thumbnailUrl("https://i.discogs.com/s4K4zNiRO2lI3BabW2vxeSVNNk1Tmhnm_nsT6L8SOpM/LTE3MzkuanBlZw.jpeg")
                                .build(),
                        Member.builder()
                                .id(874960L)
                                .name("Mike Mangini (2)")
                                .resourceUrl("https://api.discogs.com/artists/874960")
                                .active(false)
                                .thumbnailUrl("https://i.discogs.com/c5Nai52BtLs-f3W3U7cl6ll2QOh05FoqEAZave267lY/LTgwMzMuanBlZw.jpeg")
                                .build()))
                .dataQuality("Needs Vote")
                .build();
    }

    //////////////////////////////
    // GetArtistReleasesResponse
    //////////////////////////////

    public static GetArtistReleasesResponse newGetArtistReleasesResponse() {
        return GetArtistReleasesResponse.builder()
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .page(1)
                        .pages(18)
                        .perPage(50)
                        .items(870)
                        .urls(Map.of(
                                "last", "https://api.discogs.com/artists/260935/releases?sort\u003dyear\u0026sort_order\u003ddesc\u0026page\u003d18\u0026per_page\u003d50",
                                "next", "https://api.discogs.com/artists/260935/releases?sort\u003dyear\u0026sort_order\u003ddesc\u0026page\u003d2\u0026per_page\u003d50"))
                        .build())
                .releases(List.of(
                        GetArtistReleasesResponse.ArtistRelease.builder()
                                .id(3743379L)
                                .title("Parasomnia")
                                .type("master")
                                .mainRelease(33070830L)
                                .artist("Dream Theater")
                                .role("Main")
                                .resourceUrl("https://api.discogs.com/masters/3743379")
                                .year(2025)
                                .thumb("https://i.discogs.com/Au7GIQ3MctL6w4bNGWgYKLTWHPYbO7XXgbzS9X8EgF8/MzctNDA3Mi5qcGVn.jpeg")
                                .stats(Stats.builder()
                                        .community(Stat.builder()
                                                .inWantList(8)
                                                .inCollection(33)
                                                .build())
                                        .user(Stat.builder()
                                                .inWantList(0)
                                                .inCollection(0)
                                                .build())
                                        .build())
                                .build(),
                        GetArtistReleasesResponse.ArtistRelease.builder()
                                .id(1507556L)
                                .title("On The Backs Of Angels")
                                .type("master")
                                .mainRelease(13267655L)
                                .artist("Dream Theater")
                                .role("Main")
                                .resourceUrl("https://api.discogs.com/masters/1507556")
                                .year(2011)
                                .thumb("https://i.discogs.com/UT_VOKnEqC7z2npVhm1jcqEQQxrLjEc-YhMrQljm514/MDYtNjgyOC5wbmc.jpeg")
                                .stats(Stats.builder()
                                        .community(Stat.builder()
                                                .inWantList(1)
                                                .inCollection(6)
                                                .build())
                                        .user(Stat.builder()
                                                .inWantList(0)
                                                .inCollection(0)
                                                .build())
                                        .build())
                                .build()))
                .build();
    }

    ////////////////////////////////
    // GetLabelInformationResponse
    ////////////////////////////////

    public static GetLabelInformationResponse newGetLabelInformationResponse() {
        return GetLabelInformationResponse.builder()
                .id(3456L)
                .name("Real Right Recordings")
                .resourceUrl("https://api.discogs.com/labels/3456")
                .uri("https://www.discogs.com/label/3456-Real-Right-Recordings")
                .releasesUrl("https://api.discogs.com/labels/3456/releases")
                .contactInfo("realrightrecordings@aol.com")
                .profile("[a119]'s sublabel based in Detroit, Michigan")
                .parentLabel(LabelResource.builder()
                        .id(788L)
                        .name("Intangible Records \u0026 Soundworks")
                        .resourceUrl("https://api.discogs.com/labels/788")
                        .build())
                .dataQuality("Correct")
                .build();
    }

    /////////////////////////////
    // GetLabelReleasesResponse
    /////////////////////////////

    public static GetLabelReleasesResponse newGetLabelReleasesResponse() {
        return GetLabelReleasesResponse.builder()
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .page(1)
                        .pages(1)
                        .perPage(50)
                        .items(2)
                        .build())
                .releases(List.of(
                        GetLabelReleasesResponse.LabelRelease.builder()
                                .id(58937L)
                                .resourceUrl("https://api.discogs.com/releases/58937")
                                .status("Accepted")
                                .format("CD, Comp, Mixed")
                                .catalogNumber("RRCD001")
                                .thumb("https://i.discogs.com/bSHEjTcC5NH6-liqVUfB0u6efK07_Er0XMZgLqCnzJ8/anBlZw.jpeg")
                                .title("Major Ice Volume #1")
                                .year(2001)
                                .artist("Terrence Parker")
                                .stats(Stats.builder()
                                        .community(Stat.builder()
                                                .inWantList(21)
                                                .inCollection(21)
                                                .build())
                                        .user(Stat.builder()
                                                .inWantList(0)
                                                .inCollection(0)
                                                .build())
                                        .build())
                                .build(),
                        GetLabelReleasesResponse.LabelRelease.builder()
                                .id(21554L)
                                .resourceUrl("https://api.discogs.com/releases/21554")
                                .status("Accepted")
                                .format("12\"")
                                .catalogNumber("RRV 001")
                                .thumb("https://i.discogs.com/i7FSdojzEjwlK9-Ab4LcLExwROZi2xwvww7HI69Sc1g/anBlZw.jpeg")
                                .title("Junk Yard Funk")
                                .year(2001)
                                .artist("Stacy Kidd")
                                .stats(Stats.builder()
                                        .community(Stat.builder()
                                                .inWantList(152)
                                                .inCollection(118)
                                                .build())
                                        .user(Stat.builder()
                                                .inWantList(0)
                                                .inCollection(0)
                                                .build())
                                        .build())
                                .build()))
                .build();
    }

    ///////////////////
    // SearchResponse
    ///////////////////

    public static SearchResponse newSearchResponse() {
        return SearchResponse.builder()
                .pagination(PaginatedResponseBase.Pagination.builder()
                        .page(1)
                        .pages(1)
                        .perPage(50)
                        .items(2)
                        .build())
                .results(List.of(
                        SearchResult.builder()
                                .id(1618299L)
                                .resourceUrl("https://api.discogs.com/masters/1618299")
                                .country("Japan")
                                .year("2019")
                                .format(List.of(
                                        "CD",
                                        "Album",
                                        "DVD",
                                        "Limited Edition"))
                                .label(List.of("Babymetal Records", "Tucky\u0027s Mastering"))
                                .type(SearchType.MASTER)
                                .genre(List.of("Rock", "Pop"))
                                .style(List.of(
                                        "Heavy Metal",
                                        "Alternative Rock",
                                        "J-Rock",
                                        "J-pop"))
                                .barcode(List.of("4988061866864"))
                                .userData(SearchResultUserData.builder()
                                        .inWantList(false)
                                        .inCollection(false)
                                        .build())
                                .masterId(1618299L)
                                .masterUrl("https://api.discogs.com/masters/1618299")
                                .uri("/master/1618299-Babymetal-Metal-Galaxy")
                                .catalogNumber("TFCC-86686")
                                .title("Babymetal - Metal Galaxy")
                                .thumb("https://i.discogs.com/_ACZnd-MUQ6lOuY7G1hBwnMUOm16uRNEd__hhuMUnWY/MjAtMTE0Mi5qcGVn.jpeg")
                                .coverImage("https://i.discogs.com/QzZoJWRSvRWQkBPVI8sK7s3DkXMhuAhJLVysE3v022s/MjAtMTE0Mi5qcGVn.jpeg")
                                .community(SearchResultStat.builder()
                                        .want(1632)
                                        .have(4004)
                                        .build())
                                .build(),
                        SearchResult.builder()
                                .id(14339492L)
                                .resourceUrl("https://api.discogs.com/releases/14339492")
                                .country("US")
                                .year("2019")
                                .format(List.of(
                                        "Vinyl",
                                        "LP",
                                        "Album",
                                        "Limited Edition"))
                                .label(List.of("Ear Music", "Babymetal Records"))
                                .type(SearchType.RELEASE)
                                .title("release")
                                .genre(List.of("Rock", "Pop"))
                                .style(List.of("Heavy Metal", "Alternative Rock", "J-pop"))
                                .barcode(List.of(
                                        "7112975237",
                                        "7 11297 52373 7",
                                        "4029759143468"))
                                .userData(SearchResultUserData.builder()
                                        .inWantList(false)
                                        .inCollection(false)
                                        .build())
                                .masterId(1618299L)
                                .masterUrl("https://api.discogs.com/masters/1618299")
                                .uri("/release/14339492-Babymetal-Metal-Galaxy")
                                .catalogNumber("0214346EMU")
                                .title("Babymetal - Metal Galaxy")
                                .thumb("https://i.discogs.com/z5XbCtvEFl5rlrHE6w0y_yPZ7-fmvyJXo4t-5DTeCHk/NDktMjA3OC5wbmc.jpeg")
                                .coverImage("https://i.discogs.com/Qk1jNss6zaVLu7fTjwIKKWPjOX5vBWHuSqUAqI9wyS0/NDkyLTE1NzMzMjM3/NDktMjA3OC5wbmc.jpeg")
                                .community(SearchResultStat.builder()
                                        .want(82)
                                        .have(100)
                                        .build())
                                .formatQuantity(2)
                                .formats(List.of(SearchResultFormat.builder()
                                        .name("Vinyl")
                                        .qty("2")
                                        .text("Transparent")
                                        .descriptions(List.of("LP", "Album", "Limited Edition"))
                                        .build()))
                                .build()))
                .build();
    }

    @UtilityClass
    public static class Responses {
        private static final String FOLDER = "/database/";

        public static final SerializedResource RELEASE = new SerializedResource(FOLDER + "Release.json");
        public static final SerializedResource USER_RELEASE_RATING =
                new SerializedResource(FOLDER + "UserReleaseRating.json");
        public static final SerializedResource COMMUNITY_RELEASE_RATING =
                new SerializedResource((FOLDER + "CommunityReleaseRating.json"));
        public static final SerializedResource MASTER_RELEASE =
                new SerializedResource(FOLDER + "MasterRelease.json");
        public static final SerializedResource MASTER_RELEASE_VERSIONS =
                new SerializedResource(FOLDER + "MasterReleaseVersions.json");
        public static final SerializedResource ARTIST_INFORMATION =
                new SerializedResource(FOLDER + "ArtistInformation.json");
        public static final SerializedResource ARTIST_RELEASES =
                new SerializedResource(FOLDER + "ArtistReleases.json");
        public static final SerializedResource LABEL_INFORMATION =
                new SerializedResource((FOLDER + "GetLabelInformationResponse.json"));
        public static final SerializedResource LABEL_RELEASES =
                new SerializedResource(FOLDER + "GetLabelReleasesResponse.json");
        public static final SerializedResource SEARCH_RESPONSE =
                new SerializedResource(FOLDER + "SearchResponse.json");
    }
}
