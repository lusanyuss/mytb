package com.mytb;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.common.Scopes;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ChannelSection;
import com.google.api.services.youtube.model.ChannelSectionListResponse;
import com.google.api.services.youtube.model.GuideCategory;
import com.google.api.services.youtube.model.GuideCategoryListResponse;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoCategory;
import com.google.api.services.youtube.model.VideoCategoryListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeUtil {

    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    public static final String KEY = "AIzaSyCrarQG6vxxvFUUAfjhzHbGw9-teMzwCgo";
    public static final String[] SCOPES = {Scopes.PROFILE, YouTubeScopes.YOUTUBE};

    /**
     * @param context
     * @param keywords
     * @return
     * @throws Exception
     */

    public static List<VideoItem> search(Context context, String keywords) throws Exception {
        MyLog.v("---------------------search");
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();
        List<VideoItem> items = new ArrayList<VideoItem>();
        YouTube.Search.List query = youtube.search().list("id,snippet");
        query.setKey(KEY);
        query.setType("video");
        query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
        query.setQ(keywords);
        SearchListResponse response = query.execute();
        List<SearchResult> results = response.getItems();
        for (SearchResult result : results) {
            VideoItem item = new VideoItem();
            items.add(item);
        }
        MyLog.v(items.size() + "");
        return items;
    }

    /**
     * @param context
     * @param categoryId
     * @return
     * @throws Exception
     */

    public static List<ChannelItem> searchChannels(Context context, String categoryId) throws Exception {
        MyLog.v("---------------------searchChannels");
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();
        List<ChannelItem> items = new ArrayList<ChannelItem>();
        try {
            YouTube.Channels.List query = youtube.channels().list("id,snippet,brandingSettings,localizations");
            query.setKey(KEY);
            query.setCategoryId(categoryId);
            ChannelListResponse response = query.execute();
            List<Channel> results = response.getItems();

            for (Channel result : results) {
                ChannelItem item = new ChannelItem();
                item.id = result.getId();
                items.add(item);
            }
            MyLog.v(items.size() + "");
        } catch (IOException e) {

        }
        return items;
    }


    /**
     * @param context
     * @param playlistId
     * @return
     * @throws Exception
     */
    public static List<VideoItem> searchVideoList(Context context, String playlistId) throws Exception {
        MyLog.v("---------------------searchVideoList");
        List<VideoItem> items = new ArrayList<VideoItem>();
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        List<PlaylistItem> itemList = new ArrayList<PlaylistItem>();
        // Retrieve the playlist of the channel's uploaded videos.
        YouTube.PlaylistItems.List itemRequest =
                youtube.playlistItems().list("id,contentDetails,snippet");
        itemRequest.setKey(KEY);
        itemRequest.setPlaylistId(playlistId);
        String nextToken = "";
//        do {
        itemRequest.setPageToken(nextToken);
        PlaylistItemListResponse itemListResponse = itemRequest.execute();
        itemList.addAll(itemListResponse.getItems());
//            nextToken = itemListResponse.getNextPageToken();
//        } while (nextToken != null);


        for (PlaylistItem playlistItem : itemList) {
            VideoItem item = new VideoItem();
            item.id = playlistItem.getId();
            items.add(item);
        }
        MyLog.v(items.size() + "");
        return items;
    }

    /**
     * 我我
     *
     * @param context
     * @param channelId
     * @return
     * @throws Exception
     */

    public static List<VideoLists> searchVideoLists(Context context, String channelId) throws Exception {
        MyLog.v("---------------------searchVideoLists");
        List<VideoLists> items = new ArrayList<VideoLists>();
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();
        YouTube.Playlists.List mPlaylistsList = youtube.playlists().
                list("id,snippet,localizations");
        mPlaylistsList.setKey(KEY);
        mPlaylistsList.setChannelId(channelId);
        PlaylistListResponse playlistListResponse = mPlaylistsList.execute();
        List<Playlist> playlists = playlistListResponse.getItems();

        if (playlists.isEmpty()) {
            System.out.println("Can't find a playlist with channelId: " + channelId);
            return items;
        }
        for (Playlist result : playlists) {
            VideoLists item = new VideoLists();
            item.id = result.getId();
            items.add(item);
        }
        MyLog.v(items.size() + "");
        return items;
    }

    public static List<Category> searchCategorys(Context context, String hl, String regionCode) throws Exception {
        MyLog.v("---------------------searchCategorys");
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        List<Category> items = new ArrayList<Category>();
        YouTube.GuideCategories.List mYouTube = youtube.guideCategories().
                list("id,snippet");
        mYouTube.setKey(KEY);
        if (!TextUtils.isEmpty(hl)) mYouTube.setHl(hl);
        if (!TextUtils.isEmpty(regionCode)) mYouTube.setRegionCode(regionCode);

        GuideCategoryListResponse listResponse = mYouTube.execute();
        List<GuideCategory> guideCategories = listResponse.getItems();

        if (guideCategories.isEmpty()) {
            return items;
        }
        for (GuideCategory result : guideCategories) {
            Category item = new Category();
            item.id = result.getId();
            items.add(item);
        }
        MyLog.v(items.size() + "");
        return items;
    }


    public static List<ChannelSec> searchChannelSecs(Context context, String channelId) throws Exception {
        MyLog.v("---------------------searchChannelSecs");
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        List<ChannelSec> items = new ArrayList<ChannelSec>();
        YouTube.ChannelSections.List mYouTube = youtube.channelSections().
                list("id,snippet");
        mYouTube.setKey(KEY);
        mYouTube.setChannelId(channelId);

        ChannelSectionListResponse listResponse = mYouTube.execute();
        List<ChannelSection> channelSections = listResponse.getItems();

        if (channelSections.isEmpty()) {
            return items;
        }

        for (ChannelSection result : channelSections) {
            ChannelSec item = new ChannelSec();
            item.id = result.getId();
            items.add(item);
        }
        MyLog.v(items.size() + "");
        return items;
    }


    public static List<VideoCate> searchVideoCategorys(Context context, String regionCode) throws Exception {
        MyLog.v("---------------------searchChannelSecs");
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        List<VideoCate> items = new ArrayList<VideoCate>();
        YouTube.VideoCategories.List mYouTube = youtube.videoCategories().
                list("id,snippet");
        mYouTube.setKey(KEY);
        mYouTube.setRegionCode(regionCode);

        VideoCategoryListResponse listResponse = mYouTube.execute();
        List<VideoCategory> categories = listResponse.getItems();

        if (categories.isEmpty()) {
            return items;
        }
        for (VideoCategory result : categories) {
            VideoCate item = new VideoCate();
            item.id = result.getId();
            items.add(item);
        }
        MyLog.v(items.size() + "");
        return items;
    }


    public static List<VideoItem> searchVideos(Context context, String regionCode) throws Exception {
        MyLog.v("---------------------searchChannelSecs");
        YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        List<VideoItem> items = new ArrayList<VideoItem>();

        YouTube.VideoCategories.List mYouTube = youtube.videoCategories().
                list("id,snippet");
        mYouTube.setKey(KEY);
        mYouTube.setRegionCode(regionCode);

        VideoCategoryListResponse listResponse = mYouTube.execute();
        List<VideoCategory> categories = listResponse.getItems();

        if (categories.isEmpty()) {
            return items;
        }
        for (VideoCategory result : categories) {
            VideoItem item = new VideoItem();
            item.id = result.getId();
            items.add(item);
        }
        MyLog.v(items.size() + "");
        return items;
    }


}